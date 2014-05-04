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

package org.wilmascope.patterns;

import java.util.Hashtable;

/**
 * Use Singleton.instance(String) to access a given instance by name.
 */
public class Singleton {
	static private Hashtable _registry = new Hashtable();

	// A static initializer that creates an instance of
	// this class and adds it to the registry when
	// the byte-code for this class is first loaded
	static {
		Singleton foo = new Singleton();
		_registry.put(foo.getClass().getName(), foo);
	}

	/**
	 * The constructor could be made private to prevent others from instatiating
	 * this class. But this would also make it impossible to create instances of
	 * Singleton subclasses.
	 */
	protected Singleton() {
		// ...
	}

	/**
	 * @return The unique instance of the specified class.
	 */
	static public Singleton instance(String byname) {
		return (Singleton) (_registry.get(byname));
	}

	// ...additional methods omitted...
}
