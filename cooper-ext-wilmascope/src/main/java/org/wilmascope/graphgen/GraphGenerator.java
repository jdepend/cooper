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
package org.wilmascope.graphgen;

import org.wilmascope.control.GraphControl;
import org.wilmascope.global.RandomGenerator;
import org.wilmascope.util.Plugin;

/**
 * Boilerplate class for graph generators. Extend this class to create classes
 * that generate random or example graphs.
 * 
 * @author dwyer
 */
public abstract class GraphGenerator implements Plugin {
	/**
	 * This method will do the work of actually creating the graph.
	 * 
	 * @param gc
	 *            the instance of GraphControl inwhich the graph will be
	 *            generated
	 */
	public abstract void generate(GraphControl gc);

	/**
	 * Set the glyph style for the generated graph
	 * 
	 * @param nodeView
	 *            node view style
	 * @param edgeView
	 *            edge view style
	 */
	public void setView(String nodeView, String edgeView) {
		this.nodeView = nodeView;
		this.edgeView = edgeView;
	}

	/**
	 * Create a randomly coloured, randomly positioned node with the default
	 * style
	 * 
	 * @param root
	 *            cluster to add the node to
	 * @param threeD
	 *            use 3D positions
	 * @return the new node
	 */
	protected GraphControl.Node addRandomNode(GraphControl.Cluster root, boolean threeD) {
		GraphControl.Node n = root.addNode(getNodeView());
		n.setColour(RandomGenerator.randomColour());
		n.setPosition(RandomGenerator.randomPoint(threeD));
		return n;
	}

	/**
	 * Get the NodeView style
	 * 
	 * @return
	 */
	protected String getNodeView() {
		return nodeView;
	}

	protected String getEdgeView() {
		return edgeView;
	}

	private String nodeView, edgeView;
}
