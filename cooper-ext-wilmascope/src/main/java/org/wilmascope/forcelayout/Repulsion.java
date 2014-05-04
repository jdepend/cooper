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
 * A repulsive (electrostatic style) force between all nodes.
 */
public class Repulsion extends Force {
	public Repulsion(float strengthConstant, float limitRadius) {
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
		for (int i = 0; i < nodes.size(); i++) {
			node1 = nodes.get(i);
			float node1Mass = node1.getMass();
			repulsion.set(Constants.vZero);
			for (int j = 0; j < nodes.size(); j++) {
				if (j == i)
					continue;
				node2 = nodes.get(j);
				v.sub(node1.getPosition(), node2.getPosition());
				separation = v.length();
				/*
				 * - node1.getRadius() - node2.getRadius(); // clipping
				 * calculated force at a certain separation threshold // allows
				 * for negative separation (when nodes are overlapping) and //
				 * also prevents the force becoming chaotically violent when the
				 * // separation is too low. if(separation <
				 * separationThreshold) { separation = separationThreshold; }
				 */
				// if two nodes are directly on top of one another (have zero
				// separation) we get a nasty divide by zero error
				if (separation != 0) {
					if (separation < limitRadiusSquared) {
						float mass1 = node1Mass;
						float mass2 = node2.getMass();
						v.scale(strengthConstant * mass1 * mass2 / (separation * separation));
						repulsion.add(v);
					}
				} else /* if(separation <= separationThreshold) */{
					// make sure there's some distance (in a random direction to
					// avoid
					// nodes settling into lines or planes) between these two
					// nodes next time
					v.set(org.wilmascope.global.RandomGenerator.getVector3f());
					repulsion.add(v);
				}
			}
			((NodeForceLayout) node1.getLayout()).addForce(repulsion);
		}
	}

	// some workhorse temporary objects
	private Vector3f repulsion = new Vector3f(), v = new Vector3f();
	private float separation;
	private static float separationThreshold = 0.001f;
	// keeping this saves us squaring limitRadius every time
	private float limitRadiusSquared;

	private NodeList nodes;
}
