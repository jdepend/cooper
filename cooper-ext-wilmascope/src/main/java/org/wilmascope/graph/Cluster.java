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

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;

/**
 * a Cluster is a set of Nodes ({@link Node}) and Edges ({@link Edge}) sharing
 * the same ({@link LayoutEngine}). This class is the main interface to the
 * graph package for adding {@link GraphElement}s to a graph hierarchy.
 * 
 * @author Tim Dwyer
 * @version $Id: Cluster.java,v 1.15 2004/11/16 01:47:11 tgdwyer Exp $
 */
public class Cluster extends Node {
	// the list of nodes in the cluster
	private NodeList nodes = new NodeList();

	// the list of edges internal to the cluster
	private EdgeList internalEdges = new EdgeList();

	/**
	 * @return a list of the children in this cluster
	 */
	public NodeList getNodes() {
		return nodes;
	}

	/**
	 * Add a {@link Node}to the Cluster
	 * 
	 * @param node
	 *            the Node to add
	 */
	public void addNode(Node node) {
		nodes.add(node);
		node.setOwner(this);
		if (layoutEngine != null) {
			node.setLayout((NodeLayout) layoutEngine.createNodeLayout(node));
		}
		// get this node's edges. Edges between the node to be added and nodes
		// that are members of this cluster will be added to this cluster's
		// internal edge list -- addEdge is a bit brute force: it
		// adds the edge to the common ancestor of the two ends -- see the notes
		// on additional witchcraft in addEdge
		EdgeList newNodeEdges = node.getEdges();
		for (Edge edge : newNodeEdges) {
			Node neighbour = edge.getNeighbour(node);
			// add the edge at the appropriate level in the cluster hierarchy
			addEdge(edge);
		}
		addMass(node.getMass());
	}

	/**
	 * Get the edges internal to this cluster, ie, only those edges connecting a
	 * pair of nodes that are <b>both </b> inside this cluster
	 * 
	 * @see Node#getEdges
	 * @return a list of edges
	 */
	public EdgeList getInternalEdges() {
		return internalEdges;
	}

