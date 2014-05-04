/*
 * The following source code is part of the WilmaScope 3D Graph Drawing Engine
 * which is distributed under the terms of the GNU Lesser General Public License
 * (LGPL - http://www.gnu.org/copyleft/lesser.html).
 *
 * As usual we distribute it with no warranties and anything you chose to do
 * with it you do at your own risk.
 *
 * Copyright for this work is retained by Tim Dwyer and the WilmaScope
 * organisation (www.wilmascope.org) however it may be used or modified to work
 * as part of other software subject to the terms of the LGPL. I only ask that
 * you cite WilmaScope as an influence and inform us (tgdwyer@yahoo.com) if you
 * do anything really cool with it.
 *
 * The WilmaScope software source repository is hosted by Source Forge:
 * www.sourceforge.net/projects/wilma
 *  -- Tim Dwyer, 2001
 */
package org.wilmascope.control;

import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Vector;

import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.wilmascope.forcelayout.BalancedEventClient;
import org.wilmascope.forcelayout.EdgeForceLayout;
import org.wilmascope.forcelayout.Force;
import org.wilmascope.forcelayout.ForceLayout;
import org.wilmascope.graph.EdgeList;
import org.wilmascope.graph.GraphElement;
import org.wilmascope.graph.LayoutEngine;
import org.wilmascope.graph.NodeLayout;
import org.wilmascope.graph.NodeList;
import org.wilmascope.gui.Actions;
import org.wilmascope.layoutregistry.LayoutManager;
import org.wilmascope.layoutregistry.LayoutManager.UnknownLayoutTypeException;
import org.wilmascope.view.BehaviorClient;
import org.wilmascope.view.ClusterView;
import org.wilmascope.view.EdgeView;
import org.wilmascope.view.GraphBehavior;
import org.wilmascope.view.GraphCanvas;
import org.wilmascope.view.GraphElementView;
import org.wilmascope.view.NodeView;
import org.wilmascope.view.PickingClient;
import org.wilmascope.view.ViewManager;

/*
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg @author Tim Dwyer
 * 
 * @version 1.0
 */
class ObservableLayout extends Observable {
	public void addObserver(LayoutObserver o) {
		super.addObserver(o);
	}

	protected void notifyLayoutObservers() {
		setChanged();
		notifyObservers();
	}
}

/**
 * GraphControl gives the access point or facade for the WilmaGraph drawing
 * engine.
 */

public class GraphControl extends ObservableLayout {
	// In {@link PickListener} we have to do what amounts to run-time type
	// checking so I figured why not pass around java.lang.Class variables.
	// In hindsight it probably wasn't the easiest or most elegant way of
	// doing it, but it works, it's type safe (isn't it?) and it was an
	// interesting excercise, so it can stay.
	{
		edgeClass = (new Edge()).getClass();
		clusterClass = (new Cluster(false)).getClass();
		nodeClass = (new Node(false)).getClass();
		// GraphElementFacade is abstract so we have to get the class as
		// follows:
		graphElementClass = nodeClass.getSuperclass();
	}

	/**
	 * an instance of a {@link GraphElementFacade}class that can be passed into
	 * {@link PickListener#enableMultiPicking}and
	 * {@link PickListener#setSinglePickClient}
	 */
	public static Class graphElementClass;

	/**
	 * an instance of a {@link Node}class that can be passed into
	 * {@link PickListener#enableMultiPicking}and
	 * {@link PickListener#setSinglePickClient}
	 */
	public static Class nodeClass;

	/**
	 * an instance of a {@link Edge}class that can be passed into
	 * {@link PickListener#enableMultiPicking}and
	 * {@link PickListener#setSinglePickClient}
	 */
	public static Class edgeClass;

	/**
	 * an instance of a {@link Cluster}class that can be passed into
	 * {@link PickListener#enableMultiPicking}and
	 * {@link PickListener#setSinglePickClient}
	 */
	public static Class clusterClass;

	/**
	 * the basic interface common to all GraphElements
	 */
	public abstract class GraphElementFacade {
		/**
		 * create a GraphElement with no view or layout
		 */
		GraphElementFacade() {
		}

		GraphElementFacade(GraphElement element) {
			this.element = element;
			element.storeUserData("Facade", this);
		}

