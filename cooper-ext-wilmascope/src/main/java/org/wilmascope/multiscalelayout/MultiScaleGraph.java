package org.wilmascope.multiscalelayout;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * $Id: MultiScaleGraph.java,v 1.2 2004/04/23 08:35:21 tgdwyer Exp $
 * </p>
 * <p>
 * 
 * @author</p>
 *         <p>
 * @version $Revision: 1.2 $
 *          </p>
 *          unascribed
 *  
 */
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.vecmath.Point3i;
import javax.vecmath.Vector3f;

/**
 * Grid provides a matrix of node arrays, where each node array provides a list
 * of nodes located in a particular region of space (a cell). Thus the nodes in
 * the neighbourhood of a particular node can be easily found as the contents of
 * the arrays in the neighbouring cells. Originally there was a 2D
 * implementation of the following interface as well as the 3D. Now it's
 * redundant but I'll leave it here in case someone feels the need to
 * reimplement a super efficient 2D version again.
 */
interface Grid {
	/**
	 * apply the specified force to the nodes in the neighbourhood around v
	 */
	public void applyForceToNeighbourhood(Force f, MultiScaleNodeLayout v);

	public Vector3f getMaxPoint();

	public Vector3f getMinPoint();

	/**
	 * if the node positions needed to be scaled to fit the grid then the scale
	 * factor used can be obtained with this method.
	 */
	public float getScale();
}

class Grid3D implements Grid {
	Object array[][][];
	Point3i maxCell = new Point3i();
	Vector3f min, max, scale;

	public Vector3f getMaxPoint() {
		return max;
	}

	public float getScale() {
		float s = scale.x;
		if (scale.y > s) {
			s = scale.y;
		}
		if (scale.z > s) {
			s = scale.z;
		}
		return s;
	}

	public Vector3f getMinPoint() {
		return min;
	}

	/**
	 * create the grid such that the space around the graph is broken up into
	 * cells of dimensions R. Nodes are then assigned to their neighbourhood
	 * cell. If the graph takes up more space than the dimensions specified by
	 * the argument then the node positions are scaled to fit. In this case the
	 * k value (natural spring length used for the attractive force) should also
	 * be scaled... hence the scale can be obtained by the getScale method.
	 */
	Grid3D(Dimension dim, MultiScaleNodeLayout nodes[], float R) {
		// Find max and min positions in all dimensions
		MultiScaleNodeLayout v;
		min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		max = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
		for (int i = 0; i < nodes.length; i++) {
			v = nodes[i];
			if (v.position.x < min.x)
				min.x = v.position.x;
			if (v.position.x > max.x)
				max.x = v.position.x;
			if (v.position.y < min.y)
				min.y = v.position.y;
			if (v.position.y > max.y)
				max.y = v.position.y;
			if (v.position.z < min.z)
				min.z = v.position.z;
			if (v.position.z > max.z)
				max.z = v.position.z;
		}
		// Scale the graph so that it fits in the dimensions
		scale = new Vector3f(1f, 1f, 1f);
		Vector3f graphDim = new Vector3f();
		graphDim.sub(max, min);
		if (graphDim.x > dim.width) {
			scale.x = dim.width / graphDim.x;
			graphDim.x = dim.width;
		}
		if (graphDim.y > dim.height) {
			scale.y = dim.height / graphDim.y;
			graphDim.y = dim.height;
		}
		if (graphDim.z > dim.height) {
			scale.z = dim.height / graphDim.z;
			graphDim.z = dim.height;
		}
		// decompose into grid cells, a 3d array of Vectors of nodes
		maxCell.set((int) Math.ceil((float) graphDim.x / R), (int) Math.ceil((float) graphDim.y / R),
				(int) Math.ceil((float) graphDim.z / R));
		array = new Object[maxCell.x + 1][maxCell.y + 1][maxCell.z + 1];
		// centre all nodes around the origin
		Vector3f shift = new Vector3f((-max.x - min.x) / 2, (-max.y - min.y) / 2, (-max.z - min.z) / 2);
		min.add(shift);
		max.add(shift);
		for (int i = 0; i < nodes.length; i++) {
			v = nodes[i];
			v.position.add(shift);
			v.position.x *= scale.x;
			v.position.y *= scale.y;
			v.position.z *= scale.z;
			v.cell.set((int) ((v.position.x - min.x) / R), (int) ((v.position.y - min.y) / R),
					(int) ((v.position.z - min.z) / R));
			if (array[v.cell.x][v.cell.y][v.cell.z] == null) {
				array[v.cell.x][v.cell.y][v.cell.z] = new Vector();
			}
			((Vector) array[v.cell.x][v.cell.y][v.cell.z]).add(v);
		}
	}

