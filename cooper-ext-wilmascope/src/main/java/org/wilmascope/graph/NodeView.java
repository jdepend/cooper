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

import java.awt.Component;
import java.awt.Point;

/**
 * Title: WilmaToo Description: Sequel to the ever popular WilmaScope software
 * Copyright: Copyright (c) 2001 Company: WilmaScope.org
 * 
 * @author Tim Dwyer
 * @version 1.0
 * 
 *          An interface to be implemented by classes providing a drawable
 *          visual representation of a Node
 */

public interface NodeView extends Viewable, NodeAbility {
	public void setRadius(float radius);

	public float getRadius();

	/**
	 * @return position of node on 2D canvas, removing all local transforms
	 */
	public Point getCanvasPosition(Component canvas);
}
