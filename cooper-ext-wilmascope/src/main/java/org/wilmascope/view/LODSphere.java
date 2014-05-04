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

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.DistanceLOD;
import javax.media.j3d.Switch;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * Uses DistanceLOD to define spheres at various levels of detail. Number of
 * faces that appear depends on distance from viewer.
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class LODSphere {
	Switch sw;
	DistanceLOD lod;
	Sphere one, two, three, four;
	static int sharedGeometry;
	{
		sharedGeometry = ViewConstants.getInstance().getIntValue("SharedGeometry") != 0 ? 0
				: Sphere.GEOMETRY_NOT_SHARED;
	}

	public LODSphere(float radius, Appearance appearance) {
		this(radius, appearance, new float[] { 20f, 40f, 80f });
	}

	public LODSphere(float radius, Appearance appearance, float[] distances) {
		sw = new Switch(0);
		sw.setCapability(javax.media.j3d.Switch.ALLOW_SWITCH_READ);
		sw.setCapability(javax.media.j3d.Switch.ALLOW_SWITCH_WRITE);

		// Create several levels for the switch, with less detailed
		// spheres for the ones which will be used when the sphere is
		// further away
		one = new Sphere(radius, Sphere.GENERATE_NORMALS | sharedGeometry, 40, appearance);
		two = new Sphere(radius, Sphere.GENERATE_NORMALS | sharedGeometry, 20, appearance);
		three = new Sphere(radius, Sphere.GENERATE_NORMALS | sharedGeometry, 10, appearance);
		four = new Sphere(radius, Sphere.GENERATE_NORMALS | sharedGeometry, 5, appearance);
		sw.addChild(one);
		sw.addChild(two);
		sw.addChild(three);
		sw.addChild(four);

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);

		// set up the DistanceLOD behavior
		lod = new DistanceLOD(distances);
		lod.addSwitch(sw);
		lod.setSchedulingBounds(bounds);
	}

	public void addToTransformGroup(TransformGroup tg) {
		tg.addChild(sw);
		tg.addChild(lod);
	}

	public void makePickable(GraphElementView ge) {
		ge.makePickable(one.getShape());
		ge.makePickable(two.getShape());
		ge.makePickable(three.getShape());
		ge.makePickable(four.getShape());
	}
}