		/**
		 * A view is inited after it is attached to an element
		 */
		protected void initView(GraphElementView view) {
			graphElementView = view;
			graphElementView.initGraphElement();
			if (label != null) {
				setLabel(label);
			}
			pickListener.register(this);
		}

		/**
		 * This will hide the element Clusters will have their view hidden but
		 * their constituents will still be visible (if they are already)
		 */
		public void hide() {
			element.hide();
		}

		/**
		 * Make element visible
		 */
		public void show() {
			element.show(graphCanvas);
		}

		/**
		 * set the colour of the component
		 * 
		 * @param red
		 *            level of red (0-1f)
		 * @param blue
		 *            level of blue (0-1f)
		 * @param green
		 *            level of green (0-1f)
		 */
		public void setColour(float red, float green, float blue) {
			graphElementView.setColour(red, green, blue);
		}

		public void setColour(java.awt.Color colour) {
			graphElementView.setColour(colour);
		}

		public java.awt.Color getColour() {
			return graphElementView.getColour();
		}

		/**
		 * revert to default colour
		 */
		public void defaultColour() {
			graphElementView.defaultColour();
		}

		public boolean isDefaultColour() {
			return graphElementView.isDefaultColour();
		}

		public java.awt.Color getDefaultColour() {
			return graphElementView.getDefaultColour();
		}

		/**
		 * set to highlight colour
		 */
		public void highlightColour() {
			graphElementView.highlightColour();
		}

		/**
		 * set the label to show with the element
		 * 
		 * @param label
		 *            the label to show
		 */
		public void setLabel(String label) {
			graphElementView.setLabel(label);
		}

		/**
		 * set a multiline label to show with the element
		 * 
		 * @param labelLines
		 *            an array of Strings that make up each of the lines of the
		 *            label
		 */
		public void setLabel(String[] labelLines) {
			graphElementView.setLabel(labelLines);
		}

		public String getLabel() {
			return label;
		}

		public void setPickable(boolean pickable) {
			graphElementView.setPickable(pickable);
		}

		/**
		 * set a PickingClient whose callback method will be called when the
		 * element is selected by picking with the mouse
		 */
		public void addPickingClient(PickingClient client) {
			graphElementView.addPickingClient(client);
		}

		public Object getUserData() {
			return graphElementView.getUserData();
		}

		public void setUserData(Object data) {
			graphElementView.setUserData(data);
		}

		public String getViewType() {
			return graphElementView.getTypeName();
		}

		public GraphElementView getView() {
			return graphElementView;
		}

		/**
		 * delete the element, removing all references to it... forever!
		 */
		public void delete() {
			if (element instanceof org.wilmascope.graph.Edge) {
				allEdges.remove((org.wilmascope.graph.Edge) element);
			} else {
				allEdges.removeAll(((org.wilmascope.graph.Node) element).getEdges());
				allNodes.remove((org.wilmascope.graph.Node) element);
			}
			if (element instanceof org.wilmascope.graph.Cluster) {
				NodeList nodes = ((org.wilmascope.graph.Cluster) element).getAllNodes();
				allEdges.removeAll(nodes.getEdges());
				allNodes.removeAll(nodes);
			}
			element.delete();
		}

		private String label;

		private GraphElementView graphElementView;

		private GraphElement element;
	}

	/**
	 * The interface for edges
	 */
	public class Edge extends GraphElementFacade {
		Edge() {
		}

		/**
		 * create a new facade for a predefined edge
		 * 
		 * @param edge
		 *            the predefined edge: note that the edge must have its view
		 *            set
		 */
		Edge(org.wilmascope.graph.Edge edge) {
			super(edge);
			this.edge = edge;
			allEdges.add(edge);
		}

		/**
		 * create an edge between two nodes
		 * 
		 * @param start
		 *            the start node
		 * @param end
		 *            the end node
		 */
		Edge(Node start, Node end) {
			this(new org.wilmascope.graph.Edge(start.getNode(), end.getNode()));
			edge.recalculate();
			this.start = start;
			this.end = end;
		}

