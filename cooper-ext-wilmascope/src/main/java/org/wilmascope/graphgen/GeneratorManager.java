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
package org.wilmascope.graphgen;

import org.wilmascope.util.Registry;

/**
 * Provides a registry of graph generators. If you add new generators
 * (subclassing GraphModifier) then add a call to addGenerator for your class in
 * the constructor
 * 
 * @author dwyer
 * 
 */
public class GeneratorManager extends Registry<GraphGenerator> {
	private static GeneratorManager instance = new GeneratorManager();

	/**
	 * Builds up the table of available generators and sets the default based on
	 * the "DefaultGenerator" field in the WILMA_CONSTANTS.properties file.
	 * 
	 * @throws UnknownTypeException
	 *             if the default generator
	 */
	private GeneratorManager() {
		super("DefaultGenerator", "GeneratorPlugins");
	}

	/**
	 * There should only ever be one instance of this class. Access via the
	 * following method.
	 * 
	 * @return the singleton instance of Registry
	 */
	public static GeneratorManager getInstance() {
		return instance;
	}
}
