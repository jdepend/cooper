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
package org.wilmascope.graphmodifiers.plugin;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.wilmascope.control.GraphControl.Cluster;
import org.wilmascope.graph.Edge;
import org.wilmascope.graph.EdgeList;
import org.wilmascope.graphmodifiers.GraphModifier;

/**
 * Removes directed cycles
 * 
 * @author dwyer
 */
public class DirectedCycleRemoval extends GraphModifier {
	JPanel controls;
	static final int GREEDY = 0;
	static final int ENHANCED = 1;
	int algorithm = ENHANCED;

	public DirectedCycleRemoval() {
		controls = new JPanel();
		Box box = Box.createVerticalBox();
		JRadioButton greedyButton = new JRadioButton("Greedy Algorithm");
		JRadioButton enhancedButton = new JRadioButton("Enhanced Greedy Algorithm");
		ButtonGroup bg = new ButtonGroup();
		bg.add(greedyButton);
		bg.add(enhancedButton);
		box.add(greedyButton);
		box.add(enhancedButton);
		greedyButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				algorithm = GREEDY;
			}
		});
		enhancedButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				algorithm = ENHANCED;
			}
		});
	}

	/**
	 * @see org.wilmascope.graphmodifiers.GraphModifier#getName()
	 */
	public String getName() {
		return "Directed Cycle Removal";
	}

	/**
	 * @see org.wilmascope.graphmodifiers.GraphModifier#modify(org.wilmascope.graph.Cluster)
	 */
	public void modify(Cluster cluster) {
		EdgeList acyclicEdges = null;
		switch (algorithm) {
		case GREEDY:
			acyclicEdges = cluster.getCluster().getAcyclicEdgeSet_Greedy();
			break;
		case ENHANCED:
			acyclicEdges = cluster.getCluster().getAcyclicEdgeSet_EnhancedGreedy();
		}
		for (Edge e : acyclicEdges) {
			e.reverseDirection();
		}
		cluster.draw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graphmodifiers.GraphModifier#getControls()
	 */
	public JPanel getControls() {
		return controls;
	}

}
