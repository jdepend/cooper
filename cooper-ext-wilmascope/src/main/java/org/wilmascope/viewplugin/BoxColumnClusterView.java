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

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.columnlayout.ColumnLayout;
import org.wilmascope.columnlayout.NodeColumnLayout;
import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Node;
import org.wilmascope.view.Colours;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Text2D;

public class BoxColumnClusterView extends ColumnClusterView {
	public void draw() {
		try {
			Cluster c = (Cluster) getNode();
			if (c.getNodes().size() == 0) {
				// not ready to draw yet!
				return;
			}
			float zMax = Float.MIN_VALUE;
			float zMin = Float.MAX_VALUE;
			float h = ((ColumnLayout) c.getLayoutEngine()).getHeight();
			Vector3f v = new Vector3f(c.getPosition());
			Node firstNode = c.getNodes().get(0);
			v.z = firstNode.getPosition().z + (h - ((NodeColumnLayout) firstNode.getLayout()).getHeight()) / 2f;
			// gap is simply to make our cluster slightly smaller than the
			// nodes,
			// so that the nodes are drawn in front of the cluster and we don't
			// get wierd affects due to
			// the overlapping surfaces
			float gap = 0.001f;
			float maxRadius = getMaxRadius();
			float depth = getDepth();
			if (maxRadius == 0) {
				depth = 0;
			} else {
				maxRadius = maxRadius / 4f - gap;
				depth -= gap;
			}
			Vector3d scale = new Vector3d(maxRadius, depth, h / 2.0 - gap);
			setResizeTranslateTransform(scale, v);
			Transform3D t = new Transform3D();
			Vector3d tScale = new Vector3d(1.0 / scale.x, 1.0 / scale.y, 1.0 / scale.z);
			t.setTranslation(new Vector3f(0, 0, h * (float) tScale.z / 2.0f));
			t.setScale(tScale);
			textTG.setTransform(t);
		} catch (NullPointerException e) {
			System.out.println("WARNING: Null pointer in ColumnClusterView.draw()");
		}
	}

	public void setLabelColour(Color3f c) {
		this.labelColour = c;
	}

	Color3f labelColour = new Color3f(0f, 0f, 0f);
	String[] labels;
	TransformGroup textTG;

	public void setLabel(String[] labels) {
		this.labels = labels;
		textTG = new TransformGroup();
		textTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3f(0, 0.15f * (float) (labels.length - 1), 0));
		textTG.setTransform(trans);
		for (int i = 0; i < labels.length; i++) {
			Text2D text = new Text2D(labels[i], labelColour, "dummy", 60, java.awt.Font.PLAIN);
			makePickable(text);
			TransformGroup textTG2 = new TransformGroup();
			trans = new Transform3D();
			trans.setTranslation(new Vector3f(-0.5f, -0.3f * (float) i, 0.01f));
			textTG2.setTransform(trans);
			textTG2.addChild(text);
			textTG.addChild(textTG2);
		}
		setLabel(textTG);
	}

	public void setLabel(String label) {
		setLabel(new String[] { label });
	}

	public String getLabel() {
		return labels[0];
	}

	public BoxColumnClusterView() {
		setTypeName("Box Column Cluster");
	}

	protected void setupDefaultMaterial() {
		setupDefaultAppearance(Colours.pinkMaterial);
	}

	protected void setupHighlightMaterial() {
		setupHighlightAppearance(Colours.yellowMaterial);
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

	/**
	 * always BOX
	 * 
	 * @see org.wilmascope.view.SizeAdjustableNodeView#getShape()
	 */
	public int getShape() {
		return BOX;
	}

}
