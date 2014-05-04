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

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * @author dwyer
 * 
 *         Given the corners of the 3D bounding box given to the constructor,
 *         this class defines methods for converting points to a 2D window
 */
public class Renderer2D {
	Point3f bottomLeft, topRight;
	int screenWidth, screenHeight, xBorder, yBorder;
	float modelWidth, modelHeight, xScale, yScale;

	/**
	 * @param bottomLeft
	 *            bottom left corner of bounding box
	 * @param topRight
	 *            top right corner of bounding box
	 * @param w
	 *            width of target screen
	 * @param h
	 *            height of target screen
	 */
	public Renderer2D(Point3f bottomLeft, Point3f topRight, int w, int h) {
		this.bottomLeft = bottomLeft;
		this.topRight = topRight;
		xBorder = w / 4;
		yBorder = h / 10;
		this.screenWidth = w - 2 * xBorder;
		this.screenHeight = h - 2 * yBorder;
		modelWidth = topRight.x - bottomLeft.x;
		modelHeight = topRight.y - bottomLeft.y;
		xScale = screenWidth / modelWidth;
		yScale = screenHeight / modelHeight;
	}

	/**
	 * Converts a 3D point to a 2D point in the window of specified size
	 * 
	 * @param p3f
	 *            3D Point to render to the window
	 * @return the converted 2D point
	 */
	public Point2f getScreenPoint(Point3f p3f) {
		return getScreenPoint(p3f.x, p3f.y);
	}

	/**
	 * Converts a 2D float point to a 2D point in the window of specified size
	 * 
	 * @param p2f
	 *            2D float Point to render to the window
	 * @return the converted 2D point
	 */
	public Point2f getScreenPoint(Point2D.Float p2f) {
		return getScreenPoint(p2f.x, p2f.y);
	}

	/**
	 * Converts a x and y floats to a 2D point in the window of specified size
	 * 
	 * @param x
	 *            horizontal float coord to render to the window
	 * @param y
	 *            vertical float coord to render to the window
	 * @return the converted 2D point
	 */
	public Point2f getScreenPoint(float x, float y) {
		Point2f p = new Point2f();
		p.x = xBorder + ((float) screenWidth * (x - bottomLeft.x) / modelWidth);
		p.y = yBorder + screenHeight - ((float) screenHeight * (y - bottomLeft.y) / modelHeight);
		return p;
	}

	/**
	 * Draws a curve in the 2D window
	 * 
	 * @param path
	 *            add the curve to this path
	 * @param p2f
	 *            array of points (will be converted to screen points)
	 */
	public void curvePath(GeneralPath path, Point2D.Float[] p2f) {
		Point2f p1, p2, p3, p = getScreenPoint(p2f[0]);
		path.moveTo(p.x, p.y);
		int i = 1;
		while (i < p2f.length) {
			p1 = getScreenPoint(p2f[i++]);
			p2 = getScreenPoint(p2f[i++]);
			p3 = getScreenPoint(p2f[i++]);
			path.curveTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
		}
	}

	public void fillSquare(Graphics2D g, Point3f pos, float size) {
		fillRect(g, pos, size, size);
	}

	public void fillRect(Graphics2D g, Point3f pos, float width, float height) {
		Point2f p = getScreenPoint(pos);
		int rx = (int) scaleX(width);
		int ry = (int) scaleY(height);
		g.fillRect((int) p.x - rx, (int) p.y - ry, rx * 2, ry * 2);
	}

	public void drawRect(Graphics2D g, Point3f pos, float width, float height) {
		Point2f p = getScreenPoint(pos);
		int rx = (int) scaleX(width);
		int ry = (int) scaleY(height);
		g.drawRect((int) p.x - rx, (int) p.y - ry, rx * 2, ry * 2);
	}

	public void fillCircle(Graphics2D g, Point3f pos, float size) {
		Point2f p = getScreenPoint(pos);
		int rx = (int) scaleX(size);
		int ry = (int) scaleY(size);
		g.fillOval((int) p.x - rx, (int) p.y - ry, rx * 2, ry * 2);
	}

	public void linePath(Graphics2D g, Point3f start, Point3f end) {
		Point2f p1 = getScreenPoint(start);
		Point2f p2 = getScreenPoint(end);
		g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
	}

	/**
	 * Draws an arrow head (a triangle) with the base at start and the head at
	 * end
	 * 
	 * @param g
	 * @param width
	 * @param start
	 * @param end
	 */
	public void arrowPath(Graphics2D g, float width, Point3f start, Point3f end) {
		// v is vector from start to end
		Vector3f v = new Vector3f();
		v.sub(end, start);
		// w is perpendicular to v
		Vector3f w = new Vector3f(v.y, -v.x, 0);
		w.normalize();
		w.scale(width / 2f);
		// a and b are corners of arrow head
		Point3f a = new Point3f();
		Point3f b = new Point3f();
		a.sub(start, w);
		b.add(start, w);
		Point2f p1 = getScreenPoint(a);
		Point2f p2 = getScreenPoint(b);
		Point2f p3 = getScreenPoint(end);
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 5);
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		path.lineTo(p1.x, p1.y);
		path.closePath();
		g.fill(path);
	}

	/**
	 * Draws a tapered line from start to end with end widths as specified
	 * 
	 * @param g
	 * @param startWidth
	 * @param endWidth
	 * @param start
	 * @param end
	 */
	public void taperedLinePath(Graphics2D g, float startWidth, float endWidth, Point3f start, Point3f end) {
		// v is vector from start to end
		Vector3f v = new Vector3f();
		v.sub(end, start);
		// w is perpendicular to v
		Vector3f wS = new Vector3f(-v.y, v.x, 0);
		wS.normalize();
		Vector3f wE = new Vector3f(wS);
		wS.scale(startWidth / 2f);
		wE.scale(endWidth / 2f);
		Point3f a = new Point3f();
		Point3f b = new Point3f();
		Point3f c = new Point3f();
		Point3f d = new Point3f();
		a.sub(start, wS);
		b.add(start, wS);
		c.add(end, wE);
		d.sub(end, wE);
		Point2f p1 = getScreenPoint(a);
		Point2f p2 = getScreenPoint(b);
		Point2f p3 = getScreenPoint(c);
		Point2f p4 = getScreenPoint(d);
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 5);
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		path.lineTo(p4.x, p4.y);
		path.lineTo(p1.x, p1.y);
		path.closePath();
		g.fill(path);
	}

	/**
	 * scales a value such as a radius to fit the screen horizontal
	 * 
	 * @param x
	 *            value to scale
	 * @return scaled value
	 */
	public float scaleX(float x) {
		return x * xScale;
	}

	/**
	 * scales a value such as a radius to fit the screen vertical
	 * 
	 * @param x
	 *            value to scale
	 * @return scaled value
	 */
	public float scaleY(float y) {
		return y * yScale;
	}
}
