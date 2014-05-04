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
package org.wilmascope.graphgen.plugin;

import java.util.TreeMap;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.wilmascope.control.GraphControl;
import org.wilmascope.global.RandomGenerator;
import org.wilmascope.graphgen.GraphGenerator;
import org.wilmascope.gui.SpinnerSlider;

/**
 * Generates a "grid graph", that is, a rectangular lattice of arbitrary width
 * and height.
 * 
 * @author dwyer
 */
public class GridGenerator extends GraphGenerator {
	int width = 10;

	int height = 10;

	boolean threeDimensional = true;

	JPanel controlPanel = new JPanel();

	public GridGenerator() {
		final SpinnerSlider widthSlider = new SpinnerSlider("Width", 10, 2000, width);
		final SpinnerSlider heightSlider = new SpinnerSlider("Height", 10, 2000, height);
		widthSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				width = widthSlider.getValue();
			}
		});
		heightSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				height = heightSlider.getValue();
			}
		});
		final JCheckBox threeDimButton = new JCheckBox("3D Positions");
		threeDimButton.setSelected(threeDimensional);
		threeDimButton.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				threeDimensional = threeDimButton.isSelected();
			}

		});
		controlPanel.add(widthSlider);
		controlPanel.add(heightSlider);
		controlPanel.add(threeDimButton);
	}

	GraphControl.Cluster root;

	public String getName() {
		return "Grid";
	}

	/**
	 * Generates a "grid graph", that is, a rectangular lattice
	 */
	public void generate(GraphControl gc) {
		lookup = new TreeMap();
		root = gc.getRootCluster();
		// LayoutEngine layout = new FastLayout(cluster, threeD);
		// root.setLayoutEngine(layout);

		root.deleteAll();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				String lx = Integer.toString(i - 1);
				String ly = Integer.toString(j - 1);
				String x = Integer.toString(i);
				String y = Integer.toString(j);
				if (j > 0)
					addEdge(x + "," + ly, x + "," + y, threeDimensional);
				if (i > 0)
					addEdge(lx + "," + y, x + "," + y, threeDimensional);
			}
		}

		gc.getRootCluster().getCluster().draw();
	}

	TreeMap lookup;

	private void addEdge(String start, String end, boolean threeD) {
		root.addEdge(lookupNode(start, threeD), lookupNode(end, threeD), getEdgeView());
	}

	private GraphControl.Node lookupNode(String id, boolean threeD) {
		GraphControl.Node n = (GraphControl.Node) lookup.get(id);
		if (n == null) {
			n = root.addNode(getNodeView());
			// n.setColour(rand.nextFloat(),rand.nextFloat(),rand.nextFloat());
			n.setColour(0.5f, 0.5f, 0.5f);
			n.setPosition(RandomGenerator.randomPoint(threeD));
			lookup.put(id, n);
		}
		return n;
	}

	public JPanel getControls() {
		return controlPanel;
	}

}
