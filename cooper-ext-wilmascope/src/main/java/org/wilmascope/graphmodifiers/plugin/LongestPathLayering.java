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

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.JPanel;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Edge;
import org.wilmascope.graph.Node;
import org.wilmascope.graphmodifiers.GraphModifier;
import org.wilmascope.graphmodifiers.ModifierManager;
import org.wilmascope.util.UnknownTypeException;

/**
 * Assigns nodes to levels according to Nikola Nikolov's longest-path layering
 * algorithm.
 * 
 * @author dwyer
 */
public class LongestPathLayering extends GraphModifier {

	Cluster cluster;
	JPanel controls = new JPanel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.util.Plugin#getName()
	 */
	public String getName() {
		return "Longest Path Layering";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.wilmascope.graphmodifiers.GraphModifier#modify(org.wilmascope.graph
	 * .Cluster)
	 */
	public void modify(GraphControl.Cluster clusterFacade) {
		this.cluster = clusterFacade.getCluster();
		try {
			GraphModifier cycleRemoval = ModifierManager.getInstance().getPlugin("Directed Cycle Removal");
			cycleRemoval.modify(clusterFacade);
		} catch (UnknownTypeException e) {
			WilmaMain.showErrorDialog("Directed Cycle Removal plugin not available!", e);
			return;
		}
		longestPathLayering();
		promoteLayering(); // optional improvement heuristic
		for (Edge e : cluster.getInternalEdges()) {
			int uLayer = getLayer(e.getStart());
			int vLayer = getLayer(e.getEnd());
			assert (uLayer > vLayer);
			if (uLayer <= vLayer) {
				String m = "Layering Didn't Work!!!";
				WilmaMain.showErrorDialog(m, new Exception(m));
			}
		}
		int maxLayer = 0;
		int minLayer = Integer.MAX_VALUE;
		for (Node v : cluster.getNodes()) {
			int l = getLayer(v);
			if (l > maxLayer) {
				maxLayer = l;
			}
			if (l < minLayer) {
				minLayer = l;
			}
		}
		for (Node v : cluster.getNodes()) {
			int l = getLayer(v);
			v.getProperties().setProperty("LevelConstraint", "" + (l - minLayer));
			v.getLayout().resetProperties();
		}
		cluster.getLayoutEngine().getProperties().setProperty("Levels", "" + (maxLayer - minLayer + 1));
		cluster.getLayoutEngine().resetProperties();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.util.Plugin#getControls()
	 */
	public JPanel getControls() {
		return controls;
	}

	// //////////////////////////////////////// //
	// the longest-path algorithm main function
	void longestPathLayering() {
		layering = new Hashtable<Node, Integer>();
		int max_value, max, compare;
		Queue<Node> Q = new LinkedList<Node>();
		max_value = 2 * cluster.getNodes().size();
		for (Node v : cluster.getNodes()) {
			// assign sinks to the bottom layer
			if (v.getOutDegree() == 0) {
				assignLayer(v, 0);
				Q.add(v);
			} else
				assignLayer(v, max_value);
		}
		Node v, w;
		while (Q.size() > 0) {
			v = Q.remove();
			for (Edge e : v.getInEdges()) {
				w = e.getStart();
				if (getLayer(w) == max_value) {
					max = 0;
					for (Edge f : w.getOutEdges()) {
						compare = getLayer(f.getEnd());
						if (compare > max)
							max = compare;
					}
					if (max < max_value) {
						assignLayer(w, max + 1);
						Q.add(w);
					}
				} // //// if the vertex is interesting
			}
		} // end while
	}

	// The following two functions are for postprocessing
	// to reduce the number of dummy nodes

	void promoteLayering() {
		int significant_promotions;
		do {
			significant_promotions = 0;
			for (Node v : cluster.getNodes()) {
				Map<Node, Integer> layering_save = new Hashtable(layering);
				if (v.getInDegree() > 0) {
					int promote_gain = promoteNode(v);
					if (promote_gain < 0) {
						significant_promotions++;
					} else {
						layering = layering_save;
					}
				}
			}
		} while (significant_promotions > 0);
	}

	// pushes a node and all its predecessors on the layer above, up a layer
	// returns the outdegree of v and all it predecessors moved
	int promoteNode(Node v) {
		assignLayer(v, getLayer(v) + 1);
		int c = -v.getInDegree();
		for (Edge e : v.getInEdges()) {
			Node u = e.getStart();
			if (getLayer(u) == getLayer(v)) {
				c += promoteNode(u);
			}
		}
		c += v.getOutDegree();
		return c;
	}

	void assignLayer(Node n, int l) {
		layering.put(n, new Integer(l));
	}

	int getLayer(Node n) {
		return layering.get(n).intValue();
	}

	Map<Node, Integer> layering;
}