	public void applyForceToNeighbourhood(Force f, MultiScaleNodeLayout v) {
		for (int i = v.cell.x - 1; i <= v.cell.x + 1; i++) {
			if (i < 0 || i >= maxCell.x)
				continue;
			for (int j = v.cell.y - 1; j <= v.cell.y + 1; j++) {
				if (j < 0 || j >= maxCell.y)
					continue;
				for (int k = v.cell.z - 1; k <= v.cell.z + 1; k++) {
					if (k < 0 || k >= maxCell.z)
						continue;
					Vector cellNodeList = (Vector) array[i][j][k];
					if (cellNodeList != null) {
						for (Iterator list = cellNodeList.iterator(); list.hasNext();) {
							f.apply((MultiScaleNodeLayout) list.next(), v);
						}
					}
				}
			}
		}
	}
}

abstract class Force {
	Vector3f tmp = new Vector3f();
	float d, f;

	abstract void apply(MultiScaleNodeLayout u, MultiScaleNodeLayout v);
}

public class MultiScaleGraph {
	MultiScaleNodeLayout nodes[];
	MultiScaleEdgeLayout edges[];
	static float repulsiveForceConstant = 0.1f;
	static float springForceConstant = 1.0f;
	// applying force deltas in a separate loop requires a much lower tolerance
	static float tol = 0.005f;
	static float maxDelta = 1f;
	static float cooling = 0.97f;
	float R, t, k, Rsquared;
	float ksquared;
	int level;

	MultiScaleGraph(MultiScaleNodeLayout[] nodes, MultiScaleEdgeLayout[] edges, int level) {
		this.nodes = nodes;
		this.edges = edges;
		this.level = level;
	}

	void resetTemperature(float k) {
		this.k = k;
		t = k;
		R = 2.0f * ((float) level + 1.0f) * k;
		Rsquared = R * R;
		System.out.println("R=" + R + ",k=" + k + ",level=" + level);
		t = k;
		ksquared = k * k;
	}

	private MultiScaleNodeLayout luckyDip(ArrayList list) {
		return (MultiScaleNodeLayout) list.get((int) ((float) Math.random() * (float) list.size()));
	}

	MultiScaleNodeLayout[] shuffledNodeArray(MultiScaleNodeLayout[] nodes) {
		MultiScaleNodeLayout[] randomNodes = new MultiScaleNodeLayout[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			randomNodes[i] = nodes[i];
		}
		for (int i = 0; i < randomNodes.length; i++) {
			int newloc = i + (int) (Math.random() * (float) (randomNodes.length - i));
			MultiScaleNodeLayout tmp = randomNodes[i];
			randomNodes[i] = randomNodes[newloc];
			randomNodes[newloc] = tmp;
			randomNodes[i].tmpind = i;
		}
		return randomNodes;
	}

