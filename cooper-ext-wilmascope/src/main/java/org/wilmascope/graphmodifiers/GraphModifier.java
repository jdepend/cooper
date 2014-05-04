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
package org.wilmascope.graphmodifiers;

import javax.swing.JPanel;

import org.wilmascope.control.GraphControl.Cluster;
import org.wilmascope.util.Plugin;

/**
 * Boilerplate class for graph modifiers. Extend this class to create classes
 * that modify existing graphs (clusters)
 * 
 * @author dwyer
 */
public abstract class GraphModifier implements Plugin {
	/**
	 * You should simply return a string constant that is a unique identifier.
	 * This string will appear in the Graph Generator Window list so give it a
	 * capital letter and spaces where necessary.
	 * 
	 * @return unique identifier that will also appear in menus
	 */
	public abstract String getName();

	/**
	 * This method will do the work of actually modifying the graph.
	 * 
	 * @param cluster
	 *            the subgraph on which to operate
	 */
	public abstract void modify(Cluster cluster);

	/**
	 * the best way to use this is to create a JPanel with controls for setting
	 * parameters for your graph modifier in your constructor and ensure that
	 * this method returns a reference to it.
	 * 
	 * @return a control panel for setting the parameters for the generated
	 *         graph
	 */
	public abstract JPanel getControls();
}
