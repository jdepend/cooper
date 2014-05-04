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

package org.wilmascope.view;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.graph.Node;

import com.sun.j3d.utils.geometry.Cone;

/**
 * Title: WilmaToo Description: Sequel to the ever popular WilmaScope software
 * Copyright: Copyright (c) 2001 Company: WilmaScope.org
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public abstract class NodeView extends GraphElementView implements org.wilmascope.graph.NodeView, View2D {

	private Vector<WeakReference<NodeGeometryObserver>> geometryObservers;

	/*
	 * Let any edges observing our geometry know that it has changed
	 */
	private void notifyGeometryObservers() {
		if (geometryObservers != null) {
			for (Iterator<WeakReference<NodeGeometryObserver>> i = geometryObservers.iterator(); i.hasNext();) {
				WeakReference<NodeGeometryObserver> wr = i.next();
				NodeGeometryObserver o = wr.get();
				if (o != null) {
					o.nodeGeometryChanged(this);
				} else {
					i.remove();
				}
			}
		}
	}

	/**
	 * By implementing the NodeGeometryObserver interface an EdgeView can
	 * register with this NodeView to be notified of any changes to its geometry
	 * so that it can update itself accordingly
	 * 
	 * @param o
	 */
	public void addGeometryObserver(NodeGeometryObserver o) {
		WeakReference<NodeGeometryObserver> wr = new WeakReference<NodeGeometryObserver>(o);
		if (geometryObservers == null) { // lazy instantiation avoids needing
											// init
			// method
			geometryObservers = new Vector<WeakReference<NodeGeometryObserver>>();
		}
		geometryObservers.add(wr);
	}

	/*
	 * public void draw() { setTranslation(new Vector3f(node.getPosition())); }
	 */
	public void draw() {
		Node n = getNode();
		setResizeTranslateTransform(new Vector3d(radius, radius, radius), new Vector3f(n.getPosition()));
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("node.png"));
	}

	protected void showLabel(String text) {
		// the log term on the scale is a feable attempt to stop labels getting
		// too
		// big with large clusters... however it doesn't really work because
		// clusters
		// typically grow or shrink after a label is set... when the label will
		// be
		// scaled linearly along with the cluster. A more correct solution would
		// be
		// to have the label branch scaled separately in the draw method.
		addLabel(text, 10d / Math.log(radius * 10 * Math.E), new Point3f(0.0f, 0.05f, -0.07f), new Vector3f(-0.1f
				* (float) text.length(), 1.3f, 0.0f), getAppearance());
	}

	BranchGroup anchorBranch;

	public void showAnchor() {
		Appearance a = new Appearance();
		a.setMaterial(Colours.blueMaterial);
		Cone pin = new Cone(0.05f, 0.1f, a);
		makePickable(pin.getShape(Cone.BODY));
		makePickable(pin.getShape(Cone.CAP));
		anchorBranch = new BranchGroup();
		anchorBranch.setCapability(BranchGroup.ALLOW_DETACH);
		Transform3D t = new Transform3D();
		t.set(new Vector3f(0, -radius, 0));
		TransformGroup tg = new TransformGroup(t);
		Transform3D r = new Transform3D();
		r.setRotation(new AxisAngle4f(-1f, 0f, 1f, 3f * (float) Math.PI / 4f));
		TransformGroup rg = new TransformGroup(r);
		tg.addChild(pin);
		rg.addChild(tg);
		anchorBranch.addChild(rg);
		addLiveBranch(anchorBranch);
	}

	public void hideAnchor() {
		anchorBranch.detach();
	}

	public Point3f getVWorldPosition() {
		Transform3D localToVworld = new Transform3D();
		getTransformGroup().getLocalToVworld(localToVworld);
		// get current node position and convert to VWorld
		Point3f pos = new Point3f(node.getPosition());
		localToVworld.transform(pos);
		return pos;
	}

	public Point getCanvasPosition(Component canvas) {
		GraphCanvas c = (GraphCanvas) canvas;
		Transform3D localToVworld = new Transform3D();
		Transform3D vWorldToImagePlate = new Transform3D();
		getTransformGroup().getLocalToVworld(localToVworld);
		c.getVworldToImagePlate(vWorldToImagePlate);
		// get current node position and convert to VWorld
		Point3d pos = new Point3d(node.getPosition());
		localToVworld.transform(pos);
		vWorldToImagePlate.transform(pos);
		Point2d pos2d = new Point2d();
		c.getPixelLocationFromImagePlate(pos, pos2d);
		return new Point((int) (pos2d.x), (int) (pos2d.y));
	}

	/**
	 * Move the node in a plane parallel to the view plate, such that it appears
	 * at the point on the canvas specified in x and y. The x and y parameters
	 * are in AWT coordinates with 0,0 at the top left of the canvas.
	 * 
	 * @param c
	 *            the canvas
	 * @param x
	 *            the x awt coordinate
	 * @param y
	 *            the y awt coordinate
	 */
	public void moveToCanvasPos(GraphCanvas c, int x, int y) {
		// All transformations are done in VWorld coordinates so set up the
		// transforms necessary to convert
		Transform3D localToVworld = new Transform3D();
		Transform3D imagePlateToVworld = new Transform3D();
		c.getImagePlateToVworld(imagePlateToVworld);
		getTransformGroup().getLocalToVworld(localToVworld);

		// get eye position
		Point3d d = new Point3d();
		c.getCenterEyeInImagePlate(d);
		imagePlateToVworld.transform(d);
		Point3f eyePos = new Point3f(d);

		// get mouse position
		c.getPixelLocationInImagePlate(x, y, d);
		imagePlateToVworld.transform(d);
		Point3f mousePos = new Point3f(d);

		// calculate vector from eye to canvas
		Vector3f eyeToCanvas = new Vector3f();
		eyeToCanvas.sub(mousePos, eyePos);

		Point3f pos = getVWorldPosition();
		// the target position will be at a point in the same plane parallel to
		// the view plate. Calculate the scale factor for eye to canvas in order
		// to place the node from the known z values.
		float t = (pos.z - mousePos.z) / eyeToCanvas.z;

		pos.scale(t, eyeToCanvas);
		pos.add(mousePos);

		// If this method turns out to be too slow we could easily speed it up
		// by calculating the transforms in advance.

		// move the node
		localToVworld.invert();
		localToVworld.transform(pos);
		node.setPosition(pos);
	}

	/**
	 * When a new colour is set any GeometryObservers that have been added to
	 * this object will be notified
	 * 
	 * @see addGeometryObserver(NodeGeometryObserver)
	 * @param c
	 *            new colour for this NodeView
	 */
	public void setColour(Color3f c) {
		super.setColour(c);
		notifyGeometryObservers();
	}

	/**
	 * sets the radius of the node's boundary sphere
	 * 
	 * @param radius
	 *            the radius of the node, (0.25 is a good size)
	 */
	public void setRadius(float radius) {
		this.radius = radius;
		notifyGeometryObservers();
	}

	/**
	 * @return radius of the nodes boundary sphere
	 */
	public float getRadius() {
		return radius;
	}

	public void setProperties(Properties p) {
		super.setProperties(p);
		String radius = p.getProperty("Radius");
		if (radius != null) {
			setRadius(Float.parseFloat(radius));
		}
	}

	public Properties getProperties() {
		Properties p = super.getProperties();
		p.setProperty("Radius", "" + radius);
		return p;
	}

	private Node node;

	private float radius = 0.1f;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.view.View2D#draw2D(org.wilmascope.view.Renderer2D,
	 * java.awt.Graphics2D, float)
	 */
	public void draw2D(Renderer2D r, Graphics2D g, float transparency) {
		Color3f c = new Color3f();
		try {
			getAppearance().getMaterial().getDiffuseColor(c);
			// g.setColor(new Color(c.x,c.y,c.z,transparency));
		} catch (NullPointerException e) {
			System.err.println("Null pointer getting node colour... fix!");
		}
		g.setColor(c.get());
		r.fillCircle(g, getNode().getPosition(), radius);
	}
}
