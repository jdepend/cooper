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

package org.wilmascope.multiscalelayout;

import java.awt.Dimension;
import java.util.Stack;
import java.util.Vector;

import javax.vecmath.Vector3f;

/**
 * Constructs a Multi-Scale heirarchy of graphs {@link MultiScaleGraph}
 * <p>
 * $Id: QuickGraph.java,v 1.3 2004/04/23 08:35:20 tgdwyer Exp $
 * </p>
 * <p>
 * @author Tim Dwyer
 * </p>
 * <p>
 * @version $Revision: 1.3 $
 * </p>
 */
public class QuickGraph {
	Vector nodes = new Vector();
	Vector edges = new Vector();

	/**
	 * The hierarchy of MultiScaleGraphs
	 */
	Stack graphs = new Stack();
	/**
	 * The graph currently being arranged
	 */
	MultiScaleGraph graph;
	/**
	 * The ratio between spring constants of adjacent levels
	 */
	static float kscale = (float) Math.sqrt(3.0 / 7.0);

	Dimension dim = new Dimension(100, 100);
	private int iterations = 0;
	/**
	 * Number of iterations of spring algorithm performed per call to relax
	 */
	static int iterationsPerFrame = 1;

	MultiScaleGraph getGraph() {
		return graph;
	}

	public MultiScaleNodeLayout createMultiScaleNodeLayout(org.wilmascope.graph.Node gn) {
		MultiScaleNodeLayout n = new MultiScaleNodeLayout();
		n.position.set(10f + 380f * (float) Math.random(), 10f + 380f * (float) Math.random(), 0);
		nodes.add(n);
		return n;
	}

	public MultiScaleEdgeLayout createMultiScaleEdgeLayout(org.wilmascope.graph.Edge ge) {
		MultiScaleEdgeLayout e = new MultiScaleEdgeLayout((MultiScaleNodeLayout) ge.getStart().getLayout(),
				(MultiScaleNodeLayout) ge.getEnd().getLayout());
		edges.add(e);
		return e;
	}

	long startTime;

	void setNotConverged() {
		int lastNodeCount = Integer.MAX_VALUE;
		iterations = 0;
		MultiScaleNodeLayout[] nodeArray = new MultiScaleNodeLayout[nodes.size()];
		nodes.toArray(nodeArray);
		MultiScaleEdgeLayout[] edgeArray = new MultiScaleEdgeLayout[edges.size()];
		edges.toArray(edgeArray);
		graph = new MultiScaleGraph(nodeArray, edgeArray, 0);
		graphs.push(graph);
		startTime = System.currentTimeMillis();
		while (graph.nodes.length > 2 && graph.nodes.length < lastNodeCount) {
			lastNodeCount = graph.nodes.length;
			graph = graph.coursenedGraph();
			graphs.push(graph);
		}
		graph = (MultiScaleGraph) graphs.pop();
		System.out.println("initial graph size = " + graph.nodes.length);
		float ypos = (float) dim.height / 2.0f;
		float xpos = (float) dim.width / 5f;
		graph.nodes[0].position.set(xpos, ypos, 0);
		graph.nodes[1].position.set((float) (dim.width) - xpos, ypos, 0);
		Vector3f v = new Vector3f();
		v.sub(graph.nodes[0].position, graph.nodes[1].position);
		graph.resetTemperature(v.length());
	}

	boolean relax() {
		boolean c = false;
		boolean done = false;
		for (int i = 0; i < iterationsPerFrame; i++) {
			c = graph.layout(dim);
			iterations++;
			if (c)
				break;
		}
		// propagate newly calculated positions for the top-most graph back
		// down the stack (multiscale hierarchy)
		for (int i = graphs.size() - 1; i >= 0; i--) {
			MultiScaleGraph g = (MultiScaleGraph) graphs.get(i);
			g.setParentPositions();
		}
		if (c) {
			System.out.println("converged!: after " + iterations);
			float level = graph.level;
			if (graph.level == 0) {
				System.out.println("Finished in: " + iterations + " iterations");
				System.out.println("  " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
				// System.out.println("Total edge force: "+graph.getTotalLocalForce());
				done = true;
			} else {
				float k = graph.k;
				// we're now done with the top graph, move down the stack
				graph = (MultiScaleGraph) graphs.pop();
				graph.deleteParents();
				graph.resetTemperature(k * kscale);
			}
		}
		return done;
	}
}
