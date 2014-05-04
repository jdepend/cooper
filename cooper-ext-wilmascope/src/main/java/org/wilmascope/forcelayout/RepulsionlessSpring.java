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
package org.wilmascope.forcelayout;

import javax.vecmath.Vector3f;

import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Edge;
import org.wilmascope.graph.EdgeList;

/*
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular WilmaScope software
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaScope.org
 * @author Tim Dwyer
 * @version 1.0
 */

/**
 * A {@link Force} for use by nodes connected by an edge. Attempts to restore
 * the distance between nodes to the natural length of that edge.
 */

public class RepulsionlessSpring extends Force {

	/**
	 * Creates a new {@link Spring} with the given strength
	 * 
	 * @param strengthConstant
	 *            k in <a href="www.google.com">Hooke's Law</a>
	 */
	public RepulsionlessSpring(float strengthConstant) {
		super(strengthConstant, "Spring");
	}

	public void setCluster(Cluster root) {
		edges = root.getInternalEdges();
	}

	/**
	 * set the repulsion force object to use if we are to ignore the effects of
	 * repulsion accross edges connected by springs... this can be done since
	 * the spring between edges can arguably be said to already separate the
	 * nodes enough. Note that we actually have to recalculate the repulsion
	 * accross the nodes so there is some minor cost to setting repulsion not
	 * null.
	 */
	public void setRepulsionForce(RepulsionSpring repulsion) {
		this.repulsion = repulsion;
	}

	/**
	 * Calculate node deltas due to spring forces between all nodes connected by
	 * an edge
	 */
	public void calculate() {
		for (int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			EdgeForceLayout edgeLayout = (EdgeForceLayout) edge.getLayout();
			edgeLength = edge.getLength();
			if (edgeLength != 0) {
				springForce = (edgeLength - edgeLayout.getRelaxedLength()) * strengthConstant / edgeLength;
				v.set(edge.getVector());
				v.scale(springForce);
			} else {
				v.set(Constants.minVector);
			}
			NodeForceLayout startLayout = (NodeForceLayout) edge.getStart().getLayout();
			NodeForceLayout endLayout = (NodeForceLayout) edge.getEnd().getLayout();
			startLayout.addForce(v);
			endLayout.subForce(v);
			if (repulsion != null) {
				Vector3f r = repulsion.calculate(startLayout, endLayout, edge.getVector());
				startLayout.addForce(v);
				endLayout.subForce(v);
			}
		}
	}

	// workhorse temporary variables - under the premise that a v.set(x) might
	// be
	// somewhat quicker than a v = new Vector3f(x), and we may as well do the
	// same
	// for the floats... I may be mistaken and this may be just uglifying my
	// code
	// if so someone please tell me
	private Vector3f v = new Vector3f();
	private float springForce;
	private float edgeLength;

	private RepulsionSpring repulsion;

	private EdgeList edges;
}