		public void setView(EdgeView view) {
			// we use hide() to remove the old view from the scene graph and set
			// visible to false, (ie so that show() will actually do something)
			hide();
			if (view != null) {
				edge.setView(view);
				initView(view);
				show();
			}
		}

		/**
		 * set the natural or unstretched or compacted length of the edge
		 * 
		 * @param length
		 *            the new natural length for the edge
		 */
		public void setRelaxedLength(float length) {
			((EdgeForceLayout) edge.getLayout()).setRelaxedLength(length);
		}

		public void reverseDirection() {
			edge.reverseDirection();
			Node n = start;
			start = end;
			end = n;
		}

		public void setWeight(float weight) {
			edge.setWeight(weight);
		}

		public float getWeight() {
			return edge.getWeight();
		}

		public Node getStartNode() {
			return start;
		}

		public Node getEndNode() {
			return end;
		}

		/**
		 * get the edge underlying this facade
		 */
		public org.wilmascope.graph.Edge getEdge() {
			return edge;
		}

		private org.wilmascope.graph.Edge edge;

		private Node start, end;
	}

	/**
	 * the interface for nodes
	 */
	public class Node extends GraphElementFacade {
		/**
		 * create a new facade for a predefined node
		 * 
		 * @param n
		 *            the predefined node: note that the node must have its view
		 *            set
		 */
		Node(org.wilmascope.graph.Node n) {
			super(n);
			node = n;
			allNodes.add(n);
		}

		/** create a dummy nodeFacade */
		Node(boolean dummy) {
		}

		/**
		 * create a new node
		 */
		Node() {
			this(new org.wilmascope.graph.Node());
		}

		public void setPosition(javax.vecmath.Point3f pos) {
			node.setPosition(pos);
		}

		public javax.vecmath.Point3f getPosition() {
			return node.getPosition();
		}

		public void setView(NodeView view) {
			hide();
			if (view != null) {
				node.setView(view);
				initView(view);
				show();
			}
		}

		public void setMass(float m) {
			node.setMass(m);
		}

		public float getMass() {
			return node.getMass();
		}

		public void setRadius(float radius) {
			((org.wilmascope.view.NodeView) node.getView()).setRadius(radius);
		}

		public float getRadius() {
			return ((org.wilmascope.view.NodeView) node.getView()).getRadius();
		}

		public void moveToCanvasPos(int x, int y) {
			((NodeView) node.getView()).moveToCanvasPos(graphCanvas, x, y);
		}

		public int getDegree() {
			return node.getDegree();
		}

		public void setTransparency(float inverse_alpha) {
			((org.wilmascope.view.NodeView) this.getNode().getView()).getAppearance().setTransparencyAttributes(
					new TransparencyAttributes(TransparencyAttributes.FASTEST, inverse_alpha));
		} // setTransparency

		public void setProperties(Properties p) {
			node.setProperties(p);
			NodeLayout l = node.getLayout();
			if (l != null) {
				l.resetProperties();
			}
		}

		public void setProperty(String key, String value) {
			node.getProperties().setProperty(key, value);
			node.getLayout().resetProperties();
		}

		public void removeProperty(String key) {
			node.getProperties().remove(key);
			node.getLayout().resetProperties();
		}

		public Properties getProperties() {
			return node.getProperties();
		}

		/**
		 * get the node underlying this facade
		 */
		public org.wilmascope.graph.Node getNode() {
			return node;
		}

		private org.wilmascope.graph.Node node;
	}

	/**
	 * the interface for clusters which are collections of nodes, note that a
	 * cluster is also a node so you can add a cluster as a member to a cluster
	 * using the {@link #addNode(GraphControl.Node)}method
	 */
	public class Cluster extends Node {
		GraphControl gc;

		/**
		 * dummy constructor for creating dummy cluster objects, don't use.
		 */
		Cluster(boolean dummy) {
			super(false);
		}

		Cluster(GraphControl gc, org.wilmascope.graph.Cluster c) {
			super((org.wilmascope.graph.Node) c);
			cluster = c;
			this.gc = gc;
		}

		Cluster(GraphControl gc, NodeView view) {
			this(gc, new org.wilmascope.graph.Cluster(view));
			initView(view);
			this.gc = gc;
			String layout = "Force Directed";
			try {
				cluster.setLayoutEngine(layoutManager.createLayout(layout));
			} catch (UnknownLayoutTypeException e) {
				WilmaMain.showErrorDialog("Couldn't create '" + layout + "' layout engine", e);
			}
			show();
		}

