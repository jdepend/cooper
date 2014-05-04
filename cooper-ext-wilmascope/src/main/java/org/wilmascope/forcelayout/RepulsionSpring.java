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
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeList;

/*
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular WilmaScope software
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaScope.org
 * @author Tim Dwyer
 * @version 1.0
 */

/**
 * A repulsive (electrostatic style) force between all nodes. Note, this version
 * of repulsion is compatible with RepulsionlessSpring.
 */
public class RepulsionSpring extends Force {
	public RepulsionSpring(float strengthConstant, float limitRadius) {
		super(strengthConstant, "Repulsion");
		this.limitRadiusSquared = limitRadius * limitRadius;
	}

	public void setCluster(Cluster root) {
		nodes = root.getNodes();
	}

	/**
	 * Calculate deltas for the repulsive (electrostatic) forces between all
	 * nodes
	 * 
	 * @param nodes
	 *            The nodes to which this force applies
	 * @param edges
	 *            Not used in this method (here to match the interface)
	 */
	public void calculate() {
		Node node1, node2;
		NodeForceLayout nodeLayout1;
		for (int i = 0; i < nodes.size(); i++) {
			node1 = nodes.get(i);
			nodeLayout1 = (NodeForceLayout) node1.getLayout();
			Vector3f repulsion = new Vector3f();
			for (int j = 0; j < nodes.size(); j++) {
				if (j == i)
					continue;
				node2 = nodes.get(j);
				Vector3f v = new Vector3f(node1.getPosition());
				v.sub(node2.getPosition());
				repulsion.add(calculate(nodeLayout1, (NodeForceLayout) node2.getLayout(), v));
			}
			nodeLayout1.addForce(repulsion);
		}
	}

	public Vector3f calculate(NodeForceLayout nl1, NodeForceLayout nl2, Vector3f vector) {
		Vector3f v = new Vector3f(vector);
		float separation = v.length();
		// if two nodes are directly on top of one another (have zero
		// separation) we get a nasty divide by zero error
		if (separation != 0) {
			if (separation < limitRadiusSquared) {
				float mass1 = 1;// nl1.getMass();
				float mass2 = 1;// nl2.getMass();
				v.scale(strengthConstant * mass1 * mass2 / (separation * separation));
			}
		} else {
			// make sure there's some distance between these two nodes next time
			v.set(org.wilmascope.global.RandomGenerator.getVector3f());
		}
		return v;
	}

	// keeping this saves us squaring limitRadius every time
	private float limitRadiusSquared;

	private NodeList nodes;
}
