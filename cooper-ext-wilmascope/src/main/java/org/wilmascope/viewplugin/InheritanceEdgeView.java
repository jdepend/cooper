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
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.vecmath.Vector3f;

import org.wilmascope.view.Colours;
import org.wilmascope.view.EdgeView;
import org.wilmascope.view.LODCylinder;

import com.sun.j3d.utils.geometry.Cone;

/**
 * Graphical representation of the edge
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class InheritanceEdgeView extends EdgeView {
	public InheritanceEdgeView() {
		setTypeName("Inheritance");
	}

	protected void setupDefaultMaterial() {
		Material material = new Material();
		material.setDiffuseColor(0.0f, 0.0f, 1.0f);
		material.setAmbientColor(0f, 0f, 0.4f);
		material.setShininess(50.0f);
		setupDefaultAppearance(material);
	}

	protected void setupHighlightMaterial() {
		setupHighlightAppearance(Colours.yellowMaterial);
	}

	public void init() {
		LODCylinder cylinder = new LODCylinder(1f, 0.7f, getAppearance());
		// addShape(new
		// Shape3D(edgeCylinder.getShape(Cylinder.BODY).getGeometry()));

		cylinder.makePickable(this);
		Transform3D transform = new Transform3D();
		transform.setTranslation(new Vector3f(0f, 0.15f, 0f));
		TransformGroup tg = new TransformGroup(transform);
		cylinder.addToTransformGroup(tg);
		addTransformGroupChild(tg);
		showDirectionIndicator();
	}

	public void showDirectionIndicator() {
		Appearance appearance = new Appearance();
		appearance.setMaterial(org.wilmascope.view.Colours.greenMaterial);
		Cone cone = new Cone(2f, 0.3f, Cone.GENERATE_NORMALS, appearance);
		makePickable(cone.getShape(Cone.BODY));
		makePickable(cone.getShape(Cone.CAP));
		Transform3D rotation = new Transform3D();
		Transform3D transform = new Transform3D();
		transform.setTranslation(new Vector3f(0f, -0.3f, 0f));
		rotation.rotX(Math.PI);
		TransformGroup coneTransform = new TransformGroup(transform);
		TransformGroup coneRotate = new TransformGroup(rotation);
		coneRotate.addChild(cone);
		coneTransform.addChild(coneRotate);
		addTransformGroupChild(coneTransform);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("inheritance.png"));
	}
}
