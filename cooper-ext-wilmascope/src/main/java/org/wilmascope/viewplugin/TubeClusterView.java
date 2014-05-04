package org.wilmascope.viewplugin;

import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.view.ClusterView;
import org.wilmascope.view.Colours;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class TubeClusterView extends ClusterView {
	public TubeClusterView() {
		setTypeName("Tube Cluster");
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
		setResizeTranslateTransform(new Vector3d(radius, radius, 1d), new Vector3f(c.getPosition()));
	}

	public void init() {
		setExpandedView();

		// LODSphere sphere = new LODSphere(1f,getAppearance());
		// sphere.makePickable(this);
		// sphere.addToTransformGroup(getTransformGroup());

		Cylinder inside = new Cylinder(0.99f, 2.4f, Cylinder.GENERATE_NORMALS_INWARD, getAppearance());
		Cylinder outside = new Cylinder(1f, 2.4f, getAppearance());
		Shape3D is = new Shape3D(inside.getShape(Cylinder.BODY).getGeometry());
		Shape3D os = new Shape3D(outside.getShape(Cylinder.BODY).getGeometry());
		is.setAppearance(getAppearance());
		os.setAppearance(getAppearance());
		Transform3D t = new Transform3D();
		t.rotX(Math.PI / 2);
		t.setTranslation(new Vector3d(0.0, 0.0, 1.2));
		TransformGroup g = new TransformGroup(t);
		g.addChild(is);
		g.addChild(os);
		getTransformGroup().addChild(g);
		makePickable(is);
		makePickable(os);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("planarcluster.png"));
	}
}
