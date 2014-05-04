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

import java.util.Random;

import javax.media.j3d.BoundingBox;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeList;

/**
 * @author dwyer
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
class OctTree {
	private OctTreeCell root;

	private Random rand = new Random(System.currentTimeMillis());

	public OctTree(BoundingBox bBox) {
		root = new OctTreeCell(bBox);
	}

	public void addNode(Node node, OctTreeCell cell) {
		if (cell.isEmpty()) {
			cell.contents = node;
			cell.centreOfMass.set(node.getPosition());
			cell.count = 1;
			cell.setStatusFull();
			return;
		}

		if (cell.isFull()) {
			cell.setStatusSplit();
			Node movingNode = cell.contents;
			cell.contents = null;
			cell.count = 0;
			cell.centreOfMass.set(0.0f, 0.0f, 0.0f);

			addNode(movingNode, cell);
			if (node.getPosition().equals(movingNode.getPosition())) {
				Point3d boxCentre = new Point3d();
				Point3d lower = new Point3d();
				cell.bBox.getLower(lower);
				Point3d upper = new Point3d();
				cell.bBox.getUpper(upper);
				boxCentre.add(lower, upper);
				boxCentre.scale(0.5f);
				Point3f bc = new Point3f(boxCentre);

				Point3f moveOff = new Point3f(rand.nextFloat() * (root.width / 2.0f) - (root.width / 4.0f),
						rand.nextFloat() * (root.width / 2.0f) - (root.width / 4.0f), rand.nextFloat()
								* (root.width / 2.0f) - (root.width / 4.0f));
				node.setPosition(moveOff);
				addNode(node, root);
			} else {
				addNode(node, cell);
			}

			return;
		}

		if (cell.isSplit()) {
			cell.centreOfMass.x = ((((float) cell.count) * cell.centreOfMass.x) + node.getPosition().x)
					/ (((float) cell.count) + 1.0f);
			cell.centreOfMass.y = ((((float) cell.count) * cell.centreOfMass.y) + node.getPosition().y)
					/ (((float) cell.count) + 1.0f);
			cell.centreOfMass.z = ((((float) cell.count) * cell.centreOfMass.z) + node.getPosition().z)
					/ (((float) cell.count) + 1.0f);

			++cell.count;

			int index = indexOfDaughters(cell, node);

			if (cell.daughters[index] == null) {
				BoundingBox bBox = boundBoxOfOctTant(cell, index);
				cell.daughters[index] = new OctTreeCell(bBox);
			}
			addNode(node, cell.daughters[index]);
		}
	}

	public OctTreeCell root() {
		return root;
	}

	private int indexOfDaughters(OctTreeCell cell, Node node) {
		Point3d boxCentre = new Point3d();
		Point3d lower = new Point3d();
		cell.bBox.getLower(lower);
		Point3d upper = new Point3d();
		cell.bBox.getUpper(upper);
		boxCentre.add(lower, upper);
		boxCentre.scale(0.5f);
		Point3f bc = new Point3f(boxCentre);
		Point3f nc = node.getPosition();

		if (nc.x <= bc.x && nc.y > bc.y && nc.z > bc.z)
			return 0;
		if (nc.x > bc.x && nc.y > bc.y && nc.z > bc.z)
			return 1;
		if (nc.x <= bc.x && nc.y <= bc.y && nc.z > bc.z)
			return 2;
		if (nc.x > bc.x && nc.y <= bc.y && nc.z > bc.z)
			return 3;
		if (nc.x <= bc.x && nc.y > bc.y && nc.z <= bc.z)
			return 4;
		if (nc.x > bc.x && nc.y > bc.y && nc.z <= bc.z)
			return 5;
		if (nc.x <= bc.x && nc.y <= bc.y && nc.z <= bc.z)
			return 6;
		// if (nc.x > bc.x && nc.y <= bc.y && nc.z <= bc.z)
		return 7;
	}

	private BoundingBox boundBoxOfOctTant(OctTreeCell cell, int octTant) {
		Point3d lower = new Point3d();
		cell.bBox.getLower(lower);
		Point3d upper = new Point3d();
		cell.bBox.getUpper(upper);

		double newCellSize = (double) cell.width / 2.0;
		Point3d lMove = new Point3d();
		Point3d uMove = new Point3d();

		if (octTant == 0) {
			lMove.set(0.0, 1.0, 1.0);
			uMove.set(-1.0, 0.0, 0.0);
		}
		if (octTant == 1) {
			lMove.set(1.0, 1.0, 1.0);
			uMove.set(0.0, 0.0, 0.0);
		}
		if (octTant == 2) {
			lMove.set(0.0, 0.0, 1.0);
			uMove.set(-1.0, -1.0, 0.0);
		}
		if (octTant == 3) {
			lMove.set(1.0, 0.0, 1.0);
			uMove.set(0.0, -1.0, 0.0);
		}
		if (octTant == 4) {
			lMove.set(0.0, 1.0, 0.0);
			uMove.set(-1.0, 0.0, -1.0);
		}
		if (octTant == 5) {
			lMove.set(1.0, 1.0, 0.0);
			uMove.set(0.0, 0.0, -1.0);
		}
		if (octTant == 6) {
			lMove.set(0.0, 0.0, 0.0);
			uMove.set(-1.0, -1.0, -1.0);
		}
		if (octTant == 7) {
			lMove.set(1.0, 0.0, 0.0);
			uMove.set(0.0, -1.0, -1.0);
		}

		lMove.scale(newCellSize);
		uMove.scale(newCellSize);

		lMove.add(lower);
		uMove.add(upper);

		return new BoundingBox(lMove, uMove);
	}
}

class OctTreeCell {
	private static final int empty = 0;

	private static final int full = 1;

	private static final int split = 2;

	public BoundingBox bBox;

	public Node contents;

	private int status;

	public int count;

	public OctTreeCell[] daughters;

	public Point3f centreOfMass;

	public float width;

	public OctTreeCell(BoundingBox bBox) {
		this.bBox = bBox;

		status = empty;
		count = 0;

		daughters = new OctTreeCell[8];

		centreOfMass = new Point3f();

		Point3d lower = new Point3d();
		bBox.getLower(lower);
		Point3d upper = new Point3d();
		bBox.getUpper(upper);

		width = (float) (Math.abs(upper.x - lower.x));
	}

	public boolean isEmpty() {
		return status == empty;
	}

	public void setStatusEmpty() {
		status = empty;
	}

	public boolean isFull() {
		return status == full;
	}

	public void setStatusFull() {
		status = full;
	}

	public boolean isSplit() {
		return status == split;
	}

	public void setStatusSplit() {
		status = split;
	}

	public boolean isLeaf() {
		return isFull();
	}
}

public class RepulsionFADE extends Force {

	public RepulsionFADE(float strengthConstant) {
		super(strengthConstant, "FADE Repulsion");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.forcelayout.Force#calculate()
	 */
	public void calculate() {
		NodeList nodes = rootCluster.getNodes();
		// Calculate Bounding Box for nodes
		double max = 1.0;
		for (Node n : nodes) {
			double coord = (double) Math.abs(n.getPosition().x);
			if (coord > max)
				max = coord;
			coord = Math.abs(n.getPosition().y);
			if (coord > max)
				max = coord;
			coord = Math.abs(n.getPosition().z);
			if (coord > max)
				max = coord;
		}

		Point3d lower = new Point3d(-max, -max, -max);
		Point3d upper = new Point3d(max, max, max);
		BoundingBox bBox = new BoundingBox(lower, upper);

		OctTree tree = new OctTree(bBox);
		for (Node n : nodes) {
			tree.addNode(n, tree.root());
		}

		for (Node n : nodes) {
			approxForce(n, tree.root());
		}
	}

	private void approxForce(Node node, OctTreeCell cell) {

		Vector3f repelForce = new Vector3f();
		repelForce.sub(node.getPosition(), cell.centreOfMass);
		float distance = repelForce.length();
		float M = 0.0f;
		if (distance != 0) {
			switch (openingCriterion) {
			case BARNES_HUT:
				M = cell.width / distance;
				break;
			case NEW_BARNES_HUT:
				Point3d boxCentre = new Point3d();
				Point3d lower = new Point3d();
				cell.bBox.getLower(lower);
				Point3d upper = new Point3d();
				cell.bBox.getUpper(upper);
				boxCentre.add(lower, upper);
				boxCentre.scale(0.5f);
				Point3f bc = new Point3f(boxCentre);

				Vector3f C = new Vector3f();
				C.sub(cell.centreOfMass, bc);

				M = cell.width / (distance - C.lengthSquared());
				break;
			case ORTHOGONAL_DISTANCE: {
				float V = Math.max(Math.abs(repelForce.x), Math.abs(repelForce.y));
				V = Math.max(V, Math.abs(repelForce.z));

				M = cell.width / (V - 0.5f * cell.width);
				break;
			}
			}
			if (M <= accuracyParameter || cell.isLeaf()) {
				float repelFactor = ((float) cell.count) * strengthConstant / (distance * distance);

				repelForce.scale(repelFactor);

				((NodeForceLayout) (node.getLayout())).addForce(repelForce);
			} else {
				for (int i = 0; i != 8; ++i) {
					if (cell.daughters[i] != null) {
						approxForce(node, cell.daughters[i]);
					}
				}

			}
		}
	}

	public void setCluster(Cluster root) {
		this.rootCluster = root;
	}

	public final static int BARNES_HUT = 1;

	public final static int NEW_BARNES_HUT = 2;

	public final static int ORTHOGONAL_DISTANCE = 3;

	float accuracyParameter = 0.7f;
	int openingCriterion = BARNES_HUT;
	Cluster rootCluster;
}
