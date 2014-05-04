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
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryStripArray;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.ImageIcon;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.wilmascope.dotparser.EdgeClient;
import org.wilmascope.graph.Edge;
import org.wilmascope.view.EdgeView;
import org.wilmascope.view.Renderer2D;
import org.wilmascope.view.SplineMethods;
import org.wilmascope.view.View2D;
import org.wilmascope.view.ViewConstants;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

/**
 * An edge view which extrudes a tube out along a spline
 */

public class SplineRectTubeEdgeView extends EdgeView implements View2D, org.wilmascope.dotlayout.Spline {
	//
	// create the basic reference geometries from the shape sections of a
	// cylinder
	//
	static Point3f[] tubePoints;
	static int[] tubeStripCounts;
	final static int xSize = 4, ySize = 10, segmentSize = 2 * (xSize + 1);
	final static float yStep = 1f / (float) ySize;
	Rectangle bounds = new Rectangle();
	static NormalGenerator normalGenerator = new NormalGenerator();
	{
		Cylinder c = new Cylinder(1f, 1f, 0, xSize, ySize, null);
		GeometryStripArray tubeGeometry = getGeometry(c, Cylinder.BODY);
		tubePoints = new Point3f[tubeGeometry.getVertexCount()];
		tubeStripCounts = new int[tubeGeometry.getNumStrips()];
		loadGeometry(tubeGeometry, tubeStripCounts, tubePoints);
	}

	private static GeometryStripArray getGeometry(Cylinder c, int section) {
		return (GeometryStripArray) c.getShape(section).getGeometry();
	}

