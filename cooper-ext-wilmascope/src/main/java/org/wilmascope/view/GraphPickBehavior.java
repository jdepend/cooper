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

package org.wilmascope.view;

/**
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular Wilma graph drawing engine
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaOrg
 * @author Tim Dwyer
 * @version 1.0
 */

import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Shape3D;

import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;

/*
 * This is largely based on com.sun.j3d.utils.picking.behavior.rotate
 * the standard sun disclaimers are at the end of this file.  Read them b4 u
 * use any of this code in your flight control system.
 */
/**
 * A mouse behavior that allows user to pick and drag scene graph objects.
 * Common usage:
 * <p>
 * 1. Create your scene graph.
 * <p>
 * 2. Create this behavior with root and canvas.
 * <p>
 * <blockquote>
 * 
 * <pre>
 * GraphPickBehavior behavior = new GraphPickBehavior(canvas, root, bounds);
 * root.addChild(behavior);
 * </pre>
 * 
 * </blockquote>
 * <p>
 * The above behavior will monitor for any picking events on the scene graph
 * (below root node) and handle mouse drags on pick hits. Note the root node can
 * also be a subgraph node of the scene graph (rather than the topmost).
 */

public class GraphPickBehavior extends PickMouseBehavior {
	int pickMode = PickTool.BOUNDS;
	PickingClient rootPickingClient = null;

	/**
	 * Creates a pick/rotate behavior that waits for user mouse events for the
	 * scene graph. This method has its pickMode set to BOUNDS picking.
	 * 
	 * @param root
	 *            Root of your scene graph.
	 * @param canvas
	 *            Java 3D drawing canvas.
	 * @param bounds
	 *            Bounds of your scene.
	 **/

	public GraphPickBehavior(BranchGroup root, Canvas3D canvas, Bounds bounds) {
		super(canvas, root, bounds);
		this.pickCanvas.setTolerance(0.0f);
		this.setSchedulingBounds(bounds);
	}

	/**
	 * Creates a pick/rotate behavior that waits for user mouse events for the
	 * scene graph.
	 * 
	 * @param root
	 *            Root of your scene graph.
	 * @param canvas
	 *            Java 3D drawing canvas.
	 * @param bounds
	 *            Bounds of your scene.
	 * @param pickMode
	 *            specifys PickObject.USE_BOUNDS or PickObject.USE_GEOMETRY.
	 *            Note: If pickMode is set to PickObject.USE_GEOMETRY, all
	 *            geometry object in the scene graph that allows pickable must
	 *            have its ALLOW_INTERSECT bit set.
	 **/
	public GraphPickBehavior(BranchGroup root, Canvas3D canvas, Bounds bounds, int pickMode) {
		super(canvas, root, bounds);
		this.setSchedulingBounds(bounds);
		this.pickCanvas.setTolerance(0.0f);
		setMode(pickMode);
	}

	public void setRootPickingClient(PickingClient client) {
		this.rootPickingClient = client;
	}

	/**
	 * Update the scene to manipulate any nodes. This is not meant to be called
	 * by users. Behavior automatically calls this. You can call this only if
	 * you know what you are doing.
	 * 
	 * @param xpos
	 *            Current mouse X pos.
	 * @param ypos
	 *            Current mouse Y pos.
	 **/
	public void updateScene(int xpos, int ypos) {
		Shape3D shape = null;

		pickCanvas.setShapeLocation(mevent);
		try {
			PickResult pr[] = pickCanvas.pickAllSorted();
			if (pr != null) {
				for (int i = 0; i < pr.length; i++) {
					if ((shape = (Shape3D) pr[i].getNode(PickResult.SHAPE3D)) != null) {
						GraphElementView pickedElement = (GraphElementView) shape.getUserData();
						if (pickedElement != null && pickedElement.picked(mevent)) {
							break;
						}
					}
				}
			} else if (rootPickingClient != null) {
				rootPickingClient.callback(mevent);
			}
		} catch (javax.media.j3d.CapabilityNotSetException e) {
			System.err.println("This object can't be picked because: ");
			System.err.println("  " + e.getMessage());
		}
	}
}
/*
 * @(#)PickMouseBehavior.java 1.4 01/01/11 07:24:36
 * 
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear facility.
 * Licensee represents and warrants that it will not use or redistribute the
 * Software for such purposes.
 */
