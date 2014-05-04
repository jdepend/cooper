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
 * www.sourceforge.net/projects/wilma -- Tim Dwyer, 2001
 */
package org.wilmascope.file;

/**
 * Handles reading and writing of graph data structures to files
 * 
 * @author Tim Dwyer
 * @version 1.0
 */
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.gmlparser.GMLLoader;
import org.wilmascope.graph.LayoutEngine;
import org.wilmascope.layoutregistry.LayoutManager;
import org.wilmascope.layoutregistry.LayoutManager.UnknownLayoutTypeException;
import org.wilmascope.view.EdgeView;
import org.wilmascope.view.NodeView;
import org.wilmascope.view.ViewManager;

class GraphFileFilter extends FileFilter {
	public boolean accept(java.io.File file) {
		return file.isDirectory() || file.getName().endsWith(".xwg") || file.getName().endsWith(".gml")
				|| file.getName().endsWith(".lgr");
	}

	public String getDescription() {
		return "XML Wilma Graph data files (.xwg) or common graph (.gml)";
	}
}

class JPEGFileFilter extends FileFilter {
	public boolean accept(java.io.File file) {
		return file.isDirectory() || file.getName().endsWith(".jpg");
	}

	public String getDescription() {
		return "JPEG Image Files (.jpg)";
	}
}

public class FileHandler {
	GraphControl graphControl;

	Hashtable<String, GraphControl.Node> nodeLookup;

	Hashtable idLookup;

	boolean needsLayout = true;

	// at the moment it's possible for there to be multiple instances of
	// FileHandler,
	// so we'll make the lastFile static to ensure it really is the last File
	// loaded
	static File lastFile;

	public FileHandler(GraphControl graphControl) {
		this.graphControl = graphControl;
	}

	public File getLastFile() {
		return lastFile;
	}

	public void load(String fileName) {
		if (fileName.endsWith(".xwg")) {
			XMLGraph xmlGraph = new XMLGraph(fileName);
			try {
				xmlGraph.load();
				lastFile = xmlGraph.getFile();
				nodeLookup = new Hashtable();
				graphControl.reset();
				graphControl.freeze();
				long startTime = System.currentTimeMillis();
				GraphControl.Cluster gr = graphControl.getRootCluster();
				XMLGraph.Cluster xr = xmlGraph.getRootCluster();
				loadCluster(xr, gr);
				loadNodeProperties(xr, gr);
				long endTime = System.currentTimeMillis();
				long time = endTime - startTime;
				System.out.println("Loaded... in milliseconds: " + time);
				graphControl.getRootCluster().getCluster().draw();
				if (needsLayout) {
					graphControl.unfreeze();
				}
				graphControl.centreGraph();
			} catch (java.io.IOException ex) {
				WilmaMain.showErrorDialog("IOException loading xml graph file.", ex);
			}
		} else if (fileName.endsWith(".gml")) {
			GMLLoader gmlLoader = new GMLLoader(graphControl, fileName);
		} else if (fileName.endsWith(".lgr")) {
			LEDALoader ledaLoader = new LEDALoader(graphControl, fileName);
		}
	}

	public static FileFilter getFileFilter() {
		return new GraphFileFilter();
	}

	public static FileFilter getJPEGFileFilter() {
		return new JPEGFileFilter();
	}

	private void loadEdgeProperties(XMLGraph.Edge xe, GraphControl.Edge ge) {
		XMLGraph.ViewType viewType = xe.getViewType();
		if (viewType != null) {
			try {
				String viewTypeString = viewType.getName();
				EdgeView v = ViewManager.getInstance().createEdgeView(viewTypeString);
				ge.setView(v);
				v.setProperties(viewType.getProperties());
			} catch (ViewManager.UnknownViewTypeException e) {
				WilmaMain.showErrorDialog("Unknown View Type!", e);
			}
		}
		Properties p = xe.getProperties();
		if (p != null) {
			String weight = p.getProperty("Weight");
			if (weight != null) {
				ge.setWeight(Float.parseFloat(weight));
			}
		}
	}

	private void loadNodeProperties(XMLGraph.Node xn, GraphControl.Node gn) {
		XMLGraph.ViewType viewType = xn.getViewType();
		if (viewType != null) {
			try {
				NodeView v;
				if (gn instanceof GraphControl.Cluster) {
					v = ViewManager.getInstance().createClusterView(viewType.getName());
					gn.setView(ViewManager.getInstance().createClusterView(viewType.getName()));
				} else {
					v = ViewManager.getInstance().createNodeView(viewType.getName());
				}
				gn.setView(v);
				v.setProperties(viewType.getProperties());
			} catch (ViewManager.UnknownViewTypeException e) {
				WilmaMain.showErrorDialog("Unknown ViewType!", e);
			}
		}
		Properties p = xn.getProperties();
		if (p != null) {
			gn.setProperties(p);
		}
	}

