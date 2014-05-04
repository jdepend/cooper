package org.wilmascope.viewplugin;

import javax.swing.ImageIcon;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.graph.Plane;
import org.wilmascope.view.ClusterView;
import org.wilmascope.view.Colours;

import com.sun.j3d.utils.geometry.Cone;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class ConeClusterView extends ClusterView {
	public ConeClusterView() {
		setTypeName("Planar Cluster");
	}

	protected void setupDefaultMaterial() {
		setupDefaultAppearance(Colours.pinkMaterial);
	}

	protected void setupHighlightMaterial() {
		setupHighlightAppearance(Colours.yellowMaterial);
	}

	public void draw() {
		org.wilmascope.graph.Cluster c = (org.wilmascope.graph.Cluster) getNode();
		double radius = (double) getRadius();
		Plane p = c.getNodes().getBestFitPlane();
		Vector3f norm = new Vector3f();
		Vector3f v = p.getNormal();
		AxisAngle4f angle = getAxisAngle4f(new Vector3f(0, 1, 0), v);

		setFullTransform(new Vector3d(radius, radius, radius), new Vector3f(p.getCentroid()), angle);
	}

	public void init() {
		setExpandedView();

		// LODSphere sphere = new LODSphere(1f,getAppearance());
		// sphere.makePickable(this);
		// sphere.addToTransformGroup(getTransformGroup());

		Cone cone = new Cone(1f, 0.2f, getAppearance());
		getTransformGroup().addChild(cone);
		makePickable(cone.getShape(Cone.BODY));
		makePickable(cone.getShape(Cone.CAP));
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("planarcluster.png"));
	}
}
