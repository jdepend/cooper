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

import java.awt.Color;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.wilmascope.control.GraphControl;
import org.wilmascope.global.RandomGenerator;
import org.wilmascope.graph.Cluster;
import org.wilmascope.graphgen.GraphGenerator;
import org.wilmascope.gui.SpinnerSlider;

/**
 * Generate a scale-free graph based on a stochastic model in which for each
 * node <i>u </i> that is added, all existing nodes <i>v <sub>i </sub> </i> are
 * considered and an edge ( <i>u </i>, <i>v <sub>i </sub> </i>) is created with
 * probability (1+deg( <i>u </i>))/sum(deg( <i>v <sub>j </sub> </i>))
 * 
 * @author dwyer
 */
public class ScaleFreeGraphGenerator extends GraphGenerator implements Runnable {

	int initSize = 10;

	int targetSize = 100;

	float probabilityModifier = 1.0f;

	int delay = 300;

	GraphControl.Cluster root;

	JPanel controlPanel = new JPanel();

	public String getName() {
		return "Scale Free";
	}

	public ScaleFreeGraphGenerator() {
		final SpinnerSlider initSizeSlider = new SpinnerSlider("Initial nodes", 5, 200, initSize);
		final SpinnerSlider targetSizeSlider = new SpinnerSlider("Target Nodes", 1, 1000, targetSize);
		final SpinnerSlider probabilitySlider = new SpinnerSlider("Edge Probability", 0, 10,
				(int) probabilityModifier * 5);
		final SpinnerSlider delaySlider = new SpinnerSlider("Delay (ms)", 0, 1000, delay);
		initSizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				initSize = initSizeSlider.getValue();
			}
		});
		targetSizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				targetSize = targetSizeSlider.getValue();
			}
		});
		probabilitySlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				probabilityModifier = 0.5f + 0.1f * (float) probabilitySlider.getValue();
			}
		});
		delaySlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				delay = delaySlider.getValue();
			}
		});
		Box box = Box.createVerticalBox();
		box.add(initSizeSlider);
		box.add(targetSizeSlider);
		box.add(probabilitySlider);
		box.add(delaySlider);
		controlPanel.add(box);
	}

	/**
	 * Generate a scale-free graph based on a stockastic model in which for each
	 * node <i>u</i> that is added, all existing nodes <i>v<sub>i</sub></i> are
	 * considered and an edge (<i>u</i>,<i>v<sub>i</sub></i>) is created with
	 * probability (1+deg(<i>v<sub>i</sub></i>))/sum(deg(<i>v<sub>j</sub></i>))
	 * 
	 * @param initSize
	 *            number of starting nodes
	 * @param targetSize
	 *            stop after this many nodes
	 */
	public void generate(GraphControl gc) {
		root = gc.getRootCluster();
		Cluster cluster = root.getCluster();
		// LayoutEngine layout = new FastLayout(cluster, threeD);
		// root.setLayoutEngine(layout);

		root.deleteAll();
		for (int i = 0; i < initSize; i++) {
			GraphControl.Node v = root.addNode();
			setColour(v);
		}
		new Thread(this).start();
	}

	private void addEdge(GraphControl.Cluster r, GraphControl.Node a, GraphControl.Node b) {
		r.addEdge(a, b, getEdgeView());
		setColour(a);
		setColour(b);
	}

	private void setColour(GraphControl.Node n) {
		float d = (float) n.getDegree();
		float h = 2 * d / 360f;
		float sb = 0.5f + d / 20f;
		n.setColour(Color.getHSBColor(h, sb, sb));
	}

	class ProbableNode implements Comparable<ProbableNode> {
		GraphControl.Node node;

		float probability;

		public ProbableNode(float p, GraphControl.Node n) {
			this.probability = p;
			this.node = n;
		}

		public int compareTo(ProbableNode arg) {
			if (probability < arg.probability)
				return -1;
			else if (probability > arg.probability)
				return 1;
			else
				return 0;
		}

		public String toString() {
			return "" + probability;
		}
	}

	public JPanel getControls() {
		return controlPanel;
	}

	public void run() {
		for (int i = initSize; i < targetSize; i++) {
			try {
				Thread.sleep(delay);
				root.unfreeze();
			} catch (InterruptedException e) {
				// Thread interrupted
			}
			GraphControl.Node[] l = root.getNodes();
			GraphControl.Node u = root.addNode();
			setColour(u);
			int totalDegree = 1;
			for (GraphControl.Node n : l) {
				totalDegree += n.getDegree();
			}
			ProbableNode[] nodes = new ProbableNode[l.length];
			for (int j = 0; j < l.length; j++) {
				float probability = (float) (1 + l[j].getDegree()) / (float) totalDegree;
				nodes[j] = new ProbableNode(probability, l[j]);
			}
			// Arrays.sort(nodes);
			for (ProbableNode n : nodes) {
				if (RandomGenerator.getRandom().nextFloat() < probabilityModifier * n.probability)
					addEdge(root, u, n.node);
			}
		}
	}
}
