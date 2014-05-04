package org.wilmascope.viewplugin;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.vecmath.Vector3f;

import org.wilmascope.view.Colours;
import org.wilmascope.view.EdgeView;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Graphical representation of the edge
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class DirectedEdgeView extends EdgeView {
	/**
	 * Shows a directed edge with a little cone next to the edge. Alternative to
	 * arrow. Originally intended as a 3D analog for UML directed edges.
	 */
	float radius = 0.02f;

	public DirectedEdgeView() {
		setTypeName("Directed Edge");
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
		Cylinder edgeCylinder = new Cylinder(radius, 1);
		addShape(new Shape3D(edgeCylinder.getShape(Cylinder.BODY).getGeometry()));
		showDirectionIndicator();
	}

	public void showDirectionIndicator() {
		Appearance appearance = new Appearance();
		appearance.setMaterial(org.wilmascope.view.Colours.blueMaterial);
		Cone cone = new Cone(0.03f, 0.07f, Cone.GENERATE_NORMALS, appearance);
		Transform3D transform = new Transform3D();
		transform.setTranslation(new Vector3f(0.07f, 1 / 4f, 0f));
		TransformGroup coneTransform = new TransformGroup(transform);
		coneTransform.addChild(cone);
		addTransformGroupChild(coneTransform);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("directedEdge.png"));
	}
}