	/**
	 * Computes a simple matching of the graph. nodes are visited at random and
	 * paired with their smallest mass neighbour
	 * 
	 * @return a courser version of this graph
	 */
	MultiScaleGraph coursenedGraph() {
		System.out.println("Coursening..." + nodes.length);
		// pair the nodes to create courseNodes list
		MultiScaleNodeLayout[] randomNodes = shuffledNodeArray(nodes);
		ArrayList courseNodes = new ArrayList();
		// create table of neighbours for all nodes
		ArrayList[] neighboursTable = new ArrayList[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			MultiScaleNodeLayout n = nodes[i];
			n.index = i;
			neighboursTable[i] = new ArrayList(n.neighbours);
		}
		for (int i = 0; i < randomNodes.length; i++) {
			// pick next node from random list
			MultiScaleNodeLayout u = randomNodes[i];
			// find lightest neighbour, at the same time removing u from
			// all it's neighbours' neighbours lists
			MultiScaleNodeLayout v = null;
			float minWeight = Float.MAX_VALUE;
			ArrayList neighbours = neighboursTable[u.index];
			for (Iterator it = neighbours.iterator(); it.hasNext();) {
				MultiScaleNodeLayout n = (MultiScaleNodeLayout) it.next();
				ArrayList neighboursNeighbours = neighboursTable[n.index];
				neighboursNeighbours.remove(u);
				if (n.mass < minWeight) {
					minWeight = n.mass;
					v = n;
				}
			}
			// create the new node representing the matched pair
			MultiScaleNodeLayout courseNode = new MultiScaleNodeLayout();
			u.parent = courseNode;
			courseNode.mass = u.mass;
			courseNode.level = u.level;
			// courseNode.label = new String(u.label);
			if (v != null) {
				courseNode.mass += v.mass;
				courseNode.level += v.level;
				courseNode.level /= 2;
				v.parent = courseNode;
				// courseNode.label = courseNode.label.concat(","+v.label);
				// remove v from the random list by moving the next element
				// on the list to v's position and advancing the pointer past
				// the next element
				if (i != randomNodes.length) {
					randomNodes[v.tmpind] = randomNodes[++i];
					randomNodes[v.tmpind].tmpind = v.tmpind;
				}
				neighbours = neighboursTable[v.index];
				for (Iterator it = neighbours.iterator(); it.hasNext();) {
					MultiScaleNodeLayout n = (MultiScaleNodeLayout) it.next();
					ArrayList neighboursNeighbours = neighboursTable[n.index];
					neighboursNeighbours.remove(v);
				}
			}
			courseNodes.add(courseNode);
		}
		// create edges between pairs of nodes
		ArrayList courseEdges = new ArrayList();
		for (int i = 0; i < edges.length; i++) {
			MultiScaleEdgeLayout e = edges[i];
			if (e.u.parent != e.v.parent) {
				MultiScaleEdgeLayout f = new MultiScaleEdgeLayout(e.u.parent, e.v.parent);
				if (!courseEdges.contains(f)) {
					courseEdges.add(f);
				}
			}
		}
		MultiScaleNodeLayout[] courseNodeArray = new MultiScaleNodeLayout[courseNodes.size()];
		MultiScaleEdgeLayout[] courseEdgeArray = new MultiScaleEdgeLayout[courseEdges.size()];
		courseNodes.toArray(courseNodeArray);
		courseEdges.toArray(courseEdgeArray);
		System.out.println("Coursened to: " + courseNodeArray.length);
		return new MultiScaleGraph(courseNodeArray, courseEdgeArray, level + 1);
	}

	/**
	 * @return global repulsive internode force
	 * @param x
	 *            separation between nodes
	 * @param mass
	 *            weight or mass of node
	 */
	float repulsiveForce(float x, float mass) {
		return (x <= R) ? repulsiveForceConstant * mass * ksquared / x : 0;
	}

	float repulsiveForce_xsquared(float xsquared, float mass) {
		return (xsquared <= Rsquared) ? repulsiveForceConstant * mass * ksquared / xsquared : 0;
	}

	/**
	 * @return local attractive edge force
	 * @param x
	 *            separation between nodes
	 * @param d
	 *            degree of the node
	 * @param mass
	 *            weight or mass of node
	 */
	float fl(float x, float d, float mass) {
		return (springForceConstant * (x - k) / d);// + repulsiveForce(x,mass);
	}

