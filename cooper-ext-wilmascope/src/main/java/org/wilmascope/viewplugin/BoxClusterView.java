package org.wilmascope.viewplugin;

import javax.swing.ImageIcon;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.view.ClusterView;
import org.wilmascope.view.Colours;

import com.sun.j3d.utils.geometry.Box;

/**
 * Box shaped cluster
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class BoxClusterView extends ClusterView {
	public BoxClusterView() {
		setTypeName("Box Cluster");
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
		setResizeTranslateTransform(new Vector3d(radius, radius, radius), new Vector3f(c.getPosition()));
	}

	public void init() {
		setExpandedView();
		Box box = new Box(1f, 1f, 1f, getAppearance());
		getTransformGroup().addChild(box);
		makePickable(box.getShape(Box.FRONT));
		makePickable(box.getShape(Box.BACK));
		makePickable(box.getShape(Box.LEFT));
		makePickable(box.getShape(Box.RIGHT));
		makePickable(box.getShape(Box.TOP));
		makePickable(box.getShape(Box.BOTTOM));
	}

	protected float getCollapsedRadius(float density) {
		return (float) Math.pow(getCluster().getMass() / density, 1.0d / 3.0d);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("boxcluster.png"));
	}
}
