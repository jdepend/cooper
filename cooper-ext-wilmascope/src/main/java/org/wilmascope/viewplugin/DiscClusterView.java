package org.wilmascope.viewplugin;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeList;
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

public class DiscClusterView extends ClusterView {
	public DiscClusterView() {
		setTypeName("Disc Cluster");
	}

	protected void setupDefaultMaterial() {
		setupDefaultAppearance(Colours.greyBlueMaterial);
	}

	protected void setupHighlightMaterial() {
		setupHighlightAppearance(Colours.yellowMaterial);
	}

	// The radius at any given time should be the distance from the centre of
	// the
	// cluster to the farthest node
	private float calcRadius() {
		float newRadius;
		Cluster c = (Cluster) getNode();
		NodeList nodes = c.getNodes();
		float maxX, maxY, minX, minY, z = 0;
		maxX = maxY = Float.MIN_VALUE;
		minX = minY = Float.MAX_VALUE;
		for (Node member : nodes) {
			Point3f pos = member.getPosition();
			if (pos.x < minX)
				minX = pos.x;
			if (pos.x > maxX)
				maxX = pos.x;
			if (pos.y < minY)
				minY = pos.y;
			if (pos.y > maxY)
				maxY = pos.y;
			z = pos.z;
		}
		newRadius = (maxX - minX) / 2f;
		setRadius(newRadius);
		c.setPosition(new Point3f(minX + newRadius, minY + newRadius, z));
		return newRadius;
	}

	public void draw() {
		org.wilmascope.graph.Cluster c = (org.wilmascope.graph.Cluster) getNode();
		double radius = calcRadius();
		setResizeTranslateTransform(new Vector3d(radius, radius, radius), new Vector3f(c.getPosition()));
	}

	public void init() {
		setExpandedView();
		Cylinder cyl = new Cylinder(1f, 0.02f, getAppearance());
		Transform3D t = new Transform3D();
		t.rotX(Math.PI / 2.0);
		TransformGroup g = new TransformGroup(t);
		g.addChild(cyl);
		getTransformGroup().addChild(g);
		makePickable(cyl.getShape(Cylinder.BODY));
		makePickable(cyl.getShape(Cylinder.TOP));
		makePickable(cyl.getShape(Cylinder.BOTTOM));
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("planarcluster.png"));
	}
}