	private static void loadGeometry(GeometryStripArray geometry, int[] stripCounts, Point3f[] points) {
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point3f();
		}
		geometry.getCoordinates(0, points);
		geometry.getStripVertexCounts(stripCounts);
	}

	public SplineRectTubeEdgeView() {
		setTypeName("SplineRectTube");
	}

	protected void setupHighlightMaterial() {
		/**
		 * @todo: implement this org.wilmascope.view.GraphElementView abstract
		 *        method
		 */
	}

	protected void setupDefaultMaterial() {
		Material material = new Material();
		material.setDiffuseColor(0.0f, 0.0f, 1.0f);
		material.setAmbientColor(0f, 0f, 0.4f);
		material.setShininess(50.0f);
		setupDefaultAppearance(material);
		getAppearance().setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, 0.3f));
	}

	/**
	 * curves is a vector of curves each curve is a vector of control points
	 * (integer)
	 */
	Vector curves, arrowPositions = new Vector();

	public Vector getCurves() {
		return curves;
	}

	public Vector getArrowPositions() {
		return arrowPositions;
	}

	float xScale;
	float yScale;

	public void setScale(float xScale, float yScale) {
		this.xScale = xScale;
		this.yScale = yScale;
	}

	public void copyCurves(org.wilmascope.dotlayout.Spline spline) {
		this.curves = spline.getCurves();
		this.arrowPositions = spline.getArrowPositions();
		this.xScale = spline.getXScale();
		this.yScale = spline.getYScale();
		bounds = spline.getBounds();
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public float getXScale() {
		return xScale;
	}

	public float getYScale() {
		return yScale;
	}

	public void setCurves(float xScale, float yScale, int x0, int y0, int x1, int y1, Vector curves,
			Vector arrowPositions) {
		this.curves = new Vector();
		this.arrowPositions = arrowPositions;
		bounds.x = x0;
		bounds.y = y0;
		this.xScale = xScale;
		this.yScale = yScale;
		bounds.width = x1 - x0;
		bounds.height = y1 - y0;
		for (Iterator i = curves.iterator(); i.hasNext();) {
			Vector curve = (Vector) i.next();
			Point2D.Float[] pnts = new Point2D.Float[curve.size()];
			for (int j = 0; j < pnts.length; j++) {
				java.awt.Point p = (java.awt.Point) curve.get(j);
				pnts[j] = new Point2D.Float(xScale * ((float) (p.x - bounds.x) / (float) bounds.width - 0.5f), yScale
						* ((float) (p.y - bounds.y) / (float) bounds.width - 0.5f));
			}
			this.curves.add(pnts);
		}
	}

	void splineTube(Point2D.Float[] controlPoints, BranchGroup b) {
		Point2D.Float[] t2pnts = new Point2D.Float[4];
		float radius = getRadius();
		int steps = 10;
		int numCurves = controlPoints.length / 3;
		int i = 0;
		zLevel = getEdge().getStart().getPosition().z;
		for (int n = 0; n < numCurves; n++) {
			Point3f[] taperedTubePoints = new Point3f[tubePoints.length];
			System.arraycopy(controlPoints, n * 3, t2pnts, 0, 4);
			float y = 0f;
			Vector2f v = new Vector2f(t2pnts[1].x - t2pnts[0].x, t2pnts[1].y - t2pnts[0].y);
			float cosTheta = -v.y / v.length();
			float sinTheta = (float) Math.sin(Math.acos(cosTheta));
			if (v.x > 0) {
				sinTheta *= -1;
			}
			if (Float.isNaN(cosTheta)) {
				cosTheta = 1.0f;
				sinTheta = 0f;
			}
			for (int segment = 0; segment < ySize; segment++) {
				// odd numbered points are in lower layer of segment,
				// even numbered points are in higher layer
				// want to visit lower layer, then higher layer
				for (int j = 1; true; j += 2) {
					if (j == segmentSize + 1) {
						j = 0;
						y += yStep;
						System.arraycopy(controlPoints, n * 3, t2pnts, 0, 4);
						if (y < 0.9999f) {
							SplineMethods.evalBezier(t2pnts, y);
							v.set(t2pnts[1].x - t2pnts[0].x, t2pnts[1].y - t2pnts[0].y);
						} else {
							v.set(t2pnts[3].x - t2pnts[2].x, t2pnts[3].y - t2pnts[2].y);
							SplineMethods.evalBezier(t2pnts, y);
						}
						cosTheta = -v.y / v.length();
						sinTheta = (float) Math.sin(Math.acos(cosTheta));
						if (Float.isNaN(cosTheta)) {
							cosTheta = 1.0f;
							sinTheta = 0f;
						}
						if (v.x > 0) {
							sinTheta *= -1;
						}
					} else if (j >= segmentSize) {
						break;
					}
					Point3f newPnt = new Point3f();
					// Point3f oldPnt = tubePoints[segment*segmentSize+j];
					Point3f oldPnt = new Point3f();
					if (j == 0 || j == 1 || j == 8 || j == 9) {
						oldPnt.x = 1;
						oldPnt.z = 1;
					}
					if (j == 2 || j == 3) {
						oldPnt.x = 1;
						oldPnt.z = -1;
					}
					if (j == 4 || j == 5) {
						oldPnt.x = -1;
						oldPnt.z = -1;
					}
					if (j == 6 || j == 7) {
						oldPnt.x = -1;
						oldPnt.z = 1;
					}
					newPnt.x = -oldPnt.x * cosTheta * radius + t2pnts[0].x;
					newPnt.y = t2pnts[0].y + oldPnt.x * radius * sinTheta;
					newPnt.z = oldPnt.z / 2f + zLevel;
					taperedTubePoints[segment * segmentSize + j] = newPnt;
				}
			}

			GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
			gi.setCoordinates(taperedTubePoints);
			gi.setStripCounts(tubeStripCounts);
			normalGenerator.generateNormals(gi);
			Shape3D tubeShape = new Shape3D(gi.getGeometryArray(), getAppearance());
			makePickable(tubeShape);
			b.addChild(tubeShape);
		}
	}

	public void draw2D(Renderer2D r, Graphics2D g, float transparency) {
		float thickness = 2f * r.scaleX(getRadius());
		g.setStroke(new BasicStroke(thickness));
		Color3f c = new Color3f();
		getAppearance().getMaterial().getDiffuseColor(c);
		g.setColor(new Color(c.x, c.y, c.z, transparency));
		GeneralPath p = new GeneralPath();
		for (Iterator i = curves.iterator(); i.hasNext();) {
			r.curvePath(p, (Point2D.Float[]) i.next());
		}
		g.draw(p);
		for (Iterator i = arrowPositions.iterator(); i.hasNext();) {
			Point3f end = new Point3f();
			Point3f start = new Point3f();
			arrowPosition((EdgeClient.ArrowPosition) i.next(), start, end);
			r.arrowPath(g, thickness / 4, start, end);
		}
	}

	private void arrowPosition(EdgeClient.ArrowPosition arrowPos, Point3f start, Point3f end) {
		end.set(xScale * (((float) (arrowPos.position.x - bounds.x)) / (float) bounds.width - 0.5f), yScale
				* (((float) (arrowPos.position.y - bounds.y)) / (float) bounds.width - 0.5f), zLevel);
		Point2D.Float p;
		Point2D.Float[] pnts = (Point2D.Float[]) curves.get(arrowPos.curveIndex);
		if (arrowPos.arrowAtStart) {
			p = pnts[0];
		} else {
			p = pnts[pnts.length - 1];
		}
		start.set(p.x, p.y, zLevel);
	}

	public void draw() {
		if (curves == null)
			return;
		BranchGroup b = new BranchGroup();
		int settings = GeometryArray.COORDINATES | GeometryArray.NORMALS;// |
																			// GeometryArray.COLOR_3;
		for (Iterator i = curves.iterator(); i.hasNext();) {
			splineTube((Point2D.Float[]) i.next(), b);

		}
		for (Iterator i = arrowPositions.iterator(); i.hasNext();) {
			Point3f end = new Point3f();
			Point3f start = new Point3f();
			arrowPosition((EdgeClient.ArrowPosition) i.next(), start, end);

			Cone cone = new Cone(2f * getRadius(), 1.0f, Cone.GENERATE_NORMALS, getAppearance());
			makePickable(cone.getShape(Cone.BODY));
			makePickable(cone.getShape(Cone.CAP));
			Transform3D transform = new Transform3D();
			Vector3f v = new Vector3f();
			v.sub(end, start);
			transform.setScale(new Vector3d(1, (double) v.length(), 1));
			Vector3f vtrans = new Vector3f(v);
			vtrans.scale(0.5f);
			vtrans.add(start);
			transform.setTranslation(vtrans);
			Vector3f yaxis = new Vector3f(0, 1f, 0);
			Vector3f norm = new Vector3f();
			if (v.x == 0 && v.z == 0) {
				if (v.y > 0) {
					norm.z = 1;
				} else {
					norm.z = -1;
				}
			} else {
				norm.cross(yaxis, v);
			}
			transform.setRotation(new AxisAngle4f(norm, yaxis.angle(v)));
			transform.setScale(new Vector3d(1d, 1d / 5d, 4d));
			TransformGroup coneTransform = new TransformGroup(transform);
			coneTransform.addChild(cone);
			b.addChild(coneTransform);
		}
		/*
		 * if(j!=0){ LineArray arrows = new
		 * LineArray(arrowCoords.length,settings); arrows.setCoordinates(0,
		 * arrowCoords); Shape3D s = new Shape3D(arrows, a); makePickable(s);
		 * b.addChild(s); }
		 */
		addLiveBranch(b);
	}

	public void init() {
	}

	/**
	 * draw the edge correctly between the start and end nodes
	 */
	public void draw2() {
		Edge e = getEdge();
		e.recalculate();
		double l = e.getLength();
		// avoids non-affine transformations, by making sure edge always has
		// non-zero length
		if (e.getLength() == 0) {
			e.setVector(ViewConstants.gc.getVector3f("MinVector"));
			l = e.getVector().length();
		}
		Vector3f v = new Vector3f(e.getVector());
		v.scaleAdd(0.5f, e.getStart().getPosition());
		setFullTransform(new Vector3d(getRadius(), l, getRadius()), v, getPositionAngle());
	}

	float zLevel;
	GeometryArray tubeGeometryArray;
	Point3f[] taperedTubePoints;

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("splineTubeEdge.png"));
	}
}
