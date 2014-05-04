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

import java.util.Hashtable;
import java.util.Properties;

import javax.vecmath.Vector3f;

/**
 * An Edge links two nodes
 */
public class Edge extends GraphElement {
	public Edge() {
	}

	/**
	 * Creates an edge between two nodes
	 * 
	 * @param start
	 *            Node
	 * @param end
	 *            Node
	 */
	public Edge(Node start, Node end) {
		setStart(start);
		setEnd(end);
	}

	/**
	 * Creates an edge between two nodes, and sets the view
	 * 
	 * @param start
	 *            Node
	 * @param end
	 *            Node
	 * @param view
	 *            EdgeView
	 */
	public Edge(Node start, Node end, EdgeView view) {
		this(start, end);
		setView(view);
	}

	/**
	 * Associates a ({@link EdgeView}) view object with this edge. (For any
	 * edge, there can be only one view object)
	 * 
	 * @param view
	 *            {@link EdgeView}
	 */
	public void setView(EdgeView view) {
		this.view = view;
		view.setEdge(this);
		recalculateMultiEdgeOffsets();
	}

	/**
	 * When multiple edges exist between a pair of nodes we want them offset
	 * from each other by a small amount so that they are both visible.
	 */
	public void recalculateMultiEdgeOffsets() {
		EdgeList commonEdges = start.getCommonEdges(end);
		int i = 0;
		for (Edge e : commonEdges) {
			int direction = 1;
			if (e.getStart() != start) {
				direction = -1;
			}
			if (e.getView() != null) {
				e.getView().setMultiEdgeOffset(i, commonEdges.size(), direction);
			}
		}
	}

	/**
	 * @return the {@link EdgeView} for the edge object. (For any edge object,
	 *         there can be only one view object)
	 */
	public EdgeView getView() {
		return (EdgeView) view;
	}

	/**
	 * Associates an EdgeLayout object with <strong>this</strong> edge
	 */
	public void setLayout(EdgeLayout layout) {
		this.layout = layout;
		layout.setEdge(this);
	}

	/**
	 * @return {@link EdgeLayout} object for this Edge.
	 */
	public EdgeLayout getLayout() {
		return (EdgeLayout) layout;
	}

	private Node start;
	private Node end;

	/**
	 * @return the start node for this edge
	 */
	public Node getStart() {
		return start;
	}

	/**
	 * Sets a new start Node for this edge.
	 * 
	 * @param newStart
	 *            - a replacement Node
	 */
	public void setStart(Node newStart) {
		start = newStart;
		newStart.addEdge(this);
	}

	/**
	 * Sets a new end Node for this edge.
	 * 
	 * @param newEnd
	 *            - a replacement Node
	 */
	public void setEnd(Node newEnd) {
		end = newEnd;
		newEnd.addEdge(this);
	}

	/**
	 * @return the End node of <strong>this</strong> edge.
	 */
	public Node getEnd() {
		return end;
	}

	/**
	 * swap the start and end nodes so that the edge effectively reverses
	 * direction
	 */
	public void reverseDirection() {
		// have to remove the edge reference from the nodes first as setStart
		// and
		// setEnd call addEdge
		start.removeEdge(this);
		end.removeEdge(this);
		Node n = start;
		setStart(end);
		setEnd(n);
		// have to recalculate the multi Edge offsets taking into account
		// the new direction of this edge
		recalculateMultiEdgeOffsets();
	}

	/**
	 * @return the length of <strong>this</strong> edge.
	 */
	public float getLength() {
		return length;
	}

	/**
	 * If node is one of the nodes connected by the edge, this method returns
	 * it's partner. If node is not a immediately part of the edge, but it is a
	 * cluster and one end of the edge is a child or descendant of the cluster
	 * then the other end is returned. If all else fails null is returned. It
	 * would not be real useful to call this method if node is a Cluster and
	 * both ends of the edge are members of the cluster, it will simply always
	 * return end in this case.
	 */
	public Node getNeighbour(Node node) {
		if (node == start) {
			return end;
		} else if (node == end) {
			return start;
		} else if (node instanceof Cluster) {
			Cluster c = (Cluster) node;
			if (c.isAncestor(start)) {
				return end;
			} else if (c.isAncestor(end)) {
				return start;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * assuming oldNode is either the start or end of this edge, this method
	 * replaces oldNode with newNode. Actually, it's not really a complete
	 * replacement, since this edge is not actually removed from oldNode's
	 * edgeList, since: in an edge collapse, oldNode will be part of the cluster
	 * that is now collapsed, so it won't be rendered anyway in an edge expand,
	 * oldNode is the re-expanded cluster, don't want to remove the edge from
	 * the cluster's edgelist because it is still an external edge to the
	 * cluster. Also when expanding, setStart/End will try to add the edge to
	 * the newNode's edge list (where it will already be) but since these days
	 * EdgeList does not contain duplicates this doesn't matter.
	 */
	private void swapNode(Node oldNode, Node newNode) {
		if (oldNode == start) {
			setStart(newNode);
		} else if (oldNode == end) {
			setEnd(newNode);
		}
	}

	/**
	 * used to temporarily change the edge to point to cluster instead of node
	 * (assuming node is one of the edge's nodes). {@link #expand(Cluster)}
	 * undoes the effect.
	 */
	public void collapse(Cluster cluster, Node node) {
		collapseHistoryTable.put(cluster, node);
		swapNode(node, cluster);
	}

	/**
	 * undoes the effect of {@link #collapse} for a given cluster
	 */
	public void expand(Cluster cluster) {
		Node node = (Node) collapseHistoryTable.remove(cluster);
		swapNode(cluster, node);
	}

	/**
	 * Undirected comparison of the ends of this edge against the specified
	 * nodes
	 * 
	 * @param u
	 *            either the start or end vertex
	 * @param v
	 *            if u=start then v=end else u=end v=start
	 * @return true if above conditions are met
	 */
	public boolean hasVertices(Node u, Node v) {
		if (start == u && end == v || start == v && end == u) {
			return true;
		}
		return false;
	}

	/**
	 * Delete all references to this edge... make it go away... forever!
	 */
	public void delete() {
		owner.remove(this);
		start.removeEdge(this);
		end.removeEdge(this);
		if (view != null) {
			view.delete();
			view = null;
		}
		if (layout != null) {
			layout.delete();
			layout = null;
		}
		recalculateMultiEdgeOffsets();
	}

	public void recalculate() {
		vector.sub(end.getPosition(), start.getPosition());
		length = vector.length();
	}

	public void setVector(Vector3f v) {
		vector.set(v);
	}

	public Vector3f getVector() {
		return vector;
	}

	public boolean isDirected() {
		return directed;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getWeight() {
		return weight;
	}

	private float length;
	// Vector along the length of the edge
	private Vector3f vector = new Vector3f();
	private Hashtable<Cluster, Node> collapseHistoryTable = new Hashtable<Cluster, Node>();
	private boolean directed = true;
	// A user assigned weighting for the edge between 0 and 1
	private float weight = 0.5f;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.GraphElement#getProperties()
	 */
	public Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
		}
		properties.setProperty("Weight", "" + getWeight());
		properties.setProperty("IsDirected", "" + isDirected());
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.wilmascope.graph.GraphElement#setProperties(java.util.Properties)
	 */
	public void setProperties(Properties p) {
		properties = p;
		String value = properties.getProperty("Weight");
		if (value != null) {
			setWeight(Float.parseFloat(value));
		}
		value = properties.getProperty("directed");
		if (value != null) {
			directed = Boolean.parseBoolean(value.toLowerCase());
		}
	}

	private Properties properties;
}
