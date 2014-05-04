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
package org.wilmascope.randomlayout;

import javax.swing.JPanel;

import org.wilmascope.global.RandomGenerator;
import org.wilmascope.graph.Edge;
import org.wilmascope.graph.EdgeLayout;
import org.wilmascope.graph.LayoutEngine;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeLayout;

/**
 * Assigns random positions to nodes. This is the simplest possible layout
 * algorithm I can think of. Use this as a starting point for creating your own
 * layout engines.
 * 
 * @author dwyer
 */
public class RandomLayout extends LayoutEngine<RandomNode, RandomEdge> {

	/*
	 * This method always returns true because this layout can be completed in
	 * one step. An iterative algorithm could return false until complete.
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#applyLayout()
	 */
	public boolean applyLayout() {
		for (Node n : getRoot().getNodes()) {
			n.setPosition(RandomGenerator.getPoint3f());
		}
		return true;
	}

	/*
	 * Name to appear in the layout menu.
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#getName()
	 */
	public String getName() {
		return "Random";
	}

	/*
	 * called by super.init when layout instantiated
	 * 
	 * @see
	 * org.wilmascope.graph.LayoutEngine#createNodeLayout(org.wilmascope.graph
	 * .Node)
	 */
	public RandomNode createNodeLayout(Node n) {
		return new RandomNode();
	}

	/*
	 * called by super.init when layout instantiated
	 * 
	 * @see
	 * org.wilmascope.graph.LayoutEngine#createEdgeLayout(org.wilmascope.graph
	 * .Edge)
	 */
	public RandomEdge createEdgeLayout(Edge e) {
		return new RandomEdge();
	}

	/*
	 * Potentially we could create a control panel in the constructor to be
	 * returned here. Such a control panel would allow the user to set various
	 * layout properties.
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#getControls()
	 */
	public JPanel getControls() {
		return new JPanel();
	}

}

/**
 * When the layout engine is instatiated, instances of this classes are assigned
 * to all nodes in the root cluster. You can store node data specific to your
 * layout in here.
 */

class RandomNode extends NodeLayout {
}

/**
 * When the layout engine is instatiated, instances of the following class are
 * assigned to all edges in the root cluster. You can store edge data specific
 * to your layout here.
 */

class RandomEdge extends EdgeLayout {
}
