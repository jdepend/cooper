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

import java.awt.geom.Point2D;

/**
 * @author dwyer
 * 
 *         Methods for drawing spline curves
 */
public class SplineMethods {
	/**
	 * This method and the next together implement The de-Casteljau Algorithm.
	 * I've modelled them on the example code at: <a
	 * href="http://www.cs.huji.ac.il/~arik/java/ex2/">this site</a> Thanks!
	 * 
	 * @return the linear interpolation of two points
	 */
	public static Point2D.Float interpolate(Point2D.Float p0, Point2D.Float p1, float t) {
		return new Point2D.Float(t * p1.x + (1 - t) * p0.x, t * p1.y + (1 - t) * p0.y);
	}

	/**
	 * evaluates a bezier defined by the control polygon which points are given
	 * in the array at the value t returns the point on the curve (which is also
	 * returned in the first point in the input array, the line from that point
	 * to the second point in the array gives a tangent vector)
	 */
	public static Point2D.Float evalBezier(Point2D.Float[] arr, float t) {
		for (int iter = arr.length; iter > 0; iter--) {
			for (int i = 1; i < iter; i++) {
				arr[i - 1] = interpolate(arr[i - 1], arr[i], t);
			}
		}
		return arr[0];
	}
}