		Cluster(GraphControl gc, String viewType) throws ViewManager.UnknownViewTypeException {
			this(gc, ViewManager.getInstance().createClusterView(viewType));
		}

		Cluster(GraphControl gc) throws ViewManager.UnknownViewTypeException {
			this(gc, ViewManager.getInstance().createClusterView());
		}

		/**
		 * set the callback client for when the system reaches a balanced state
		 * 
		 * @param client
		 *            the class whose
		 *            {@link org.wilmascope.forcelayout.BalancedEventClient#callback}
		 *            method will be called when the graph is balanced
		 */
		public void setBalancedEventClient(BalancedEventClient client) {
			((ForceLayout) cluster.getLayoutEngine()).setBalancedEventClient(client);
		}

		public void deleteAll() {
			freeze(); // stop layout
			NodeList condemned = new NodeList(cluster.getNodes());
			for (int i = 0; i < condemned.size(); i++) {
				Node n = (Node) condemned.get(i).getUserData("Facade");
				n.delete();
			}
		}

		public LayoutEngine getLayoutEngine() {
			return cluster.getLayoutEngine();
		}

		public void setLayoutEngine(LayoutEngine layoutEngine) {
			cluster.setLayoutEngine(layoutEngine);
		}

		/**
		 * Add a pre-existing node to the cluster
		 */
		public void addNode(Node n) {
			synchronized (gc) {
				org.wilmascope.graph.Node node = n.getNode();
				cluster.addNode(node);
			}
		}

		public void add(GraphElementFacade e) {
			synchronized (gc) {
				if (e instanceof Node) {
					addNode((Node) e);
				} else if (e instanceof Edge) {
					addEdge((Edge) e);
				}
			}
		}

		public Node addNode() {
			synchronized (gc) {
				try {
					return addNode(ViewManager.getInstance().createNodeView());
				} catch (ViewManager.UnknownViewTypeException ex) {
					WilmaMain.showErrorDialog("Couldn't get default Node View!", ex);
					return null;
				}
			}
		}

		public Node addNode(String nodeType) {
			synchronized (gc) {
				try {
					return addNode(ViewManager.getInstance().createNodeView(nodeType));
				} catch (ViewManager.UnknownViewTypeException ex) {
					WilmaMain.showErrorDialog("Couldn't get default Node View!", ex);
					return null;
				}
			}
		}

		/**
		 * create a new node and add it to the cluster
		 */
		public Node addNode(NodeView view) {
			synchronized (gc) {
				Node n = new Node();
				n.setView(view);
				addNode(n);
				return n;
			}
		}

		/**
		 * Add a pre-existing edge to a cluster
		 */
		private void addEdge(Edge e) {
			synchronized (gc) {
				cluster.addEdge(e.getEdge());
			}
		}

		/**
		 * Create a new edge between two nodes and add it to the cluster
		 * 
		 * @param start
		 *            the start node
		 * @param end
		 *            the end node
		 * @param view
		 *            the view to use for the edge
		 * @return the new edge
		 */
		public Edge addEdge(Node start, Node end, EdgeView view) {
			synchronized (gc) {
				Edge e = new Edge(start, end);
				e.setView(view);
				addEdge(e);
				return e;
			}
		}

		/**
		 * Add a new edge
		 * 
		 * @param start
		 *            the start node
		 * @param end
		 *            the end node
		 * @param edgeType
		 *            the type of edge to add, from the ViewManager
		 * @return the new Edge
		 */
		public Edge addEdge(Node start, Node end, String edgeType) {
			synchronized (gc) {
				try {
					EdgeView view = ViewManager.getInstance().createEdgeView(edgeType);
					return addEdge(start, end, view);
				} catch (ViewManager.UnknownViewTypeException ex) {
					WilmaMain.showErrorDialog("No view type specified", ex);
					return null;
				}
			}
		}

