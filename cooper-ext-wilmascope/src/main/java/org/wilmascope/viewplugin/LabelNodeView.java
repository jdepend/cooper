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

import javax.media.j3d.Appearance;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.ImageIcon;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.wilmascope.view.Colours;
import org.wilmascope.view.NodeView;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * Only the label is shown as a banner
 * 
 * @author Tim Dwyer
 * @version 1.0
 */
public class LabelNodeView extends NodeView {
	public LabelNodeView() {
		setTypeName("LabelOnly");
	}

	protected void setupDefaultMaterial() {
		setupDefaultAppearance(Colours.redMaterial);
	}

	protected void setupHighlightMaterial() {
		setupHighlightAppearance(Colours.yellowMaterial);
	}

	public void setLabel(String label) {
		float leftShift = -0.5f * (float) label.length();
		addLabel(label, 10d, new Point3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), getAppearance());
	}

	protected void init() {
		// now create a fully transparent pickable sphere to encompass the label
		// so user has something to pick.
		Appearance transApp = new Appearance();
		transApp.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.99f));
		Sphere pickableSphere = new Sphere(0.2f, Sphere.GENERATE_NORMALS, 5, transApp);
		makePickable(pickableSphere.getShape(Sphere.BODY));
		addTransformGroupChild(pickableSphere);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("labelNode.png"));
	}
}
