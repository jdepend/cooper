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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.graph.Edge;
import org.wilmascope.view.Colours;
import org.wilmascope.view.EdgeView;
import org.wilmascope.view.NodeView;
import org.wilmascope.view.Renderer2D;

import com.sun.j3d.utils.geometry.Cone;

/**
 * Graphical representation of the edge
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class SpikeEdgeView extends EdgeView {
	public SpikeEdgeView() {
		setTypeName("Spike");
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
		showDirectionIndicator();
		setColour(((NodeView) getEdge().getEnd().getView()).getColour());
	}

	public void showDirectionIndicator() {
		Cone cone = new Cone(2f, 0.1f, Cone.GENERATE_NORMALS, getAppearance());
		makePickable(cone.getShape(Cone.BODY));
		makePickable(cone.getShape(Cone.CAP));
		Transform3D transform = new Transform3D();
		// transform.setTranslation(new Vector3f(0f, 0.45f, 0f));
		// transform.rotX(Math.PI);
		TransformGroup coneTransform = new TransformGroup(transform);
		coneTransform.addChild(cone);
		addTransformGroupChild(coneTransform);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("arrow.png"));
	}

	/**
	 * draw the edge correctly between the start and end nodes
	 */
	public void draw() {
		Edge edge = getEdge();
		float radius = getRadius();
		getEdge().recalculate();
		double l = 5 * edge.getWeight();
		Vector3f positionVector = new Vector3f(edge.getStart().getPosition());
		Vector3f v = new Vector3f(edge.getVector());
		v.normalize();
		v.scale((float) (edge.getStart().getView().getRadius() + 0.05 * l));
		positionVector.add(v);
		setFullTransform(new Vector3d(radius, l, radius), positionVector, getPositionAngle());
	}

	public void draw2D(Renderer2D r, Graphics2D g, float transparency) {
		float thickness = r.scaleX(getRadius());
		g.setStroke(new BasicStroke(thickness));
		Color3f c = new Color3f();
		getAppearance().getMaterial().getDiffuseColor(c);
		g.setColor(new Color(c.x, c.y, c.z, transparency));
		Point3f start = getEdge().getStart().getPosition();
		Point3f end = getEdge().getEnd().getPosition();
		Vector3f v = new Vector3f();
		v.sub(end, start);
		v.scale(0.9f);
		Point3f lineEnd = new Point3f(start);
		lineEnd.add(v);
		r.linePath(g, start, lineEnd);
		r.arrowPath(g, thickness / 10, lineEnd, end);
	}
}
