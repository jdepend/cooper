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

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Properties;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryStripArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TriangleFanArray;
import javax.media.j3d.TriangleStripArray;
import javax.swing.ImageIcon;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.wilmascope.view.Colours;
import org.wilmascope.view.NodeView;
import org.wilmascope.view.Renderer2D;
import org.wilmascope.view.SizeAdjustableNodeView;
import org.wilmascope.view.View2D;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

/**
 * A tube with square cross section and specified bottom and top dimensions
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class SquareTubeNodeView extends NodeView implements SizeAdjustableNodeView, View2D {
	//
	// create the basic reference geometries from the shape sections of a
	// cylinder
	//
	static int segments = 4;
	static Point3f[] tubePoints;
	static int[] tubeStripCounts;
	static Point3f[] topPoints;
	static int[] topStripCounts;
	static Point3f[] bottomPoints;
	static int[] bottomStripCounts;
	static NormalGenerator normalGenerator = new NormalGenerator();
	{
		Cylinder c = new Cylinder(1f, 1f, 0, segments, 1, null);
		GeometryStripArray tubeGeometry = getGeometry(c, Cylinder.BODY);
		GeometryStripArray topGeometry = getGeometry(c, Cylinder.TOP);
		GeometryStripArray bottomGeometry = getGeometry(c, Cylinder.BOTTOM);
		tubePoints = new Point3f[tubeGeometry.getVertexCount()];
		topPoints = new Point3f[topGeometry.getVertexCount()];
		bottomPoints = new Point3f[bottomGeometry.getVertexCount()];
		tubeStripCounts = new int[tubeGeometry.getNumStrips()];
		topStripCounts = new int[topGeometry.getNumStrips()];
		bottomStripCounts = new int[bottomGeometry.getNumStrips()];
		loadGeometry(tubeGeometry, tubeStripCounts, tubePoints);
		loadGeometry(topGeometry, topStripCounts, topPoints);
		loadGeometry(bottomGeometry, bottomStripCounts, bottomPoints);
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
		// rotate so that cylinder stands lengthwise in the z-dimension
		Transform3D rotateTransform = new Transform3D();
		rotateTransform.rotX(Math.PI / 2);
		for (int i = 0; i < points.length; i++) {
			rotateTransform.transform(points[i]);
		}
	}

	public void draw() {
		Transform3D t = new Transform3D();
		t.setTranslation(new Vector3f(getNode().getPosition()));
		t.setRotation(new AxisAngle4d(0, 0, 1, Math.PI / 4));
		setTransformGroupTransform(t);
	}

	public void draw2D(Renderer2D r, Graphics2D g, float transparency) {
		Color3f c = new Color3f();
		getAppearance().getMaterial().getDiffuseColor(c);
		g.setColor(new Color(c.x, c.y, c.z, transparency));
		r.fillSquare(g, getNode().getPosition(), getRadius());
	}

	public SquareTubeNodeView() {
		setTypeName("SquareTube");
	}

	protected void setupDefaultMaterial() {
		setupDefaultAppearance(Colours.defaultMaterial);
		getAppearance().setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		getAppearance().setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
	}

	protected void setupHighlightMaterial() {
		setupHighlightAppearance(Colours.yellowMaterial);
	}

	protected void init() {
		float radius = getRadius();
		taperedTubePoints = new Point3f[tubePoints.length];
		scaledTopPoints = new Point3f[topPoints.length];
		for (int i = 0; i < topPoints.length; i++) {
			scaledTopPoints[i] = new Point3f(topPoints[i].x * radius, topPoints[i].y * radius, topPoints[i].z);
		}

		scaledBottomPoints = new Point3f[bottomPoints.length];
		for (int i = 0; i < bottomPoints.length; i++) {
			scaledBottomPoints[i] = new Point3f(bottomPoints[i].x * radius, bottomPoints[i].y * radius,
					bottomPoints[i].z);
		}
		for (int i = 0; i < tubePoints.length; i++) {
			if (i % 2 == 0) {
				taperedTubePoints[i] = new Point3f(tubePoints[i].x * radius, tubePoints[i].y * radius, tubePoints[i].z);
			} else {
				taperedTubePoints[i] = new Point3f(tubePoints[i].x * radius, tubePoints[i].y * radius, tubePoints[i].z);
			}
		}
		GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
		gi.setCoordinates(taperedTubePoints);
		gi.setStripCounts(tubeStripCounts);
		normalGenerator.generateNormals(gi);
		tubeGeometryArray = new TriangleStripArray(taperedTubePoints.length, GeometryArray.COORDINATES
				| GeometryArray.BY_REFERENCE | GeometryArray.NORMALS, tubeStripCounts);
		tubeGeometryArray.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		tubeGeometryArray.setCoordRef3f(gi.getCoordinates());
		tubeGeometryArray.setNormalRef3f(gi.getNormals());
		Shape3D tubeShape = new Shape3D(tubeGeometryArray, getAppearance());
		makePickable(tubeShape);
		addTransformGroupChild(tubeShape);
		// cap on the tube
		gi = new GeometryInfo(GeometryInfo.TRIANGLE_FAN_ARRAY);
		gi.setCoordinates(scaledTopPoints);
		gi.setStripCounts(topStripCounts);
		normalGenerator.generateNormals(gi);
		topGeometryArray = new TriangleFanArray(scaledTopPoints.length, GeometryArray.COORDINATES
				| GeometryArray.BY_REFERENCE | GeometryArray.NORMALS, topStripCounts);
		topGeometryArray.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		topGeometryArray.setCoordRef3f(gi.getCoordinates());
		topGeometryArray.setNormalRef3f(gi.getNormals());
		Shape3D topShape = new Shape3D(topGeometryArray, getAppearance());
		makePickable(topShape);
		addTransformGroupChild(topShape);

		// cap on the tube
		gi = new GeometryInfo(GeometryInfo.TRIANGLE_FAN_ARRAY);
		gi.setCoordinates(scaledBottomPoints);
		gi.setStripCounts(bottomStripCounts);
		normalGenerator.generateNormals(gi);
		bottomGeometryArray = new TriangleFanArray(scaledBottomPoints.length, GeometryArray.COORDINATES
				| GeometryArray.BY_REFERENCE | GeometryArray.NORMALS, bottomStripCounts);
		bottomGeometryArray.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		bottomGeometryArray.setCoordRef3f(gi.getCoordinates());
		bottomGeometryArray.setNormalRef3f(gi.getNormals());
		Shape3D bottomShape = new Shape3D(bottomGeometryArray, getAppearance());
		makePickable(bottomShape);
		addTransformGroupChild(bottomShape);
	}

	public ImageIcon getIcon() {
		return new ImageIcon(org.wilmascope.images.Images.class.getResource("column.png"));
	}

	/**
	 * adjust the radius of the top and bottom of the tube each argument is a
	 * xScale factor, ie the resulting radius will be the argument * node radius
	 * 
	 * @parameter bottomRadius bottom radius xScale factor
	 * @parameter topRadius top radius xScale factor
	 */
	public void setEndRadii(final float bottomRadius, final float topRadius) {
		tubeGeometryArray.updateData(new GeometryUpdater() {
			public void updateData(Geometry blah) {
				for (int i = 0; i < taperedTubePoints.length; i++) {
					if (i % 2 == 0) {
						taperedTubePoints[i].x *= topRadius;
						taperedTubePoints[i].y *= topRadius;
					} else {
						taperedTubePoints[i].x *= bottomRadius;
						taperedTubePoints[i].y *= bottomRadius;
					}
				}
			}
		});
		topGeometryArray.updateData(new GeometryUpdater() {
			public void updateData(Geometry blah) {
				for (int i = 0; i < topPoints.length; i++) {
					scaledTopPoints[i].x *= topRadius;
					scaledTopPoints[i].y *= topRadius;
				}
			}
		});

		bottomGeometryArray.updateData(new GeometryUpdater() {
			public void updateData(Geometry blah) {
				for (int i = 0; i < bottomPoints.length; i++) {
					scaledBottomPoints[i].x *= bottomRadius;
					scaledBottomPoints[i].y *= bottomRadius;
				}
			}
		});
		this.topRadius = topRadius;
		this.bottomRadius = bottomRadius;
		setRadius(getRadius() * (topRadius + bottomRadius) / 2f);
	}

	public void setHeight(final float height) {
		tubeGeometryArray.updateData(new GeometryUpdater() {
			public void updateData(Geometry blah) {
				for (int i = 0; i < taperedTubePoints.length; i++) {
					taperedTubePoints[i].z *= height;
				}
			}
		});
		topGeometryArray.updateData(new GeometryUpdater() {
			public void updateData(Geometry blah) {
				for (int i = 0; i < topPoints.length; i++) {
					scaledTopPoints[i].z *= height;
				}
			}
		});

		bottomGeometryArray.updateData(new GeometryUpdater() {
			public void updateData(Geometry blah) {
				for (int i = 0; i < bottomPoints.length; i++) {
					scaledBottomPoints[i].z *= height;
				}
			}
		});
	}

	public void setProperties(Properties p) {
		super.setProperties(p);
		float topRadius = Float.parseFloat(p.getProperty("TopRadius"));
		float bottomRadius = Float.parseFloat(p.getProperty("BottomRadius"));
		setEndRadii(bottomRadius, topRadius);
	}

	public Properties getProperties() {
		Properties p = super.getProperties();
		p.setProperty("BottomRadius", "" + bottomRadius);
		p.setProperty("TopRadius", "" + topRadius);
		return p;
	}

	public float getBottomRadius() {
		return bottomRadius;
	}

	public float getTopRadius() {
		return topRadius;
	}

	public float getDepth() {
		return topRadius;
	}

	public int getShape() {
		return BOX;
	}

	Point3f[] taperedTubePoints;
	Point3f[] scaledTopPoints;
	Point3f[] scaledBottomPoints;
	GeometryArray tubeGeometryArray;
	GeometryArray topGeometryArray;
	GeometryArray bottomGeometryArray;
	float topRadius, bottomRadius;
}