		public Edge addEdge(Node start, Node end, String edgeType, float radius) {
			synchronized (gc) {
				try {
					EdgeView view = ViewManager.getInstance().createEdgeView(edgeType);
					view.setRadius(radius);
					return addEdge(start, end, view);
				} catch (ViewManager.UnknownViewTypeException ex) {
					WilmaMain.showErrorDialog("Warning: view type: " + edgeType + " unknown, edge will be invisible.",
							ex);
					return addEdge(start, end, (EdgeView) null);
				}
			}
		}

		/**
		 * Add the new edge of the default type to the cluster
		 * 
		 * @param start
		 *            the start node
		 * @param end
		 *            the end node
		 * @return the new edge
		 */
		public Edge addEdge(Node start, Node end) {
			synchronized (gc) {
				try {
					EdgeView view = ViewManager.getInstance().createEdgeView();
					return addEdge(start, end, view);
				} catch (ViewManager.UnknownViewTypeException ex) {
					WilmaMain.showErrorDialog("Couldn't get default view", ex);
					return null;
				}
			}
		}

		/**
		 * Create a new cluster and add it as a member of this cluster
		 */
		public Cluster addCluster() {
			synchronized (gc) {
				try {
					Cluster c = new Cluster(gc);
					addNode(c);
					return c;
				} catch (ViewManager.UnknownViewTypeException e) {
					String msg = "No default ClusterView type set!??!?!";
					WilmaMain.showErrorDialog(msg, e);
					// that's all folks!
					throw new Error(msg);
				}
			}
		}

		/**
		 * Create a new cluster and add it as a member of this cluster
		 */
		public Cluster addCluster(String viewType) {
			synchronized (gc) {
				try {
					Cluster c = new Cluster(gc, viewType);
					addNode(c);
					return c;
				} catch (ViewManager.UnknownViewTypeException e) {
					System.err.println("Couldn't find view type: " + viewType + "in the repository, using default");
					return addCluster();
				}
			}
		}

		/**
		 * Create a new cluster and add it as a member of this cluster
		 */
		public Cluster addCluster(Vector nodeFacades) {
			synchronized (gc) {
				try {
					Cluster c = new Cluster(gc);
					addNode(c);
					NodeList nodes = new NodeList();
					for (int i = 0; i < nodeFacades.size(); i++) {
						nodes.add(((Node) nodeFacades.get(i)).getNode());
					}
					c.getCluster().addNodes(nodes);
					return c;
				} catch (ViewManager.UnknownViewTypeException e) {
					throw new Error();
				}
			}
		}

		/**
		 * move a node out of this cluster and into the parent cluster
		 * 
		 * @param n
		 *            node to move
		 */
		public void moveToParent(Node n) {
			cluster.moveToParent(n.getNode());
		}

		public void expand() {
			cluster.expand(graphCanvas);
			((ClusterView) cluster.getView()).setExpandedView();
		}

		public void collapse() {
			cluster.collapse();
			((ClusterView) cluster.getView()).setCollapsedView();
		}

		/**
		 * @return true if the cluster is expanded else false
		 */
		public boolean isExpanded() {
			return cluster.isExpanded();
		}

		/**
		 * make just the children of this cluster pickable, ie all other graph
		 * elements are not pickable, used for removing things from the cluster
		 */
		public void makeJustChildrenPickable() {
			setAllPickable(false);
			childrenPickable();
		}

		/**
		 * make just the graph elements other than this cluster pickable, used
		 * for adding things to the cluster. Also sets the cluster to the
		 * highlight colour to indicate we're doing something to it.
		 */
		public void makeNonChildrenPickable() {
			setAllPickable(true);
			setChildrenPickable(false);
			cluster.getView().setPickable(false);
			highlightColour();
		}

		/**
		 * makes children pickable... call this method on the rootCluster to
		 * reverse the effects of all
		 */
		public void childrenPickable() {
			setChildrenPickable(true);
			org.wilmascope.graph.ClusterList childClusters = cluster.getNodes().getClusters();
			for (org.wilmascope.graph.Cluster c : childClusters) {
				((GraphElementView) c.getView()).defaultColour();
			}
			cluster.getView().setPickable(false);
			setColour(242 / 255f, 200 / 255f, 242 / 255f);
		}

