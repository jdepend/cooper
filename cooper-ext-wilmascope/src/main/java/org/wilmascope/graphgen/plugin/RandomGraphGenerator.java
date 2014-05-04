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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.wilmascope.control.GraphControl;
import org.wilmascope.global.RandomGenerator;
import org.wilmascope.graph.LayoutEngine;
import org.wilmascope.graphgen.GeneratorManager;
import org.wilmascope.graphgen.GraphGenerator;
import org.wilmascope.graphmodifiers.GraphModifier;
import org.wilmascope.graphmodifiers.ModifierManager;
import org.wilmascope.gui.SpinnerSlider;

/**
 * Generates a random (connected) graph with (up to) a specified number of nodes
 * and edges with random node positions in either 2 or 3 dimensions.
 * <p>
 * The algorithm is extremely simplistic:
 * <ul>
 * <li>create the specified number of nodes</li>
 * <li>for each node randomly pick another and create an edge between them</li>
 * <li>delete disconnected nodes</li>
 * </ul>
 * 
 * @author dwyer
 */
public class RandomGraphGenerator extends GraphGenerator {

	public String getName() {
		return "Random";
	}

	/** number of nodes that will be created initially in the random graph */
	int nodeCount = 30;

	/** number of edges that will be created in the random graph */
	int edgeCount = 60;

	/** generate with 3D positions? (or in 2D plane) */
	boolean threeDimensional = true;

	/** constrain to levels? */
	boolean stratified = false;

	/** number of levels */
	int levels = 3;

	/**
	 * Generate the random graph
	 * 
	 * @see org.wilmascope.graphgen.GraphModifier#generate(org.wilmascope.control.GraphControl)
	 */
	public void generate(GraphControl gc) {
		GraphControl.Cluster root = gc.getRootCluster();
		/*
		 * The default layout engine will be used unless an alternative is set
		 * up as follows:
		 */

		// LayoutEngine layout = new FastLayout(cluster, threeD);
		// root.setLayoutEngine(layout);
		// clean up before we start
		root.deleteAll();

		if (stratified) {
			LayoutEngine l = root.getLayoutEngine();
			l.getProperties().setProperty("Levels", "" + levels);
			l.resetProperties();
		}
		GraphControl.Node[] nodes = new GraphControl.Node[nodeCount];

		for (int i = 0; i < nodeCount; i++) {
			nodes[i] = addRandomNode(root, threeDimensional);
			if (stratified) {
				nodes[i].setProperty("LevelConstraint", "" + RandomGenerator.getRandom().nextInt(levels));
			}
		}
		for (int i = 1; i < edgeCount; i++) {
			GraphControl.Node a = nodes[RandomGenerator.getRandom().nextInt(nodeCount)];
			GraphControl.Node b = nodes[RandomGenerator.getRandom().nextInt(nodeCount)];
			if (a != b) {
				root.addEdge(a, b, getEdgeView());
			} else {
				i--;
			}
		}
		// for each node a randomly pick another b and create an edge from a to
		// b
		for (GraphControl.Node a : nodes) {
			GraphControl.Node b;
			do {
				b = nodes[RandomGenerator.getRandom().nextInt(nodeCount)];
			} while (a == b);
			root.addEdge(a, b, getEdgeView());
		}
		/*
		 * if we don't unfreeze the layoutengine then we must invoke the
		 * following method to draw the graph with the random positions we have
		 * assigned
		 */
		root.draw();
		/*
		 * trigger layout... if we do this then we don't need the above call to
		 * draw
		 */
		// root.unfreeze();
		if (layeredDAG) {
			createLayeredDirectedAcyclicGraph(gc);
		}
	}

	public void createLayeredDirectedAcyclicGraph(GraphControl graphControl) {
		try {
			GraphModifier gm = ModifierManager.getInstance().getPlugin("Longest Path Layering");
			gm.modify(graphControl.getRootCluster());
			GeneratorManager.getInstance().getPlugin("Insert Dummy Nodes").generate(graphControl);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	JPanel controlPanel = new JPanel();

	void enableStratifiedControls() {
		for (Component c : enabledGroup) {
			c.setEnabled(stratified);
		}
	}

	/**
	 * set up controlPanel with controls for setting parameters
	 */
	public RandomGraphGenerator() {
		final SpinnerSlider nodesSlider = new SpinnerSlider("Number of nodes", 10, 2000, nodeCount);
		final SpinnerSlider edgesSlider = new SpinnerSlider("Number of edges", 10, 2000, edgeCount);
		nodesSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				nodeCount = nodesSlider.getValue();
			}
		});
		edgesSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				edgeCount = edgesSlider.getValue();
			}
		});
		final JCheckBox threeDimButton = new JCheckBox("3D Positions");
		threeDimButton.setSelected(threeDimensional);
		threeDimButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				threeDimensional = threeDimButton.isSelected();
			}
		});
		final JCheckBox stratifiedCB = new JCheckBox("Levels");
		stratifiedCB.setSelected(stratified);
		final JRadioButton randomLevelsButton = new JRadioButton("Random Levels");
		enabledGroup.add(randomLevelsButton);
		JRadioButton layeredDAGButton = new JRadioButton("Layered DAG");
		enabledGroup.add(layeredDAGButton);
		layeredDAGButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layeredDAG = true;
			}
		});
		SpinnerNumberModel sm = new SpinnerNumberModel(levels, 0, 20, 1);
		final JSpinner levelsSpinner = new JSpinner(sm);
		enabledGroup.add(levelsSpinner);
		enableStratifiedControls();
		stratifiedCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stratified = stratifiedCB.isSelected();
				enableStratifiedControls();
				randomLevelsButton.setSelected(true);
			}

		});
		levelsSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				levels = ((Integer) levelsSpinner.getValue()).intValue();
			}
		});
		Box sliders = Box.createVerticalBox();
		Box options = Box.createVerticalBox();
		ButtonGroup bg = new ButtonGroup();
		bg.add(randomLevelsButton);
		bg.add(layeredDAGButton);
		sliders.add(nodesSlider);
		sliders.add(edgesSlider);
		options.add(threeDimButton);
		options.add(stratifiedCB);
		options.add(randomLevelsButton);
		options.add(layeredDAGButton);
		options.add(levelsSpinner);
		controlPanel.add(sliders);
		controlPanel.add(options);
	}

	Vector<Component> enabledGroup = new Vector<Component>();

	public JPanel getControls() {
		return controlPanel;
	}

	boolean layeredDAG = false;
}
