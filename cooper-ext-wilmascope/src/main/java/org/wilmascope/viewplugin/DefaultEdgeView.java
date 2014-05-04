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

package org.wilmascope.viewplugin;

import org.wilmascope.view.Colours;
import org.wilmascope.view.EdgeView;
import org.wilmascope.view.LODCylinder;

/**
 * Graphical representation of the edge
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class DefaultEdgeView extends EdgeView {
	float length = 1.0f;

	public DefaultEdgeView() {
		setTypeName("Plain Edge");
	}

	protected void setupDefaultMaterial() {
		setupDefaultAppearance(Colours.blueMaterial);
	}

	protected void setupHighlightMaterial() {
		setupHighlightAppearance(Colours.yellowMaterial);
	}

	public void init() {
		/*
		 * Cylinder edgeCylinder = new
		 * Cylinder(radius,1,Cylinder.GENERATE_NORMALS,10,1,getAppearance());
		 * makePickable(edgeCylinder.getShape(Cylinder.BODY));
		 * makePickable(edgeCylinder.getShape(Cylinder.TOP));
		 * makePickable(edgeCylinder.getShape(Cylinder.BOTTOM));
		 * addTransformGroupChild(edgeCylinder);
		 */

		LODCylinder cylinder = new LODCylinder(length, length, getAppearance());
		cylinder.makePickable(this);
		cylinder.addToTransformGroup(getTransformGroup());
	}
}