	private void saveEdgeProperties(GraphControl.Edge ge, XMLGraph.Edge xe) {
		EdgeView v = (EdgeView) ge.getView();
		if (ge.getView() != null) {
			XMLGraph.ViewType xv = xe.setViewType(v.getTypeName());
			xv.setProperties(v.getProperties());
		}
		Properties p = new Properties();
		p.setProperty("Weight", "" + ge.getWeight());
		xe.setProperties(p);
	}

	private void saveNodeProperties(GraphControl.Node gn, XMLGraph.Node xn) {
		NodeView v = (NodeView) gn.getView();
		if (gn.getView() != null) {
			XMLGraph.ViewType xv = xn.setViewType(v.getTypeName());
			xv.setProperties(v.getProperties());
		}
		xn.setProperties(gn.getProperties());
	}

	private void loadCluster(XMLGraph.Cluster xmlRoot, GraphControl.Cluster graphRoot) {
		Vector<XMLGraph.Node> nodes = new Vector<XMLGraph.Node>();
		Vector<XMLGraph.Edge> edges = new Vector<XMLGraph.Edge>();
		Vector forces = new Vector();
		Vector<XMLGraph.Cluster> clusters = new Vector<XMLGraph.Cluster>();
		xmlRoot.load(nodes, edges, clusters);
		XMLGraph.LayoutEngineType layoutEngine = xmlRoot.getLayoutEngineType();
		loadLayoutEngine(layoutEngine, graphRoot);
		loadNodeProperties(xmlRoot, graphRoot);
		for (XMLGraph.Node xmlNode : nodes) {
			GraphControl.Node n = graphRoot.addNode();
			loadNodeProperties(xmlNode, n);
			nodeLookup.put(xmlNode.getID(), n);
		}
		for (XMLGraph.Cluster xc : clusters) {
			GraphControl.Cluster gc = graphRoot.addCluster();
			loadCluster(xc, gc);
		}
		for (XMLGraph.Edge xmlEdge : edges) {
			GraphControl.Edge e = graphRoot.addEdge(nodeLookup.get(xmlEdge.getStartID()),
					nodeLookup.get(xmlEdge.getEndID()));
			loadEdgeProperties(xmlEdge, e);
		}
	}

	private void loadLayoutEngine(XMLGraph.LayoutEngineType l, GraphControl.Cluster c) {
		try {
			if (l == null)
				return;
			String type = l.getName();
			LayoutEngine e = LayoutManager.getInstance().createLayout(type);
			c.setLayoutEngine(e);
			e.setProperties(l.getProperties());
		} catch (UnknownLayoutTypeException e1) {
			WilmaMain.showErrorDialog("Unknown Layout Type", e1);
		}
	}

	public void save(String fileName) {
		if (!fileName.endsWith(".xwg")) {
			fileName = fileName.concat(".xwg");
			System.out.println("File: " + fileName);
		}
		XMLGraph xmlGraph = new XMLGraph(fileName);
		xmlGraph.create();
		idLookup = new Hashtable();
		GraphControl.Cluster gr = graphControl.getRootCluster();
		XMLGraph.Cluster xr = xmlGraph.getRootCluster();
		saveCluster(gr, xr);
		saveNodeProperties(gr, xr);
		xmlGraph.save();
		lastFile = xmlGraph.getFile();
	}

	private void saveCluster(GraphControl.Cluster graphCluster, XMLGraph.Cluster xmlCluster) {
		saveLayoutEngine(graphCluster, xmlCluster);
		GraphControl.Node[] nodes = graphCluster.getNodes();
		Hashtable clusters = new Hashtable();
		for (int i = 0; i < nodes.length; i++) {
			GraphControl.Node graphNode = nodes[i];
			XMLGraph.Node xmlNode;
			if (graphNode instanceof GraphControl.Cluster) {
				xmlNode = xmlCluster.addCluster();
				clusters.put(graphNode, xmlNode);
			} else {
				xmlNode = xmlCluster.addNode();
				idLookup.put(graphNode, xmlNode.getID());
			}
			saveNodeProperties(graphNode, xmlNode);
		}
		for (Enumeration e = clusters.keys(); e.hasMoreElements();) {
			GraphControl.Cluster graphChildCluster = (GraphControl.Cluster) e.nextElement();
			saveCluster(graphChildCluster, (XMLGraph.Cluster) clusters.get(graphChildCluster));
		}
		GraphControl.Edge[] edges = graphCluster.getEdges();
		for (int i = 0; i < edges.length; i++) {
			XMLGraph.Edge xmlEdge = xmlCluster.addEdge((String) idLookup.get(edges[i].getStartNode()),
					(String) idLookup.get(edges[i].getEndNode()));
			saveEdgeProperties(edges[i], xmlEdge);
		}
	}

	private void saveLayoutEngine(GraphControl.Cluster graphRoot, XMLGraph.Cluster xmlCluster) {
		XMLGraph.LayoutEngineType l = null;
		Properties p = new Properties();
		org.wilmascope.graph.LayoutEngine gl = graphRoot.getLayoutEngine();
		l = xmlCluster.setLayoutEngineType(gl.getName());
		l.setProperties(gl.getProperties());
	}
}
