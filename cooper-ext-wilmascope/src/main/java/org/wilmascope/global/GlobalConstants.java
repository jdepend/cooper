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

package org.wilmascope.global;

import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.vecmath.Vector3f;

/*
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular WilmaScope software
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaScope.org
 * @author Tim Dwyer
 * @version 1.0
 */

/**
 * A class which contains a set of concrete constants. It has some hard coded
 * properties. It will also attempt to read property information from a file
 */

public class GlobalConstants extends AbstractConstants {

	private static final ResourceBundle WILMA_CONSTANTS_PROPERTIES = PropertyResourceBundle
			.getBundle("WILMA_CONSTANTS");

	private static GlobalConstants instance = null;

	private GlobalConstants(Properties defaultProps, ResourceBundle bundle) {
		super(defaultProps, bundle);
	}

	public static GlobalConstants getInstance() {
		if (instance == null) {
			instance = new GlobalConstants(getDefaultProperties(), WILMA_CONSTANTS_PROPERTIES);
		}
		return instance;
	}

	protected static Properties getDefaultProperties() {
		Properties d = new Properties();

		// A very small vector, useful in calculations approximating zero.
		d.setProperty("MinVectorX", "0.01");
		d.setProperty("MinVectorY", "0.01");
		d.setProperty("MinVectorZ", "0.01");

		// The default length for generated random vectors
		d.setProperty("DefaultRandomVectorLength", "0.25");
		d.setProperty("DefaultEdgeLength", "0.25");
		// The Maximum acceleration for a node
		d.setProperty("MaximumAcceleration", "10");
		// The Maximum velocity of a node
		d.setProperty("TerminalVelocity", "0.25");
		// A scalar for the force opposite and proportional to the nodes
		// direction
		// of motion
		d.setProperty("FrictionCoefficient", "200");
		d.setProperty("AngularInertia", "10");
		d.setProperty("BalancedThreshold", "0.03");
		d.setProperty("VelocityAttenuation", "0.01");

		// default data path
		d.setProperty("DefaultDataPath", "../data");
		// default path to the DOT program
		d.setProperty("DotPath", "dot");
		return d;
	}

	// the following are fairly definitive, I don't think we need to load them
	// from a file
	//
	// A zero value vector. Useful in calculations and initializing new vectors
	public static final Vector3f vZero = new Vector3f(0f, 0f, 0f);

	// A vector along the x axis
	public static final Vector3f vX = new Vector3f(1f, 0f, 0f);

	// A vector along the y axis
	public static final Vector3f vY = new Vector3f(0f, 1f, 0f);

	// A vector along the z axis
	public static final Vector3f vZ = new Vector3f(0f, 0f, 1f);
}
