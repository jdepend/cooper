package org.wilmascope.viewplugin;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.swing.ImageIcon;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.wilmascope.view.EdgeView;
import org.wilmascope.view.NodeGeometryObserver;
import org.wilmascope.view.NodeView;
import org.wilmascope.view.Renderer2D;

/**
 * Uses OpenGL (or DirectX) line segment primitive for super-fast rendering.
 * Lines colouring is smoothly graded from start node colour to end node colour
 * if used with LineNode.
 * 
 * @author dwyer
 * 
 */
public class LineEdgeView extends EdgeView implements NodeGeometryObserver {
	public LineEdgeView() {
		setTypeName("LineEdge");
	}

	protected void setupHighlightMaterial() {
		/**
		 * @todo: implement this org.wilmascope.view.GraphElementView abstract
		 *        method
		 */
	}

	protected void setupDefaultMaterial() {
		/**
		 * @todo: implement this org.wilmascope.view.GraphElementView abstract
		 *        method
		 */
	}

	LineArray myLine;

	protected void init() {
		int settings = GeometryArray.ALLOW_COORDINATE_WRITE | GeometryArray.ALLOW_COLOR_WRITE
				| GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.COLOR_3;
		Point3f[] myCoords = new Point3f[] { new Point3f(0, -0.5f, 0), new Point3f(0, 0.5f, 0) };
		myLine = new LineArray(2, settings);
		myLine.setCapability(LineArray.ALLOW_COORDINATE_WRITE);
		myLine.setCapability(LineArray.ALLOW_COLOR_WRITE);
		myLine.setCoordinates(0, myCoords);
		NodeView startView = (NodeView) getEdge().getStart().getView();
		NodeView endView = (NodeView) getEdge().getEnd().getView();
		setStartColour(startView.getColor3f());
		setEndColour(endView.getColor3f());
		startView.addGeometryObserver(this);
		endView.addGeometryObserver(this);
		Shape3D s = new Shape3D(myLine);
		makePickable(s);
		addTransformGroupChild(s);
	}

	public void draw() {
		myLine.setCoordinate(0, getEdge().getStart().getPosition());
		myLine.setCoordinate(1, getEdge().getEnd().getPosition());
	}

	/**
	 * 2D version of line edge is just a two colour solid line. First half line
	 * is coloured same as start node Second half is coloured as for end node
	 */
	public void draw2D(Renderer2D r, Graphics2D g, float transparency) {
		float thickness = r.scaleX(getRadius());
		g.setStroke(new BasicStroke(thickness));

		Point3f start = getEdge().getStart().getPosition();
		Point3f end = getEdge().getEnd().getPosition();
		Vector3f v = new Vector3f();
		v.sub(end, start);
		v.scale(0.5f);
		Point3f mid = new Point3f(start);
		mid.add(v);
		g.setColor(((NodeView) getEdge().getStart().getView()).getColour());
		r.linePath(g, start, mid);
		g.setColor(((NodeView) getEdge().getEnd().getView()).getColour());
		r.linePath(g, mid, end);
	}

	public void setStartColour(Color3f c) {
		myLine.setColor(0, c);
	}

	public void setEndColour(Color3f c) {
		myLine.setColor(1, c);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("lineEdge.png"));
	}

	public void nodeGeometryChanged(NodeView nv) {
		Color3f c = nv.getColor3f();
		if (getEdge().getStart().getView() == nv) {
			setStartColour(c);
		} else {
			setEndColour(c);
		}
	}
}
