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

import org.wilmascope.graph.Cluster;

/*
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular WilmaScope software
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaScope.org
 * @author Tim Dwyer
 * @version 1.0
 */
/**
 * Super class that any force must extend
 */
public abstract class Force extends org.wilmascope.patterns.Prototype {
	/**
	 * The strength of the force
	 */
	protected float strengthConstant;

	/**
	 * Calculate the position deltas due to this force
	 * 
	 * @param v
	 *            The graph elements to which this force applies
	 */
	public abstract void calculate();

	/**
	 * Set the strength constant for this force
	 * 
	 * @param strengthConstant
	 *            new strength constant value
	 */
	public void setStrengthConstant(float strengthConstant) {
		this.strengthConstant = strengthConstant;
	}

	/**
	 * Query the current strength constant
	 * 
	 * @return the Strength Constant
	 */
	public float getStrengthConstant() {
		return strengthConstant;
	}

	/**
	 * Return value of strengthConstant as a string
	 * 
	 * @return String strengthConstant
	 */
	public String toString() {
		return "strengthConstant = " + strengthConstant;
	}

	public abstract void setCluster(Cluster root);

	protected Force(float strengthConstant, String name) {
		setStrengthConstant(strengthConstant);
		setTypeName(name);
	}
}
