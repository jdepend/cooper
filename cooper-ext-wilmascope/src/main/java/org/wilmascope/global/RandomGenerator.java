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

/*
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular Wilma graph drawing engine
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaOrg
 * @author Tim Dwyer
 * @version 1.0
 */
import java.awt.Color;
import java.util.Random;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Random vector generator
 */
public class RandomGenerator {
	private static Random random = new Random();

	public static Random getRandom() {
		return random;
	}

	// returns a random float in the range -0.5 to 0.5
	public static float getRandomFloat() {
		return (random.nextFloat() - 0.5f);
	}

	/**
	 * @return A random Vector3f scaled by Constant: DefaultRandomVectorLength
	 */
	public static Vector3f getVector3f() {
		// System.out.println("Warning: no length for RandomVector.getVector()");
		return getVector3f(constants.getFloatValue("DefaultRandomVectorLength"));
	}

	public static Point3f getPoint3f() {
		Point3f vec = new Point3f(getRandomFloat(), getRandomFloat(), getRandomFloat());
		return vec;
	}

	/**
	 * @return a random unit vector scaled by length
	 */
	public static Vector3f getVector3f(float length) {
		Vector3f vec = new Vector3f(getRandomFloat(), getRandomFloat(), getRandomFloat());
		vec.scale(length / vec.length());
		return vec;
	}

	/**
	 * @param threeD
	 *            if this is false then z values are all 0
	 * @return a random point with x,y,z values between 0 and 1
	 */
	public static Point3f randomPoint(boolean threeD) {
		return new Point3f((random.nextFloat() - 0.5f) * 5f, (random.nextFloat() - 0.5f) * 5f,
				threeD ? (random.nextFloat() - 0.5f) * 5f : 0f);
	}

	/**
	 * @return a random colour
	 */
	public static Color randomColour() {
		return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
	}

	// The constants
	private static org.wilmascope.global.GlobalConstants constants = org.wilmascope.global.GlobalConstants
			.getInstance();
}
