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

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeLayout;

/*
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular WilmaScope software
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaScope.org
 * @author Tim Dwyer
 * @version 1.0
 */

/**
 * This class encapsulates the physical properties of a node in relation to the
 * {@link ForceLayout} such as mass, and the forces acting upon it.
 */

public class NodeForceLayout extends NodeLayout {
	public void resetProperties() {
		super.resetProperties();
		String str = getNode().getProperties().getProperty("LevelConstraint");
		if (str != null) {
			levelConstraint = Integer.parseInt(str);
		}
		str = getNode().getProperties().getProperty("OrbitConstraint");
		if (str != null) {
			orbitConstraint = Integer.parseInt(str);
		}
	}

	/** Add a force vector which will act on this NodeForceLayout */
	public void addForce(Vector3f force) {
		netForce.add(force);
	}

	/**
	 * 'Subtract' a force vector
	 */
	public void subForce(Vector3f force) {
		netForce.sub(force);
	}

	/** Get the aggregate (or net) force acting on this NodeForceLayout */
	public Vector3f getNetForce() {
		if (levelConstraint >= 0) {
			netForce.z = 0;
		}
		return netForce;
	}

	/**
	 * Adjust the node's position by calculating an acceleration due to the
	 * forces on the node, then applying that acceleration to the velocity of
	 * the node and then scale that velocity by the attenuation factor. Also
	 * applies a 'friction' force. This causes the graph to settle at a stable
	 * position. The friction force is directly proportional to the velocity and
	 * the radius of the node, thus large nodes (especially expanded clusters)
	 * will have greater "air resistance?" and will thus not vibrate as easily
	 * as small nodes.
	 * 
	 * @param attenuation
	 *            scale factor for the velocity
	 */
	public void applyForce(float attenuation) {
		Node node = getNode();
		if (isFixedPosition()) {
			return;
		}
		// Calculate friction based on current velocity
		Vector3f friction = new Vector3f(velocity);
		friction.scale(Constants.frictionCoefficient /*
													 *  *
													 * node.getRadius()/node.getMass
													 * ()
													 */);
		netForce.sub(friction);
		// Acceleration (change in velocity) = F/m
		float sf = attenuation / node.getMass();
		acceleration.scale(sf, netForce);
		netForce.set(Constants.vZero);
		// Clip acceleration to its maximum... prevents bizarre behaviour
		clip(acceleration, Constants.maxAcceleration);
		velocity.add(acceleration);

		// Clip velocity at terminal velocity
		clip(velocity, Constants.terminalVelocity);

		// Move the node
		node.reposition(velocity);
		ForceLayout fl = (ForceLayout) node.getOwner().getLayoutEngine();
		// apply level constraint
		int totalLevels = fl.getLevels();
		float levelSeparation = fl.getLevelSeparation();
		if (totalLevels >= 0) {
			Point3f p = node.getPosition();
			p.z = levelSeparation * ((float) levelConstraint - (float) totalLevels / 2f);
		}
		// apply orbit constraint
		int totalOrbits = fl.getOrbits();
		float orbitSeparation = fl.getOrbitSeparation();
		if (totalOrbits >= 0) {
			Point3f o = node.getOwner().getPosition();
			Point3f p = node.getPosition();
			Vector3f po = new Vector3f();
			po.sub(o, p);
			Vector3f rpo = new Vector3f(po);
			rpo.normalize();
			float r = orbitSeparation * (float) orbitConstraint;
			rpo.scale(r);
			Vector3f pq = new Vector3f();
			pq.sub(po, rpo);
			node.reposition(pq);
		}
	}

	/*
	 * If a {@link vec} is longer than {@link maxLength} then normalise it.
	 * 
	 * @param vec input vector
	 * 
	 * @param maxLength length beyond which {@link vec} is clipped
	 * 
	 * @return True if normalisation occurs
	 */
	private static boolean clip(Vector3f vec, float maxLength) {
		if (vec.length() > maxLength) {
			vec.scale(maxLength / vec.length());
			return true;
		}
		return false;
	}

	// The sum of all force vectors on the node
	Vector3f netForce = new Vector3f();
	// The velocity with which the node was travelling after the last time the
	// forces were applied.
	Vector3f velocity = new Vector3f();
	// Acceleration of the node due to the forces on the node
	Vector3f acceleration = new Vector3f();
	int levelConstraint = -1;
	int orbitConstraint = -1;
}
