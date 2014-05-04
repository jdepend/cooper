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

/*
 * Definition of a Graph Node
 * 
 * Title: WilmaToo Description: Sequel to the ever popular WilmaScope software
 * Copyright: Copyright (c) 2001 Company: WilmaScope.org @author Tim Dwyer
 * 
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Definition of a graph Node or Vertex.
 * 
 * Nodes (or Vertices) in the graph may be:
 * <ul>
 * <li>connected to other nodes via edges</li>
 * <li>members of clusters</li>
 * <li>disconnected, ie having no edges connected to them</li>
 * </ul>
 */
public class Node extends GraphElement {
	public Node() {
	}

	public Node(NodeView view) {
		setView(view);
	}

	/**
	 * add an edge to the node's list of edges to which it is connected
	 */
	public void addEdge(Edge edge) {
		edges.add(edge);
	}

	/**
	 * remove a reference to an edge from this node
	 */
	protected void removeEdge(Edge edge) {
		// When removing an edge we should also make sure that it's removed from
		// the owner cluster's external edge list
		if (owner != null) { // owner will be null if this is the root
								// cluster
			owner.removeExternalEdge(edge);
		}
		edges.remove(edge);
	}

	/**
	 * return the common ancestor cluster of this node and the argument, ie
	 * recurse up the heirarchy of clusters until we meet a cluster to which
	 * both nodes are members or sub-members
	 */
	public Cluster getCommonAncestor(Node node) {
		if (owner.isAncestor(node)) {
			return owner;
		} else {
			return owner.getCommonAncestor(node);
		}
	}

	/**
	 * return a list of edges common to this node and the argument
	 */
	public EdgeList getCommonEdges(Node node) {
		EdgeList commonEdges = new EdgeList();
		for (Edge e : edges) {
			if (e.getNeighbour(this) == node) {
				commonEdges.add(e);
			}
		}
		return commonEdges;
	}

	/**
	 * delete the node forever, removing all references to it and all edges that
	 * are attached to it
	 */
	public void delete() {
		if (owner != null) {
			owner.remove(this);
		}
		if (view != null) {
			view.delete();
			view = null;
		}
		if (layout != null) {
			layout.delete();
			layout = null;
		}
		edges.delete();
	}

	/**
	 * @return List of edges attached to the node
	 */
	public EdgeList getEdges() {
		return edges;
	}

	/**
	 * @return list of edges for which this is the end node
	 */
	public EdgeList getInEdges() {
		EdgeList inEdges = new EdgeList();
		for (Edge e : edges) {
			if (e.getEnd() == this) {
				inEdges.add(e);
			}
		}
		return inEdges;
	}

	/**
	 * @return list of edges for which this is the start node
	 */
	public EdgeList getOutEdges() {
		EdgeList outEdges = new EdgeList();
		for (Edge e : edges) {
			if (e.getStart() == this) {
				outEdges.add(e);
			}
		}
		return outEdges;
	}

	/**
	 * set the edges by which this node is connected to other nodes copies the
	 * values in to preserve external references to the original list
	 */
	public void setEdges(EdgeList edges) {
		this.edges.addAll(edges);
	}

	/**
	 * set the view representing all visual aspects of the node and make the
	 * node visible
	 */
	public void setView(NodeView view) {
		this.view = view;
		view.setNode(this);
	}

	/**
	 * get the view representing the node
	 */
	public NodeView getView() {
		return (NodeView) view;
	}

	/**
	 * set the layout properties of the node
	 */
	public void setLayout(NodeLayout layout) {
		this.layout = layout;
		layout.setNode(this);
	}

	/**
	 * get the layout properties of the node
	 */
	public NodeLayout getLayout() {
		return (NodeLayout) layout;
	}

	/**
	 * set the position of the node in space
	 */
	public void setPosition(Point3f newPosition) {
		position.set(newPosition);
	}

	/**
	 * adjust the node's position by delta
	 * 
	 * @param delta
	 *            the adjustment to make to the node's position
	 */
	public void reposition(Vector3f delta) {
		position.add(delta);
	}

	/**
	 * get the position of the node in space
	 */
	public Point3f getPosition() {
		return position;
	}

	/**
	 * set the Mass of this node
	 */
	public void setMass(float mass) {
		this.mass = mass;
	}

	/**
	 * get the mass of this node
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * @return the number of edges connected to this node
	 */
	public int getDegree() {
		return edges.size();
	}

	/**
	 * @return the number of edges starting at this node
	 */
	public int getOutDegree() {
		return getOutEdges().size();
	}

	/**
	 * @return the number of edges ending at this node
	 */
	public int getInDegree() {
		return getInEdges().size();
	}

	public Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
		}
		Point3f pos = getPosition();
		properties.setProperty("Position", pos.x + " " + pos.y + " " + pos.z);
		properties.setProperty("Mass", "" + getMass());
		return properties;
	}

	public void setProperties(Properties p) {
		properties = p;
		String position = p.getProperty("Position");
		if (position != null) {
			StringTokenizer st = new StringTokenizer(position);
			setPosition(new Point3f(Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()),
					Float.parseFloat(st.nextToken())));
		}
		String mass = p.getProperty("Mass");
		if (mass != null) {
			setMass(Float.parseFloat(mass));
		}
	}

	private Properties properties;

	private EdgeList edges = new EdgeList();

	private Point3f position = new Point3f();

	// node's Spherical radius
	private float mass = 1f;

	/**
	 * Checks if there is an edge between this node and the argument
	 * 
	 * @param node
	 *            possible neighbour of this node
	 * @return true if such an edge exists
	 */
	public boolean isNeighbour(Node node) {
		for (Edge e : getEdges()) {
			if (e.getStart() == node || e.getEnd() == node) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return list of all nodes connected to this node by an edge
	 */
	public ArrayList<Node> getNeighbours() {
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for (Edge e : getEdges()) {
			neighbours.add(e.getNeighbour(this));
		}
		return neighbours;
	}
}
