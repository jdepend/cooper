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

/**
 * A set of copies of constant variables pre-parsed from the global constants in
 * the belief that this may speed things up a bit
 */
public class Constants {
	private static org.wilmascope.global.GlobalConstants gc = org.wilmascope.global.GlobalConstants.getInstance();
	public final static float maxAcceleration = gc.getFloatValue("MaximumAcceleration");
	public final static float terminalVelocity = gc.getFloatValue("TerminalVelocity");
	public static float frictionCoefficient = gc.getFloatValue("FrictionCoefficient");
	public final static float angularInertia = gc.getFloatValue("AngularInertia");
	public final static Vector3f minVector = gc.getVector3f("MinVector");
	public final static Vector3f vZero = gc.vZero;
	public final static float balancedThreshold = gc.getFloatValue("BalancedThreshold");
	public final static float velocityAttenuation = gc.getFloatValue("VelocityAttenuation");
	public final static float defaultEdgeLength = gc.getFloatValue("DefaultEdgeLength");
}
