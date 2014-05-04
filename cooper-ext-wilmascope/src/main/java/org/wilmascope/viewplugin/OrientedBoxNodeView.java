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

import java.awt.Font;

import javax.media.j3d.Appearance;
import javax.media.j3d.QuadArray;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.ImageIcon;
import javax.vecmath.Point3d;

import org.wilmascope.view.Colours;
import org.wilmascope.view.NodeView;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Text2D;

/**
 * A box that always faces the viewer. Labels are texture mapped onto the face.
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class OrientedBoxNodeView extends NodeView {
	public OrientedBoxNodeView() {
		setTypeName("Oriented Box Node");
	}

	protected void setupDefaultMaterial() {
		setupDefaultAppearance(Colours.defaultMaterial);
		getAppearance().setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		getAppearance().setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
	}

	protected void setupHighlightMaterial() {
		setupHighlightAppearance(Colours.yellowMaterial);
	}

	protected void init() {
		float radius = getRadius();
		// make the main box that will carry the texture mapped label
		box = new OrientedLabelCube(getAppearance(), 1.0f, 1.0f);
		makePickable(box);
		addTransformGroupChild(box);
		// add a border without the label texture... just to make it look more
		// 3D.
		Appearance app = new Appearance();
		app.setMaterial(getAppearance().getMaterial());
		border = new OrientedNoLabelCube(app, 1.0f, 1.0f);
		makePickable(border);
		addTransformGroupChild(border);
		// now create a fully transparent pickable sphere to encompass the box.
		// This is because Oriented shapes don't seem to work at all well
		// with geometry picking. This expensive cludge simulates bounds
		// picking.
		Appearance transApp = new Appearance();
		transApp.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.99f));
		pickableSphere = new Sphere(2 * radius, Sphere.GENERATE_NORMALS, 5, transApp);
		makePickable(pickableSphere.getShape(Sphere.BODY));
		addTransformGroupChild(pickableSphere);
	}

	public void showLabel(String label) {
		Appearance appearance = getAppearance();
		Text2D textObject = new Text2D(label, org.wilmascope.view.Colours.black, "Arial", 55, Font.PLAIN);
		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.DECAL);
		appearance.setTextureAttributes(texAttr);

		appearance.setTexture(textObject.getAppearance().getTexture());

		// Create a label cube wide enough to comfortably fit the text */
		Point3d coord = new Point3d();
		QuadArray qa = (QuadArray) textObject.getGeometry();
		// First coordinate in the GeometryArray of the textObject should
		// be the top right corner so use its x value to determine the width
		qa.getCoordinate(0, coord);
		float widthScale;
		// 3.0 is the maximum scaling factor, 1.0 is the minimum
		if (coord.x > 2.1d) {
			widthScale = 3.0f;
		} else if (coord.x < 1.0d) {
			widthScale = 1.0f;
		} else {
			widthScale = (float) coord.x;
		}
		// getNode().setRadius(width);
		box.generateGeometry(1f, widthScale);
		border.generateGeometry(1f, widthScale);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("cube.png"));
	}

	OrientedLabelCube box;
	OrientedNoLabelCube border;
	Sphere pickableSphere;
}
