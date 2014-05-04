/*
 * Created on 14/11/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.wilmascope.graphanalysis.plugin;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

import org.wilmascope.control.GraphControl;
import org.wilmascope.global.RandomGenerator;
import org.wilmascope.graph.Edge;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeList;
import org.wilmascope.graphanalysis.GraphAnalysis;

/**
 * @author dwyer
 */
public class BiconnectedComponents extends GraphAnalysis {
	class DFSNode {
		DFSNode(Node n) {
			n.getProperties().setProperty(getName(), "" + 0.1);
			n.storeUserData("DFS", this);
		}

		Node parent;

		int children = 0;

		int dfsnum = 0;

		int low = 0;

		boolean visited = false;
	}

	public String getName() {
		return "Biconnected Components";
	}

	private DFSNode data(Node n) {
		return (DFSNode) n.getUserData("DFS");
	}

	/**
	 * @see org.wilmascope.graphanalysis.GraphAnalysis#analyse()
	 */
	public void analyse() {
		NodeList nodes = getCluster().getCluster().getNodes();
		for (Node n : nodes) {
			new DFSNode(n);
		}
		edgeStack = new Stack<Edge>();
		components = new ArrayList<ArrayList<Edge>>();
		index = 0;
		for (Node n : nodes) {
			DFSNode dn = data(n);
			if (dn.dfsnum == 0) {
				visit(n);
				if (dn.children >= 2) {
					n.getProperties().setProperty(getName(), "" + 0.5);
				} else {
					n.getProperties().setProperty(getName(), "" + 0.1);
				}
			}
		}
		for (ArrayList<Edge> component : components) {
			Color c = Color.getHSBColor(RandomGenerator.getRandomFloat(), 1, 1);
			for (Edge e : component) {
				((GraphControl.Edge) e.getUserData("Facade")).setColour(c);
			}
		}
	}

	int index;

	void visit(Node v) {
		DFSNode dv = data(v);
		dv.low = dv.dfsnum = ++index;
		for (Edge e : v.getEdges()) {
			Node w = e.getNeighbour(v);
			DFSNode dw = data(w);
			if (dv.dfsnum > dw.dfsnum) {
				if (!edgeStack.contains(e)) {
					edgeStack.push(e);
				}
			}
			if (dw.dfsnum == 0) {
				dv.children++;
				visit(w);
				if (dw.low >= dv.dfsnum) {
					// u is an articulation point
					v.getProperties().setProperty(getName(), "" + 0.5);
					ArrayList<Edge> biconnectedComponent = new ArrayList<Edge>();
					Edge f = null;
					do {
						f = edgeStack.pop();
						System.out.println("connected edge");
						biconnectedComponent.add(f);
					} while (f != e);
					components.add(biconnectedComponent);
				} else {
					dv.low = Math.min(dv.low, dw.low);
				}
			} else {
				dv.low = Math.min(dv.low, dw.dfsnum);
			}
		}
	}

	Stack<Edge> edgeStack;

	ArrayList<ArrayList<Edge>> components;
}
