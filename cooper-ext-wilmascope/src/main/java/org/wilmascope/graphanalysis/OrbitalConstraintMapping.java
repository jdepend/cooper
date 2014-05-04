/*
 * The following source code is part of the WilmaScope 3D Graph Drawing Engine
 * which is distributed under the terms of the GNU Lesser General Public License
 * (LGPL - http://www.gnu.org/copyleft/lesser.html).
 *
 * As usual we distribute it with no warranties and anything you chose to do
 * with it you do at your own risk.
 *
 * Copyright for this work is retained by Tim Dwyer and the WilmaScope organisation
 * (www.wilmascope.org) however it may be used or modified to work as part of
 * other software subject to the terms of the LGPL.  I only ask that you cite
 * WilmaScope as an influence and inform us (tgdwyer@yahoo.com)
 * if you do anything really cool with it.
 *
 * The WilmaScope software source repository is hosted by Source Forge:
 * www.sourceforge.net/projects/wilma
 *
 * -- Tim Dwyer, 2001
 */
package org.wilmascope.graphanalysis;

import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.wilmascope.control.WilmaMain;
import org.wilmascope.control.GraphControl.Cluster;
import org.wilmascope.control.GraphControl.Node;
import org.wilmascope.graph.LayoutEngine;

/**
 * A mapping from a specified float node attribute to radial constraints
 * 
 * @author dwyer
 */
public class OrbitalConstraintMapping extends VisualMapping {
	public OrbitalConstraintMapping() {
		super("Node Orbital Constraints");
	}

	String[] partitions;

	JList partitionList = new JList();

	JSpinner orbitsSpinner;

	int orbits = 3;

	public void apply(Cluster c, String analysisType) {
		int nodeCount = c.getNodes().length;
		float[] attrs = new float[nodeCount];
		if (nodeCount < orbits) {
			WilmaMain.showErrorDialog("More orbits than nodes in graph!", new Exception());
		}
		int i = 0;
		for (Node n : c.getNodes()) {
			attrs[i++] = Float.parseFloat(n.getProperties().getProperty(analysisType));
		}
		Arrays.sort(attrs);
		float pivotInterval = (float) nodeCount / (float) orbits;
		float[] pivots = new float[orbits];
		partitions = new String[orbits];
		for (i = 0; i < orbits; i++) {
			pivots[i] = attrs[(int) Math.floor(i * pivotInterval)];
			if (i > 0) {
				partitions[i] = "" + pivots[i];
			}
		}
		for (Node n : c.getNodes()) {
			float attr = Float.parseFloat(n.getProperties().getProperty(analysisType));
			for (i = 0; i < orbits; i++) {
				if (attr >= pivots[i]) {
					n.setProperty("OrbitConstraint", "" + (orbits - i));
				}
			}
		}
		LayoutEngine l = c.getLayoutEngine();
		l.getProperties().setProperty("Orbits", "" + orbits);
		l.resetProperties();
		c.unfreeze();
		partitionList.setListData(partitions);
	}

	public JPanel getControls() {
		JPanel p = new JPanel();
		Box b = Box.createVerticalBox();
		Box h = Box.createHorizontalBox();
		SpinnerNumberModel sm = new SpinnerNumberModel(orbits, 1, 10, 1);
		orbitsSpinner = new JSpinner(sm);
		orbitsSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				orbits = ((Integer) orbitsSpinner.getValue()).intValue();
			}
		});
		h.add(new JLabel("Orbits"));
		h.add(orbitsSpinner);
		b.add(h);
		Box partitions = Box.createHorizontalBox();
		partitions.add(new JLabel("Partitions"));
		partitions.add(partitionList);
		b.add(partitions);
		p.add(b);
		return p;
	}
}
