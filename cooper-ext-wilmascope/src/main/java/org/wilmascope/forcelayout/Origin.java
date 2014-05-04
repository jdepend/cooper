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
package org.wilmascope.forcelayout;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeList;

/*
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular Wilma graph drawing engine
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaOrg
 * @author Tim Dwyer
 * @version 1.0
 */

/**
 * Force that attracts either one or all of the nodes to the centre of the graph
 * space. Simply keeps the graph centred, stops it drifting off the screen.
 */
public class Origin extends Force {

	public Origin(float strengthConstant) {
		super(strengthConstant, "Origin");
		this.centreNode = null;
		originPoint = new Point3f(Constants.vZero);
	}

	public void setCluster(Cluster root) {
		setOriginNode(root);
		nodes = root.getNodes();
	}

	// calculate the origin force on an individual node
	private void calculateNode(Node node) {
		Vector3f vOrigin = new Vector3f();
		vOrigin.sub(node.getPosition(), originPoint);
		Vector3f originForce = new Vector3f();
		float r = vOrigin.length();
		// unclipped 1/r (ie strengthConstant / r * r) forces are chaotic!
		originForce.scale(-strengthConstant, vOrigin);
		// need the force to drop off near the origin or else oscillation ensues
		// if (r < 1) originForce.scale( r );
		((NodeForceLayout) node.getLayout()).addForce(originForce);
		if (originNodeLayout != null) {
			originNodeLayout.subForce(originForce);
		}
	}

	/**
	 * set the origin point
	 * 
	 * @param originPoint
	 *            the centroid!
	 */
	public void setOriginNode(Node originNode) {
		originPoint = originNode.getPosition();
		originNodeLayout = (NodeForceLayout) originNode.getLayout();
	}

	/**
	 * Calculate the origin centering deltas for all the nodes, or if a
	 * CentreNode has been specified then just for that centre node.
	 * 
	 * @param nodes
	 *            Nodes to which this force applies
	 */
	public void calculate() {
		if (centreNode != null) {
			calculateNode(centreNode);
		} else {
			for (int i = 0; i < nodes.size(); i++) {
				Node node = nodes.get(i);
				calculateNode(node);
			}
		}
	}

	/**
	 * Node specified will be drawn to the origin.
	 * 
	 * @param node
	 *            Node around which the graph will be centred.
	 */
	public void setCentreNode(Node node) {
		centreNode = node;
	}

	// if centreNode is set then that node is drawn to the origin
	// otherwise whole graph is attracted to the origin and thus centred
	private Node centreNode;
	private Point3f originPoint = new Point3f();
	private NodeForceLayout originNodeLayout;
	private NodeList nodes;
}
