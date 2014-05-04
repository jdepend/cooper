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
package org.wilmascope.graph;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 * A plane defined by a normal vector and a point in the plane
 * 
 * @author dwyer
 */
public class Plane {
	Point3f centroid;

	Vector3f normal;

	SingularValueDecomposition svd;

	public Plane(Point3f centroid, Vector3f normal) {
		this.centroid = centroid;
		this.normal = normal;
	}

	/**
	 * Creates a plane of best fit for the nodes using orthogonal regression
	 * 
	 * @param nodes
	 */
	public Plane(NodeList nodes) {
		centroid = nodes.getBarycenter();
		normal = new Vector3f(0, 1, 0);
		if (nodes.size() > 1) {
			Matrix M = new Matrix(nodes.size(), 3);
			int i = 0;
			for (Node n : nodes) {
				Point3f p = n.getPosition();
				Point3f pc = new Point3f();
				pc.sub(p, centroid);
				M.set(i, 0, pc.x);
				M.set(i, 1, pc.y);
				M.set(i++, 2, pc.z);
			}
			// plane of best fit
			svd = M.svd();
			Matrix V = svd.getV();
			normal = new Vector3f((float) V.get(0, 2), (float) V.get(1, 2), (float) V.get(2, 2));
		}
		// System.out.println("Single values: "+new
		// Vector3d(getSingularValues()));
	}

	public double[] getSingularValues() {
		if (svd == null)
			return new double[] { 0, 0, 0 };
		else
			return svd.getSingularValues();
	}

	/**
	 * @return Returns the centroid.
	 */
	public Point3f getCentroid() {
		return centroid;
	}

	/**
	 * @return Returns the normal.
	 */
	public Vector3f getNormal() {
		return normal;
	}
}
