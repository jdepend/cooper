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

package org.wilmascope.graph;

import java.util.Properties;

/**
 * Title: WilmaToo Description: Sequel to the ever popular WilmaScope software
 * Copyright: Copyright (c) 2001 Company: WilmaScope.org
 * 
 * @author Tim Dwyer
 * @version 1.0
 * 
 *          An interface to be implemented by classes providing properties for
 *          nodes that can be placed by a LayoutEngine
 */

public abstract class NodeLayout implements Layable, NodeAbility {
	/** dereference links */
	public void delete() {
		node = null;
	}

	/** Set the node for the NodeLayout */
	public void setNode(Node node) {
		this.node = node;
	}

	/** Return the node for the NodeLayout */
	public Node getNode() {
		return node;
	}

	public boolean isFixedPosition() {
		return fixedPosition;
	}

	public void setFixedPosition(boolean fixed) {
		Properties p = node.getProperties();
		if (fixed) {
			p.setProperty("FixedPosition", "true");
		} else {
			p.remove("FixedPosition");
		}
	}

	boolean fixedPosition = false;

	/**
	 * Reset's properties common to all layouts, these include: FixedPosition
	 * 
	 * Should be overridden with a call to this inherited method to load any
	 * properties from the node properties that are specific to the layout
	 * engine
	 */
	public void resetProperties() {
		Properties nodeProperties = getNode().getProperties();
		fixedPosition = false;
		String str = nodeProperties.getProperty("FixedPosition");
		if (str != null) {
			if (str.toLowerCase().equals("true")) {
				fixedPosition = true;
			}
		}
	}

	Node node;
}
