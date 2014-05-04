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

package org.wilmascope.dotlayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JPanel;
import javax.vecmath.Point3f;

import org.wilmascope.columnlayout.ColumnLayout;
import org.wilmascope.control.GraphControl;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.dotparser.DotParser;
import org.wilmascope.dotparser.EdgeClient;
import org.wilmascope.dotparser.GraphClient;
import org.wilmascope.dotparser.LineBreakFilter;
import org.wilmascope.dotparser.NodeClient;
import org.wilmascope.global.GlobalConstants;
import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Edge;
import org.wilmascope.graph.EdgeLayout;
import org.wilmascope.graph.EdgeList;
import org.wilmascope.graph.LayoutEngine;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeLayout;
import org.wilmascope.graph.NodeList;
import org.wilmascope.view.SizeAdjustableNodeView;
import org.wilmascope.viewplugin.ColumnClusterView;

/**
 * <p>
 * A layout engine which uses the dot program to find node and edge positions.
 * Dot is part of the AT&T Labs-Research <a
 * href="http://www.research.att.com/sw/tools/graphviz/">Graphviz suite</a>.
 * </p>
 * <p>
 * Copyright: Copyright (c) Tim Dwyer 2001
 * </p>
 * <p>
 * Company: WilmaScope.org
 * </p>
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class DotLayout extends LayoutEngine {
	TreeMap nodeLookup = new TreeMap();
	TreeMap edgeLookup = new TreeMap();
	TreeMap curveLookup = new TreeMap();
	Vector curvelessEdges = new Vector();

	public void calculateLayout() {
		NodeList nodes = getRoot().getNodes();

		try {
			String dotPath = GlobalConstants.getInstance().getProperty("DotPath");
			FileOutputStream in = new FileOutputStream(new File("in.dot"));
			in.write("digraph d { graph [concentrate=false]; node [ ".getBytes());
			in.write("fixedsize=true ".getBytes());
			in.write("layer=all ".getBytes());
			in.write("];\n".getBytes());
			int numLayers = ((Cluster) nodes.get(0)).getNodes().size();
			// always assume at least 2 layers otherwise dot removes quotes
			// around layers
			// string in out.dot which is a pain to parse
			String layersString = "layers=\"l0:l1";
			for (int i = 2; i < numLayers; i++) {
				layersString = layersString + ":l" + i;
			}
			layersString = layersString + "\"\n";
			in.write(layersString.getBytes());
			for (Node n : nodes) {
				float rad;
				try {
					rad = ((ColumnClusterView) n.getView()).getMaxRadius();

				} catch (ClassCastException e) {
					WilmaMain
							.showErrorDialog("Not a ColumnClusterView, it's a: " + n.getView().getClass().getName(), e);
					rad = 1.0f;
				}
				String id = "" + n.hashCode();
				nodeLookup.put(id, n);
				String shape = "circle";
				float height = rad, width = rad;
				if (((SizeAdjustableNodeView) n.getView()).getShape() == SizeAdjustableNodeView.BOX) {
					shape = "box";
					height = ((SizeAdjustableNodeView) n.getView()).getDepth() * 6f;
				}
				String text = id + " [label=\"" + ((org.wilmascope.view.GraphElementView) n.getView()).getLabel()
						+ "\" shape=" + shape + " width=\"" + width / 6f + "\" height=\"" + height / 6f + "\"];\n";
				in.write(text.getBytes());
			}
			EdgeList edges = getRoot().getInternalEdges();
			// masterEdgeList is a lookup table which has a list of matching
			// edges for each
			// "master edge". For a given master Edge a matching edge is one
			// with both nodes
			// in the same columnClusters as the master.
			Hashtable masterEdgeList = new Hashtable();
			// for each edge
			// if matching edge in masterEdgeList
			// add edge to matchingEdgeList for masterEdge
			// else
			// add edge to masterEdgeList
			// write edge to dot file

			for (Edge e : edges) {
				((DotEdgeLayout) e.getLayout()).setZLevel(((org.wilmascope.columnlayout.NodeColumnLayout) e.getStart()
						.getLayout()).getStratum());
				Edge m = matchingEdge(masterEdgeList.keys(), e);
				if (m != null) {
					Vector matchingEdgeList = (Vector) masterEdgeList.get(m);
					matchingEdgeList.add(e);
				} else {
					masterEdgeList.put(e, new Vector());
				}
			}
			for (Enumeration es = masterEdgeList.keys(); es.hasMoreElements();) {
				Edge e = (Edge) es.nextElement();
				Node start = e.getStart();
				Node end = e.getEnd();
				DotEdgeLayout eLayout = (DotEdgeLayout) e.getLayout();
				if (start.getOwner() != getRoot()) {
					start = start.getOwner();
				}
				if (end.getOwner() != getRoot()) {
					end = end.getOwner();
				}
				String text = new String(start.hashCode() + "->" + end.hashCode());
				Vector matchingEdges = (Vector) masterEdgeList.get(e);
				String layerText = new String("l" + eLayout.getZLevel());
				for (Iterator i = matchingEdges.iterator(); i.hasNext();) {
					Edge m = (Edge) i.next();
					layerText = layerText + ":l" + ((DotEdgeLayout) m.getLayout()).getZLevel();
				}
				edgeLookup.put(text, e);
				in.write(text.concat("[ layer=\"" + layerText + "\" ").getBytes());
				in.write(("weight=\"" + ((int) e.getWeight() + 1) + "\" ").getBytes());
				in.write("]".getBytes());
				in.write(";\n".getBytes());
			}
			in.write("}\n".getBytes());
			in.flush();
			in.close();
			Process p = Runtime.getRuntime().exec(dotPath + " in.dot -o out.dot",
					new String[] { "DOT_CROSSING_STYLE=2", "DOT_ILP_MINCROSS=1" }, null);
			InputStream err = p.getErrorStream();
			try {
				BufferedReader r = new BufferedReader(new InputStreamReader(err));
				String s;
				while ((s = r.readLine()) != null) {
					System.out.println(s);
				}
				parseDot(new FileInputStream("out.dot"));
			} catch (org.wilmascope.dotparser.ParseException e) {
				System.err.println(e.getMessage());
			}
			for (Enumeration en = masterEdgeList.keys(); en.hasMoreElements();) {
				Edge master = (Edge) en.nextElement();
				Vector clones = (Vector) masterEdgeList.get(master);
				for (Iterator i = clones.iterator(); i.hasNext();) {
					Edge clone = (Edge) i.next();

					Spline cloneSpline = (Spline) clone.getView();
					Spline masterSpline = (Spline) master.getView();
					cloneSpline.copyCurves(masterSpline);
				}
			}
		} catch (IOException e) {
			WilmaMain.showErrorDialog("IOException occured processing dot file", e);
		}
	}

	/*
	 * returns a matching edge from masterEdges for the specified edge. A
	 * matching edge is one whose nodes are in the same columnClusters as that
	 * of the argument's nodes. Returns null if no master edge is found.
	 */
	private Edge matchingEdge(Enumeration masterEdges, Edge edge) {
		Edge masterEdge = null;
		while (masterEdges.hasMoreElements()) {
			Edge e = (Edge) masterEdges.nextElement();
			if (e.getStart().getOwner() == edge.getStart().getOwner()
					&& e.getEnd().getOwner() == edge.getEnd().getOwner()) {
				masterEdge = e;
				break;
			}

		}
		return masterEdge;
	}

	private boolean stratified = false;
	static DotParser parser;

	/**
	 * parses a dot output file
	 */
	void parseDot(InputStream stream) throws org.wilmascope.dotparser.ParseException {
		InputStream f = new LineBreakFilter(stream);
		if (parser == null) {
			parser = new DotParser(f);
		} else {
			DotParser.ReInit(f);
		}
		DotParser.graph(new GraphClient() {
			public void setBoundingBox(String bounds) {
				StringTokenizer st = new StringTokenizer(bounds, ",");
				bbXMin = Integer.parseInt(st.nextToken());
				bbYMin = Integer.parseInt(st.nextToken());
				bbXMax = Integer.parseInt(st.nextToken());
				bbYMax = Integer.parseInt(st.nextToken());
				width = (float) (bbXMax - bbXMin);
				height = (float) (bbYMax - bbYMin);
			}

			public EdgeClient addEdge(NodeClient start, NodeClient end) {
				return new EdgeClient(start, end) {
					public void setCurves(Vector curves) {
						String start = this.start.getID();
						String end = this.end.getID();
						String key = new String(start + "->" + end);
						if (curves.size() != 0) {
							curveLookup.put(key, curves);
						}
						Edge e = (Edge) edgeLookup.get(key);
						if (e == null) {
							System.err.println("Warning: null edge (OK if it's a dummy edge)");
							return;
						}
						if (curves.size() == 0) {
							curvelessEdges.add(e);
							return;
						}
						Spline spline = (Spline) e.getView();
						spline.setCurves(xScale, yScale, bbXMin, bbYMin, bbXMax, bbYMax, curves, getArrowPositions());
					}
				};
			}

			public NodeClient addNode(String id) {
				NodeClient n = (NodeClient) nodeList.get(id);
				if (n == null) {
					n = new NodeClient(id) {
						public void setPosition(int x, int y) {
							Node n = (Node) nodeLookup.get(this.id);
							if (n == null) {
								return;
							}
							Point3f p = n.getPosition();
							p.x = xScale * ((float) (x - bbXMin) / width - 0.5f);
							p.y = yScale * ((float) (y - bbYMin) / width - 0.5f);
						}
					};
					nodeList.put(id, n);
				}
				return n;
			}

			Hashtable nodeList = new Hashtable();
		});

	}

	public int bbXMin, bbXMax, bbYMin, bbYMax;
	public float width, height;
	private float xScale = 2.0f;
	private float yScale = 2.0f;

	public boolean applyLayout() {
		return true;
	}

	public void reset() {
	}

	public NodeLayout createNodeLayout(org.wilmascope.graph.Node n) {
		return new DotNodeLayout();
	}

	public EdgeLayout createEdgeLayout(org.wilmascope.graph.Edge e) {
		return new DotEdgeLayout();
	}

	public JPanel getControls() {
		return new ControlPanel((GraphControl.Cluster) getRoot().getUserData("Facade"));
	}

	/**
	 * @return the horizontal scale for the layout
	 */
	public float getXScale() {
		return xScale;
	}

	/**
	 * @return the vertical scale for the layout
	 */
	public float getYScale() {
		return yScale;
	}

	/**
	 * @param f
	 *            horizontal scale
	 */
	public void setXScale(float f) {
		xScale = f;
	}

	/**
	 * @param f
	 *            vertical scale
	 */
	public void setYScale(float f) {
		yScale = f;
	}

	public void setStrataSeparation(float sep) {
		NodeList l = getRoot().getNodes();
		for (Node n : l) {
			((ColumnLayout) ((Cluster) n).getLayoutEngine()).setStrataSeparation(sep);
		}
	}

	public float getStrataSeparation() {
		NodeList l = getRoot().getNodes();
		float s = 0;
		if (l.size() > 0) {
			s = ((ColumnLayout) ((Cluster) l.get(0)).getLayoutEngine()).getStrataSeparation();
		}
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#getProperties()
	 */
	public Properties getProperties() {
		super.getProperties().setProperty("XScale", "" + getXScale());
		super.getProperties().setProperty("YScale", "" + getYScale());
		return super.getProperties();
	}

	public void resetProperties() {
		super.resetProperties();
		setXScale(Float.parseFloat(super.getProperties().getProperty("XScale", "1")));
		setYScale(Float.parseFloat(super.getProperties().getProperty("YScale", "1")));
	}

	public String getName() {
		return "Dot Stratified";
	}
}
