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

/** A class for holding constants.  Extend this class and implement
 * the getDefaultProperties method to load default properties from a
 * file or hard code them (using the Properties.setProperty method).
 *
 * Default properties can be over-ridden by passing a fileName for a
 * valid properties file to the constructor.
 *
 * Note, implementations of this class are also a handy place to put
 * more permanent constants declared public static final.
 *
 * @author  $Author: tgdwyer $
 * @version $Revision: 1.4 $
 */
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

public abstract class AbstractConstants extends Properties {

	public AbstractConstants(Properties defaultProperties) {
		super(defaultProperties);
	}

	public AbstractConstants(Properties defaultProperties, ResourceBundle bundle) {
		super(defaultProperties);
		for (Enumeration e = bundle.getKeys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String defaultValue = defaultProperties.getProperty(key);
			String overrideValue = bundle.getString(key);
			if (defaultValue == null || !defaultValue.equals(overrideValue)) {
				System.out.println("value for " + key + " was " + defaultValue + " overriden by " + overrideValue);
			}
			super.setProperty(key, overrideValue);
		}
	}

	/**
	 * Gets the int value for the specified constant. If the specified constant
	 * does not exist or is not in a valid integer number format in properties
	 * then it attempts to get the value from the defaults.
	 * 
	 * @param constantName
	 * @return int value of the constant
	 */
	public int getIntValue(String constantName) {
		int value = 0;
		String property;
		property = getProperty(constantName);
		if (property == null) {
			throw new Error("Unknown Constant '" + constantName + "'!!");
		}
		try {
			value = Integer.parseInt(property);
		} catch (NumberFormatException e) {
			System.err.println("Number Format Error: " + e.getMessage());
			System.err.println("in '" + constantName + "':");
			System.err.println("  '" + property + "' is not a valid integer");
			System.err.println("  using default value instead");
			property = defaults.getProperty(constantName);
			if (property == null) {
				throw new Error("No Default Value for '" + constantName + "'!!");
			}
			try {
				value = Integer.parseInt(property);
			} catch (NumberFormatException f) {
				throw new Error("Number Format Error in default value for '" + constantName + "': " + e.getMessage());
			}
		}
		return value;
	}

	/** Gets the boolean value for the specified constant. */
	public boolean getBooleanValue(String constantName) {
		String property;
		property = getProperty(constantName);
		if (property == null) {
			throw new Error("Unknown Constant '" + constantName + "'!!");
		}
		return Boolean.valueOf(property).booleanValue();
	}

	/**
	 * Gets the float value for the specified constant. If the specified
	 * constant does not exist or is not in a valid floating point number format
	 * in properties then it attempts to get the value from the defaults.
	 * 
	 * @param constantName
	 * @return float value of the constant
	 */
	public float getFloatValue(String constantName) {
		float value = 0;
		String property;
		property = getProperty(constantName);
		if (property == null) {
			throw new Error("Unknown Constant '" + constantName + "'!!");
		}
		try {
			value = Float.parseFloat(property);
		} catch (NumberFormatException e) {
			System.err.println("Number Format Error: " + e.getMessage());
			System.err.println("in '" + constantName + "':");
			System.err.println("  '" + property + "' is not a valid float");
			System.err.println("  using default value instead");
			property = defaults.getProperty(constantName);
			if (property == null) {
				throw new Error("No Default Value for '" + constantName + "'!!");
			}
			try {
				value = Float.parseFloat(property);
			} catch (NumberFormatException f) {
				throw new Error("Number Format Error in default value for '" + constantName + "': " + e.getMessage());
			}
		}
		return value;
	}

	/**
	 * @param constantName
	 *            name of constant to return
	 * @return Vector3f representation of the constant
	 */
	public Vector3f getVector3f(String constantName) {
		return new Vector3f(getFloatValue(constantName + "X"), getFloatValue(constantName + "Y"),
				getFloatValue(constantName + "Z"));
	}

	/**
	 * @param constantName
	 *            name of constant to return
	 * @return Vector3f representation of the constant
	 */
	public Color3f getColor3f(String constantName) {
		return new Color3f(getFloatValue(constantName + "R"), getFloatValue(constantName + "G"),
				getFloatValue(constantName + "B"));
	}

}
