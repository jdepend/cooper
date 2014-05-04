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

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class LODCylinder {
	Switch sw;
	DistanceLOD lod;
	Cylinder one, two, three;

	public LODCylinder(float radius, float length, Appearance appearance) {
		this(radius, length, appearance, new float[] { 5f, 10f });
	}

	public LODCylinder(float radius, float length, Appearance appearance, float[] distances) {
		sw = new Switch(0);
		sw.setCapability(javax.media.j3d.Switch.ALLOW_SWITCH_READ);
		sw.setCapability(javax.media.j3d.Switch.ALLOW_SWITCH_WRITE);

		// Create several levels for the switch, with less detailed
		// cylinders for the ones which will be used when the edge is
		// further away
		one = new Cylinder(radius, length, Cylinder.GENERATE_NORMALS, 10, 1, appearance);
		two = new Cylinder(radius, length, Cylinder.GENERATE_NORMALS, 6, 1, appearance);
		three = new Cylinder(radius, length, Cylinder.GENERATE_NORMALS, 3, 1, appearance);

		sw.addChild(one);
		sw.addChild(two);
		sw.addChild(three);

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
		// Bizarrely when you only have 1 segment along the height of the
		// cylinder you have to set the picking capability bits for top, bottom
		// and body just to pick it from the side
		ge.makePickable(one.getShape(Cylinder.BOTTOM));
		ge.makePickable(two.getShape(Cylinder.BOTTOM));
		ge.makePickable(three.getShape(Cylinder.BOTTOM));
		ge.makePickable(one.getShape(Cylinder.TOP));
		ge.makePickable(two.getShape(Cylinder.TOP));
		ge.makePickable(three.getShape(Cylinder.TOP));
		ge.makePickable(one.getShape(Cylinder.BODY));
		ge.makePickable(two.getShape(Cylinder.BODY));
		ge.makePickable(three.getShape(Cylinder.BODY));
	}
}