	float fl_squared(float xsquared, float d, float mass) {
		return springForceConstant * (xsquared - ksquared) / (k * d * d) + repulsiveForce_xsquared(xsquared, mass);
	}

	void randomLayout(Dimension dim) {
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].position.set(dim.width * (float) Math.random(), dim.height * (float) Math.random(), 0);
		}
	}

	float randomLength() {
		return (float) Math.random() * k;
	}

	void randomDelta(MultiScaleNodeLayout v) {
		v.delta.add(new Vector3f(randomLength(), randomLength(), randomLength()));
	}

	Force fglobal = new Force() {
		void apply(MultiScaleNodeLayout u, MultiScaleNodeLayout v) {
			if (u == v)
				return;
			tmp.sub(v.position, u.position);
			d = tmp.length();
			if (d > 0.0001f) {
				f = repulsiveForce(d, u.mass);
				tmp.scale(f);
				v.delta.add(tmp);
			} else {
				randomDelta(v);
			}
		}
	};
	Force flocal = new Force() {
		void apply(MultiScaleNodeLayout u, MultiScaleNodeLayout v) {
			tmp.sub(v.position, u.position);
			// d = tmp.lengthSquared();
			d = tmp.length();
			if (d > 0.0001f) {
				f = fl(d, v.neighbours.size(), u.mass);
				tmp.scale(f);
				v.delta.sub(tmp);
			} else {
				randomDelta(v);
			}
		}
	};
	Vector3f min, max;

	boolean layout(Dimension dim) {
		Vector3f oldpos = new Vector3f();
		Vector3f tmp = new Vector3f();
		float separation;
		float scale;
		MultiScaleNodeLayout v, u;
		boolean converged = true;
		Grid grid = new Grid3D(dim, nodes, R);
		k *= grid.getScale();
		max = grid.getMaxPoint();
		min = grid.getMinPoint();
		for (int i = 0; i < nodes.length; i++) {
			v = nodes[i];
			v.delta.set(0, 0, 0);
			// calculate global repulsive forces
			grid.applyForceToNeighbourhood(fglobal, v);
			/*
			 * replacing the above line with the following would apply global
			 * force to all nodes instead of just the neighbourhood... real slow
			 * for (int j = 0; j < nodes.length; j++) { u = nodes[j];
			 * fglobal.apply(u,v); }
			 */
			// calculate local spring forces
			for (int j = 0; j < v.neighbours.size(); j++) {
				u = (MultiScaleNodeLayout) v.neighbours.get(j);
				flocal.apply(u, v);
			}
		}
		for (int i = 0; i < nodes.length; i++) {
			v = nodes[i];
			// reposition v
			oldpos.set(v.position);
			float deltaLength = v.delta.length();
			if (deltaLength == 0)
				continue;
			scale = (t < deltaLength ? t : deltaLength);
			v.delta.scale(scale / deltaLength);
			deltaLength = v.delta.length();
			if (deltaLength > maxDelta) {
				v.delta.scale(maxDelta / deltaLength);
			}
			if (v.level >= 0) {
				v.delta.z = 0;
			}
			v.position.add(v.delta);
			// check convergence
			tmp.sub(v.position, oldpos);
			deltaLength = tmp.length();
			if (deltaLength > k * tol) {
				converged = false;
			}
		}
		// cool
		t *= cooling;
		return converged;
	}

	/**
	 * Copy the positions for the nodes of this graph from their parent node
	 * positions
	 */
	void setParentPositions() {
		for (int i = 0; i < nodes.length; i++) {
			MultiScaleNodeLayout n = nodes[i];
			n.position.set(n.parent.position);
		}
	}

	/**
	 * free up the references to each nodes parent in the hope that it will save
	 * some memory.
	 */
	void deleteParents() {
		for (int i = 0; i < nodes.length; i++) {
			MultiScaleNodeLayout n = nodes[i];
			// won't need the parent again so release the reference for the gc
			n.parent = null;
		}
	}
}