	/**
	 * Check if the specified node is a child of this cluster or one of this
	 * cluster's child clusters etc recursively
	 * 
	 * @param node
	 *            node ancestory of
	 * @return true if this cluster is an ancestor of node
	 */
	public boolean isAncestor(Node node) {
		if (nodes.contains(node)) {
			return true;
		}
		ClusterList clusters = nodes.getClusters();
		for (Cluster c : clusters) {
			if (c.isAncestor(node)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * shift a {@link Node} up from this Cluster to its parent cluster
	 * 
	 * @param n
	 *            which Node to remove
	 */
	public void moveToParent(Node n) {
		remove(n);
		owner.addNode(n);
	}

	/**
	 * remove a {@link Node} up from this Cluster
	 * 
	 * @param n
	 *            which Node to remove
	 */
	protected void removeNode(Node n) {
		nodes.remove(n);
		// remove all the node's edges
		EdgeList edges = n.getEdges();
		for (Edge e : edges) {
			// if edge is already an external edge then remove it altogether
			if (super.getEdges().contains(e)) {
				super.removeEdge(e);
			} else if (e != null) {
				// otherwise it was an internal edge that should now be made
				// external
				internalEdges.remove(e);
				Node neighbour = e.getNeighbour(n);
				if (neighbour == null) {
					System.out.println("Warning... null neighbour while making internal"
							+ " edge external during node delete");
					// this may occur when cluster is collapsed
				} else {
					addExternalEdge(e, e.getNeighbour(n));
				}
			} // if e was external but the cluster was collapsed so that e has
				// been
				// promoted then our reference to the edge will be null...
				// nothing
				// needs to occur as the edge will be deleted from the owner
		}
		// recalculate the mass which should now be minus one node!
		addMass(-1 * n.getMass());
	}

	/**
	 * Add an {@link Edge}to the Cluster Note that the edge may not finish up
	 * being added to this cluster, it will be added to the lowest common
	 * ancestor of the two ends of the edge.
	 * 
	 * @param e
	 *            the edge to add
	 */
	public void addEdge(Edge e) {
		Cluster cluster;
		if ((cluster = e.getStart().getCommonAncestor(e.getEnd())) != null) {
			cluster.addInternalEdgeHere(e);
		} else {
			throw new Error("No root cluster!  Edge not added!");
		}
	}

	/**
	 * Add a reference to an external edge, an edge that has one end in the
	 * cluster and the other outside
	 * 
	 * @param edge
	 *            the external edge to add
	 * @param portalNode
	 *            the connection point of the edge inside the cluster
	 */

	private void addExternalEdge(Edge edge, Node portalNode) {
		// the EdgeList we inherited from Node is our external node list
		super.addEdge(edge);
		// make a mapping from the external edge to its connection point
		// inside the cluster (a "portal" node)
		portalNodes.put(edge, portalNode);
	}

	/**
	 * An external edge is one which has one end in this cluster and one end in
	 * another cluster that is not a child (or descendent) of this cluster. A
	 * portal node is a node in this cluster which has an external edge This
	 * method looks up the portal node in this cluster for a specific external
	 * edge.
	 * 
	 * @param edge
	 *            an edge external to this cluster
	 */
	public Node getPortalNode(Edge edge) {
		return (Node) portalNodes.get(edge);
	}

	/**
	 * Add an internal edge to this cluster... If the edge is already a member
	 * of this cluster it will be removed and then added again. Nothing like
	 * brute force to get the job done.
	 */
	protected void addInternalEdgeHere(Edge e) {
		if (!internalEdges.contains(e)) {
			e.setOwner(this);
			internalEdges.add(e);
			// just in case the edge was already external to this cluster
			// ie one end of the edge was already in this cluster and the other
			// has just been added
			super.removeEdge(e);
			// create the appropriate layout details for the edge depending on
			// the
			// layout engine used for this cluster
			if (layoutEngine != null) {
				e.setLayout((EdgeLayout) getLayoutEngine().createEdgeLayout(e));
			}
		}
		// for the each node of the edge, if the parent of that node is not
		// this cluster (ie the cluster that now owns the edge) then this edge
		// is external to that parent cluster
		Node n = e.getStart();
		Cluster owner = n.getOwner();
		if (owner != this) {
			owner.addExternalEdge(e, n);
		}
		n = e.getEnd();
		owner = n.getOwner();
		if (owner != this) {
			owner.addExternalEdge(e, n);
		}
	}

	/**
	 * remove a graph element (ie node or edge) from this cluster
	 */
	protected void remove(GraphElement e) {
		if (e instanceof Node) {
			removeNode((Node) e);
		} else if (e instanceof Edge) {
			removeEdge((Edge) e);
		}
	}

	/**
	 * Remove an {@link Edge}from the Cluster. Will work with either an internal
	 * or external edge.
	 */
	protected void removeEdge(Edge e) {
		internalEdges.remove(e);
		portalNodes.remove(e);
		removeExternalEdge(e);
	}

	/**
	 * remove an edge that is external to this cluster. If the edge is actually
	 * internal nothing will happen. If in doubt call {@link #removeEdge}
	 * 
	 * @see #removeEdge
	 */
	protected void removeExternalEdge(Edge e) {
		super.removeEdge(e);
	}

	/**
	 * The default constructor - an empty cluster
	 */
	public Cluster() {
	}

	/**
	 * Creates a new cluster with duplicate nodes and edges to the argument.
	 * References to the original graph elements are kept in userFacade. Does
	 * not recurse down clusters.
	 * 
	 * @param original
	 */
	public Cluster(Cluster original) {
		Hashtable<Node, Node> nodeMap = new Hashtable<Node, Node>();
		for (Node orig : original.getNodes()) {
			Node copy = new Node();
			nodeMap.put(orig, copy);
			// copy.setLayout(orig.getLayout());
			copy.storeUserData("Original", orig);
			addNode(copy);
		}
		for (Edge orig : original.getInternalEdges()) {
			Edge copy = new Edge(nodeMap.get(orig.getStart()), nodeMap.get(orig.getEnd()));
			copy.storeUserData("Original", orig);
			// copy.setLayout(orig.getLayout());
			addEdge(copy);
		}
		storeUserData("Original", original);
	}

	/**
	 * Create a new cluster with the specified {@link NodeView}
	 */
	public Cluster(NodeView view) {
		super(view);
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void draw() {
		if (expanded) {
			internalEdges.draw();
			nodes.draw();
			calcRadius();
		}
		super.draw();
	}

	/**
	 * Set the {@link LayoutEngine}for the cluster
	 */
	public void setLayoutEngine(LayoutEngine l) {
		layoutEngine = l;
		l.init(this);
	}

	/**
	 * Get the {@link LayoutEngine}used by the cluster
	 */
	public LayoutEngine getLayoutEngine() {
		return layoutEngine;
	}

	public void hideChildren() {
		nodes.hide();
		internalEdges.hide();
	}

	public void showChildren(org.wilmascope.view.GraphCanvas gc) {
		if (expanded) {
			nodes.show(gc);
			internalEdges.show(gc);
		}
	}

	/**
	 * @return all children (recursive descendants) of this cluster
	 */
	public NodeList getAllNodes() {
		NodeList allNodes = new NodeList(this.nodes);
		ClusterList clusters = this.nodes.getClusters();
		for (Cluster c : clusters) {
			allNodes.addAll(c.getAllNodes());
		}
		return allNodes;
	}

	public void collapse() {
		userCollapsed = true;
		subCollapse();
	}

	private void subCollapse() {
		this.expanded = false;
		ClusterList childClusters = nodes.getClusters();
		for (Cluster c : childClusters) {
			c.subCollapse();
		}

		hideChildren();
		EdgeList externalEdges = super.getEdges();
		for (Edge e : externalEdges) {
			e.collapse(this, (Node) portalNodes.get(e));
		}
	}

	private NodeList getLeafNodes() {
		NodeList leafNodes = new NodeList();
		for (Node node : nodes) {
			if (node instanceof Cluster) {
				Cluster cluster = (Cluster) node;
				if (cluster.isExpanded()) {
					leafNodes.addAll(((Cluster) node).getLeafNodes());
				} else {
					leafNodes.add(cluster);
				}
			} else {
				leafNodes.add(node);
			}
		}
		return leafNodes;
	}

	public void expand(org.wilmascope.view.GraphCanvas graphCanvas) {
		userCollapsed = false;
		subExpand(graphCanvas);
	}

	private void subExpand(org.wilmascope.view.GraphCanvas graphCanvas) {
		if (userCollapsed) {
			return;
		}
		expanded = true;
		nodes.setPosition(getPosition());
		nodes.show(graphCanvas);
		internalEdges.show(graphCanvas);
		EdgeList externalEdges = super.getEdges();
		for (Edge e : externalEdges) {
			e.expand(this);
		}
		for (Cluster c : nodes.getClusters()) {
			c.subExpand(graphCanvas);
		}
	}

	/**
	 * Find a new layout for the nodes according to the current graph and layout
	 * engine
	 */
	public void calculateLayout() {
		if (expanded) {
			layoutEngine.calculateLayout();
			nodes.getClusters().calculateLayout();

			// Let's postpone calculating the radius until just before we draw
			// since
			// at the moment there's nothing in Layout that needs it...
			// calcRadius();
		}
	}

	/**
	 * applies the layout changes calculated in
	 * {@link org.wilmascope.graph.Cluster#calculateLayout}to the contents of
	 * this cluster and all subclusters
	 * 
	 * @return true if all are balanced
	 */
	public boolean applyLayout() {
		if (balanced) {
			// layout invoked on a balanced graph...
			// need to notify listeners of change of state
			balanced = false;
			notifyBalancedEventListeners();
		}
		boolean clusterBalanced = layoutEngine.applyLayout();
		// this cluster only balanced if children are too
		boolean childrenBalanced = nodes.getClusters().applyLayout();
		balanced = clusterBalanced && childrenBalanced;
		if (balanced) {
			notifyBalancedEventListeners();
		}
		return balanced;
	}

	// The radius at any given time should be the distance from the centre of
	// the
	// cluster to the farthest node + that node's radius and a bit
	private void calcRadius() {
		float newRadius = 0;
		float tmpRadius;
		for (Node member : nodes) {
			tmpRadius = getPosition().distance(member.getPosition()) + member.getView().getRadius();
			if (tmpRadius > newRadius) {
				newRadius = tmpRadius;
			}
		}
		// add a little bit extra to the radius to make sure it covers all nodes
		newRadius += 0.1;
		((NodeView) view).setRadius(newRadius);
	}

	// A layout engine for use by the cluster
	private LayoutEngine layoutEngine;

	/**
	 * Moves this cluster (and its' internal nodes) by the specified amount
	 * 
	 * @param delta
	 *            the change vector public void reposition(Vector3f delta) {
	 *            super.reposition(delta); if(expanded) {
	 *            nodes.reposition(delta); } }
	 */
	/**
	 * delete a cluster and its contents
	 */
	public void delete() {
		NodeList tmplist = new NodeList(nodes);
		for (Node n : tmplist) {
			n.delete();
		}
		super.delete();
	}

	/**
	 * Add the specified nodes to this cluster
	 */
	public void addNodes(NodeList startNodes) {
		for (Node n : startNodes) {
			addNode(n);
		}
		Vector3f delta = new Vector3f(nodes.getBarycenter());
		delta.sub(getPosition());
		super.reposition(delta);
	}

	/**
	 * On adding or removing a node from the cluster the mass of the cluster
	 * will change, and a positive or negative contribution to the mass will
	 * have to be added, not only to this cluster, but to parent clusters as
	 * well. The latter is handled by {@link #setMass}
	 */
	public float addMass(float newMass) {
		setMass(getMass() + newMass);
		return getMass();
	}

	/**
	 * When the mass of this cluster changes, due to an addition or removal of a
	 * node from the cluster or by a user call to this method, the mass of the
	 * parent cluster must also be recalculated
	 */
	public void setMass(float newMass) {
		float delta = newMass - getMass();
		super.setMass(newMass);
		// owner of the root cluster will be null!
		if (owner != null) {
			owner.addMass(delta);
		}
	}

	public void addBalancedEventListener(BalancedEventListener l) {
		WeakReference<BalancedEventListener> wr = new WeakReference<BalancedEventListener>(l);
		if (balancedEventListeners == null) { // lazy instantiation might save a
												// bit
												// of memory
			balancedEventListeners = new Vector<WeakReference<BalancedEventListener>>();
		}
		balancedEventListeners.add(wr);
	}

	/*
	 * Notify any clients waiting for layout to be finished for this cluster
	 */
	private void notifyBalancedEventListeners() {
		if (balancedEventListeners != null) {
			for (Iterator<WeakReference<BalancedEventListener>> i = balancedEventListeners.iterator(); i.hasNext();) {
				WeakReference<BalancedEventListener> wr = i.next();
				BalancedEventListener l = wr.get();
				if (l != null) {
					l.clusterBalanced(this, balanced);
				} else {
					i.remove();
				}
			}
		}
	}

	/**
	 * @return Returns true if the cluster is balanced, meaning layout is not
	 *         currently in progress.
	 */
	public boolean isBalanced() {
		return balanced;
	}

	private Vector<WeakReference<BalancedEventListener>> balancedEventListeners;

	private boolean balanced = true;

	// a table mapping this cluster's external edges to their
	// connection points (portal nodes) in the cluster
	private Hashtable portalNodes = new Hashtable();

	private AxisAngle4f rotationAngle = new AxisAngle4f();

	// indicates whether this cluster should be included in recursive operations
	// such as layout calculation and drawing
	private boolean expanded = true;

	// used to keep track of whether the cluster was collapsed by a user (in
	// which case it should not be expanded until specifically requested) or not
	// in which case it will be automatically expanded when a parent is
	// expanded.
	private boolean userCollapsed = false;

	/**
	 * Finds an acyclic subset Ea of this cluster's edges E such that
	 * |Ea|>=|E|/2 using a simple greedy heuristic (Berger and Shor, 1990).
	 * 
	 * @return acyclic subset of edges in this cluster
	 */
	public EdgeList getAcyclicEdgeSet_Greedy() {
		Cluster copy = new Cluster(this);
		EdgeList acyclicEdges = new EdgeList();
		while (copy.getNodes().size() > 0) {
			Node v = copy.getNodes().get(0);
			if (v.getOutDegree() >= v.getInDegree()) {
				acyclicEdges.addAll(v.getOutEdges());
			} else {
				acyclicEdges.addAll(v.getInEdges());
			}
			v.delete();
		}
		EdgeList originalAcyclicEdges = new EdgeList();
		for (Edge e : acyclicEdges) {
			originalAcyclicEdges.add((Edge) e.getUserData("Original"));
		}
		return originalAcyclicEdges;
	}

	/**
	 * Uses a tricky heuristic (Eades et al. 1993) to find an acyclic subset
	 * (Ea) of this cluster's edges (E) such that: |Ea|>=|E|/2+|V|/6
	 * 
	 * @return acyclic subset of edges in this cluster
	 */
	public EdgeList getAcyclicEdgeSet_EnhancedGreedy() {
		Cluster copy = new Cluster(this);
		EdgeList acyclicEdges = new EdgeList();
		NodeList V = copy.getNodes();
		while (V.size() > 0) {
			while (V.getSinks().size() > 0) {
				Node sink = V.getSinks().get(0);
				acyclicEdges.addAll(sink.getEdges());
				sink.delete();
			}
			while (V.getIsolated().size() > 0) {
				V.getIsolated().get(0).delete();
			}
			while (V.getSources().size() > 0) {
				Node source = V.getSources().get(0);
				acyclicEdges.addAll(source.getEdges());
				source.delete();
			}
			if (V.size() > 0) {
				int maxDelta = 0;
				Node sourciestNode = null;
				for (Node v : V) {
					int delta = v.getOutDegree() - v.getInDegree();
					if (delta >= maxDelta) {
						maxDelta = delta;
						sourciestNode = v;
					}
				}
				acyclicEdges.addAll(sourciestNode.getOutEdges());
				sourciestNode.delete();
			}
		}
		EdgeList originalAcyclicEdges = new EdgeList();
		for (Edge e : acyclicEdges) {
			originalAcyclicEdges.add((Edge) e.getUserData("Original"));
		}
		return originalAcyclicEdges;
	}
}