		/**
		 * sets the pickable status of the child members of this cluster
		 */
		private void setChildrenPickable(boolean pickable) {
			NodeList nodes = cluster.getNodes();
			for (int i = 0; i < nodes.size(); i++) {
				nodes.get(i).getView().setPickable(pickable);
			}
			EdgeList edges = cluster.getInternalEdges();
			for (int i = 0; i < edges.size(); i++) {
				edges.get(i).getView().setPickable(pickable);
			}
		}

		/**
		 * kind of dodgy all access method for controlling the pick status of
		 * all nodes and edges in the graph, reverts clusters to their default
		 * colour (to indicate they are pickable again
		 */
		public void setAllPickable(boolean pickable) {
			for (int i = 0; i < allNodes.size(); i++) {
				org.wilmascope.graph.Node node = allNodes.get(i);
				node.getView().setPickable(pickable);
				if (node instanceof org.wilmascope.graph.Cluster) {
					((GraphElementView) node.getView()).defaultColour();
				}
			}
			for (int i = 0; i < allEdges.size(); i++) {
				allEdges.get(i).getView().setPickable(pickable);
			}
		}

		public org.wilmascope.graph.Cluster getCluster() {
			return cluster;
		}

		public void showHiddenChildren() {
			for (int i = 0; i < allNodes.size(); i++) {
				allNodes.get(i).show(graphCanvas);
			}
			for (int i = 0; i < allEdges.size(); i++) {
				allEdges.get(i).show(graphCanvas);
			}
		}

		public Node[] getNodes() {
			NodeList nodes = cluster.getNodes();
			Node[] nodeFacades = new Node[nodes.size()];
			for (int i = 0; i < nodes.size(); i++) {
				nodeFacades[i] = (Node) nodes.get(i).getUserData("Facade");
			}
			return nodeFacades;
		}

		public Edge[] getEdges() {
			EdgeList edges = cluster.getInternalEdges();
			Edge[] edgeFacades = new Edge[edges.size()];
			for (int i = 0; i < edges.size(); i++) {
				edgeFacades[i] = (Edge) edges.get(i).getUserData("Facade");
			}
			return edgeFacades;
		}

		/**
		 * halts layout for this cluster
		 */
		public void freeze() {
			gc.freeze();
		}

		/**
		 * Triggers the layout engine for this cluster and it's children
		 */
		public void unfreeze() {
			gc.unfreeze();
		}

		/**
		 * Cause this cluster and it's children to be redrawn
		 */
		public void draw() {
			getCluster().draw();
		}

		private org.wilmascope.graph.Cluster cluster;
	}

	public class ForceFacade {
		ForceFacade(Force f) {
			this.force = f;
		}

		public void setStrength(float strength) {
			force.setStrengthConstant(strength);
		}

		public float getStrength() {
			return force.getStrengthConstant();
		}

		public String getType() {
			return force.getTypeName();
		}

		private Force force;
	}

	/**
	 * get a reference to the root cluster of the graph
	 */
	public Cluster getRootCluster() {
		return rootCluster;
	}

	public void setRootCluster(Cluster rootCluster) {
		this.rootCluster = (Cluster) rootCluster;
		// don't want the root cluster in the list of all nodes...
		allNodes.remove(rootCluster.getCluster());
		rootCluster.hide();
	}

	public void setRootPickingClient(PickingClient client) {
		graphCanvas.setRootPickingClient(client);
	}

	public GraphControl(int xsize, int ysize) {
		viewManager = ViewManager.getInstance();
		layoutManager = LayoutManager.getInstance();
		try {
			viewManager.loadViews();
		} catch (InstantiationException ex1) {
			System.err.println("Couldn't load plugins because " + ex1.getMessage());
		} catch (IllegalAccessException ex1) {
			System.err.println("Couldn't load plugins because " + ex1.getMessage());
		} catch (ClassNotFoundException ex1) {
			System.err.println("Couldn't load plugins because " + ex1.getMessage());
		} catch (java.io.IOException e) {
			System.err.println("Couldn't load plugins because " + e.getMessage());
		}
		try {
			viewManager.getEdgeViewRegistry().setDefaultView("Plain Edge");
			viewManager.getNodeViewRegistry().setDefaultView("DefaultNodeView");
			viewManager.getClusterViewRegistry().setDefaultView("Spherical Cluster");
		} catch (ViewManager.UnknownViewTypeException e) {
			WilmaMain.showErrorDialog("Unknown ViewType!  Fatal Error, stopping.", e);
			System.exit(1);
		}
		graphCanvas = new GraphCanvas(xsize, ysize);
		reset();
		balancedThreshold = ((ForceLayout) rootCluster.getLayoutEngine()).getBalancedThreshold();

		graphBehavior = (GraphBehavior) graphCanvas.addPerFrameBehavior(new BehaviorClient() {
			public void callback() {
				iterate();
			}
		});
		graphCanvas.createUniverse();
	}

