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
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.view.Colours;
import org.wilmascope.view.EdgeView;
import org.wilmascope.view.LODCylinder;

import com.sun.j3d.utils.geometry.Box;

/**
 * Graphical representation of the edge
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class AggregationEdgeView extends EdgeView {
	public AggregationEdgeView() {
		setTypeName("Aggregation");
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
		LODCylinder cylinder = new LODCylinder(1f, 0.8f, getAppearance());
		// addShape(new
		// Shape3D(edgeCylinder.getShape(Cylinder.BODY).getGeometry()));

		cylinder.makePickable(this);
		Transform3D transform = new Transform3D();
		transform.setTranslation(new Vector3f(0f, 0.1f, 0f));
		TransformGroup tg = new TransformGroup(transform);
		cylinder.addToTransformGroup(tg);
		addTransformGroupChild(tg);
		showDirectionIndicator();
	}

	public void showDirectionIndicator() {
		float width = 0.1f;
		Appearance appearance = new Appearance();
		appearance.setMaterial(org.wilmascope.view.Colours.redMaterial);
		Box diamond = new Box(width, width, width, appearance);
		makePickable(diamond.getShape(Box.TOP));
		makePickable(diamond.getShape(Box.BOTTOM));
		makePickable(diamond.getShape(Box.BACK));
		makePickable(diamond.getShape(Box.FRONT));
		makePickable(diamond.getShape(Box.LEFT));
		makePickable(diamond.getShape(Box.RIGHT));
		Transform3D rotation = new Transform3D();
		rotation.setRotation(new AxisAngle4f(1f, 0f, 1f, (float) Math.atan(Math.sqrt(2))));
		TransformGroup rotationTG = new TransformGroup(rotation);
		rotationTG.addChild(diamond);
		Transform3D transform = new Transform3D();
		transform.setTranslation(new Vector3f(0f, -0.35f, 0f));
		transform.setScale(new Vector3d(15d, 0.7d, 15d));
		TransformGroup tg = new TransformGroup(transform);
		tg.addChild(rotationTG);
		addTransformGroupChild(tg);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("aggregation.png"));
	}
}
