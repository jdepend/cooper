package org.wilmascope.view;

import javax.media.j3d.Appearance;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.ImageIcon;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Node;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author
 * @version 1.0
 */

public abstract class ClusterView extends NodeView {
	public ClusterView() {
	}

	/**
	 * update the cluster's size and position on screen
	 */
	public void draw() {
		Node n = getNode();
		double radius = (double) getRadius();
		setResizeTranslateTransform(new Vector3d(radius, radius, radius), new Vector3f(n.getPosition()));
	}

	public void setExpandedView() {
		Appearance a = getAppearance();
		setTransparencyAttributes(transparencyAttributes);
	}

	/**
	 * the collapsed cluster view will be opaque and will have radius
	 * proportional to the mass where: mass = density * volume = 4/3 PI r^3
	 * density is arbitrarily chosen to make the collapsed cluster a convenient
	 * size
	 */
	public void setCollapsedView() {
		getAppearance().setTransparencyAttributes(null);
		setRadius(getCollapsedRadius(density));
	}

	protected float getCollapsedRadius(float density) {
		return (float) Math.pow(3f * getCluster().getMass() / (4f * density * Math.PI), 1.0d / 3.0d);
	}

	public Cluster getCluster() {
		return (Cluster) getNode();
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("cluster.png"));
	}

	/*
	 * protected void showLabel(String text) { addLabel(text, 0.1d, new
	 * Point3f(0.0f,30.0f*getCluster().getRadius(),0.0f), Constants.vZero,
	 * getAppearance()); }
	 */
	private TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.FASTEST,
			0.6f);
	private static final float density = 25f;
}