	protected synchronized void iterate() {
		for (int i = 0; i < iterationsPerFrame; i++) {
			rootCluster.getCluster().calculateLayout();
			boolean balanced = rootCluster.getCluster().applyLayout();
			layoutIterationsCounter++;
			setChanged();
			if (balanced) {
				graphBehavior.setEnable(false);
				System.out.println("Balanced after: " + (float) (System.currentTimeMillis() - startTime) / 1000f);
				System.out.println("iterations: " + layoutIterationsCounter);
				notifyObservers(LayoutObserver.LAYOUT_FINISHED);
				break;
			} else {
				notifyObservers(LayoutObserver.LAYOUT_ITERATION);
			}
		}
		rootCluster.getCluster().draw();
	}

	public void centreGraph() {
		NodeList nodes = getRootCluster().getCluster().getAllNodes();
		int canvasHeight = graphCanvas.getHeight();
		int canvasWidth = graphCanvas.getWidth();
		Point3f bottomLeft = new Point3f(), topRight = new Point3f(), centre = new Point3f();
		nodes.getVWorldBoundingBoxCorners(bottomLeft, topRight, centre);

		float sceneWidth = topRight.x - bottomLeft.x;
		float sceneHeight = topRight.y - bottomLeft.y;
		float aspectRatio = (float) canvasWidth / (float) canvasHeight;
		float diameter = sceneWidth / aspectRatio;
		if (sceneHeight * aspectRatio > diameter) {
			diameter = sceneHeight * aspectRatio;
		}
		getGraphCanvas().reorient(new Vector3f(nodes.getMidPoint()), diameter);
	}

	public GraphCanvas getGraphCanvas() {
		return graphCanvas;
	}

	public void setIterationsPerFrame(int iterations) {
		this.iterationsPerFrame = iterations;
	}

	Observer layoutObserver = null;

	public void setLayoutObserver(Observer o) {
		this.layoutObserver = o;
	}

	public int getIterationsPerFrame() {
		return iterationsPerFrame;
	}

	/**
	 * Net effect is to freeze the layout animation
	 */
	public void freeze() {
		graphBehavior.setEnable(false);
	}

	/**
	 * Net effect is to unfreeze the layout animation
	 */
	public synchronized void unfreeze() {
		startTime = System.currentTimeMillis();
		layoutIterationsCounter = 0;
		graphBehavior.setEnable(true);
	}

	public synchronized void reset() {
		try {
			if (rootCluster != null) {
				rootCluster.freeze();
				rootCluster.deleteAll();
				rootCluster.delete();

			}
			Actions.getInstance().closeOpenFrames();
			setRootCluster(new Cluster(this));
		} catch (ViewManager.UnknownViewTypeException ex) {
			WilmaMain.showErrorDialog("Unknown ViewType!", ex);
			throw new Error();
		}
	}

	private float balancedThreshold = 0.002f;

	private Cluster rootCluster;

	private GraphCanvas graphCanvas;

	private ViewManager viewManager;

	// The constants
	private static org.wilmascope.global.GlobalConstants constants = org.wilmascope.global.GlobalConstants
			.getInstance();

	private NodeList allNodes = new NodeList();

	private EdgeList allEdges = new EdgeList();

	private long startTime = 0;

	private int layoutIterationsCounter = 0;

	private int iterationsPerFrame = 1;

	public static PickListener getPickListener() {
		return pickListener;
	}

	static PickListener pickListener = new PickListener();

	private GraphBehavior graphBehavior;

	private LayoutManager layoutManager;

}
