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

import java.util.Collection;
import java.util.Hashtable;

/*
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular WilmaScope software
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaScope.org
 * @author Tim Dwyer
 * @version 1.0
 */

/**
 * This class provides a manager or registry of all the available forces which
 * can be added to an instance of ForceLayout. This class implements the
 * Singleton design pattern (Gamma et al.) such that there can only ever be one
 * instance in the system and a reference to that instance can be obtained by
 * calling the static {link #getInstance()} method from anywhere.
 */
public class ForceManager {
	public static ForceManager getInstance() {
		return instance;
	}

	private ForceManager() {
		addPrototypeForce(new Repulsion(1.2f, 20f));
		addPrototypeForce(new RepulsionFADE(1.2f));
		addPrototypeForce(new Spring(5f));
		addPrototypeForce(new Origin(8f));
		addPrototypeForce(new DirectedField(1f));
		addPrototypeForce(new Planar(1f));
	}

	public class UnknownForceTypeException extends Exception {
		public UnknownForceTypeException(String forceType) {
			super("No known force type: " + forceType);
		}
	}

	public Force createForce(String forceType) throws UnknownForceTypeException {
		Force prototype = forces.get(forceType);
		if (prototype == null) {
			throw (new UnknownForceTypeException(forceType));
		}
		return (Force) prototype.clone();
	}

	public void addPrototypeForce(Force prototype) {
		forces.put(prototype.getTypeName(), prototype);
	}

	public Collection<Force> getAvailableForces() {
		return forces.values();
	}

	private Hashtable<String, Force> forces = new Hashtable<String, Force>();
	private static final ForceManager instance = new ForceManager();
}
