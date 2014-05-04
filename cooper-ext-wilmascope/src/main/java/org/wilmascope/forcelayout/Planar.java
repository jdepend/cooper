package org.wilmascope.forcelayout;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import javax.vecmath.Vector3f;

import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeList;
import org.wilmascope.graph.Plane;

public class Planar extends Force {

	public Planar(float strength) {
		super(strength, "Planar");
	}

	public void setCluster(Cluster root) {
		this.root = root;
		nodes = root.getNodes();
	}

	public void calculate() {
		Plane p = nodes.getBestFitPlane();
		Vector3f f = new Vector3f();
		for (Node n : nodes) {
			// find f: force on the node back towards the plane and an equal and
			// opposite force back on the plane
			f.sub(p.getCentroid(), n.getPosition());
			float d = f.dot(p.getNormal());
			f.set(p.getNormal());
			f.scale(d);

			f.scale(6 * strengthConstant);
			NodeForceLayout l = (NodeForceLayout) n.getLayout();
			l.addForce(f);
		}
	}

	NodeList nodes;
	Cluster root;
	Vector3f normal = new Vector3f(0, 0, 1f);
}
