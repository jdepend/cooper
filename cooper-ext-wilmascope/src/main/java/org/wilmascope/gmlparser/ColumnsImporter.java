package org.wilmascope.gmlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import org.wilmascope.columnlayout.ColumnCluster;
import org.wilmascope.columnlayout.NodeColumnLayout;
import org.wilmascope.control.GraphControl;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.dotlayout.DotLayout;
import org.wilmascope.global.GlobalConstants;
import org.wilmascope.graph.Edge;
import org.wilmascope.graph.EdgeList;
import org.wilmascope.view.GraphElementView;

/**
 * @author dwyer
 * 
 *         Imports a set of GML files and compiles them into a stratified graph
 *         by matching node IDs.
 */
class ColumnGraphClient implements GraphClient {
	private static String[] toStringArray(ArrayList l) {
		String[] a = new String[l.size()];
		for (int i = 0; i < l.size(); i++) {
			a[i] = (String) l.get(i);
		}
		return a;
	}

	class TransientColumn {
		ColumnCluster c;

		GraphControl.Node n;

		int level;
	}

	Hashtable edgeColumns;

	Hashtable columns;

	Hashtable nodes = new Hashtable();

	int level, maxLevel;

	GraphControl.Cluster r;

	GlobalConstants constants = GlobalConstants.getInstance();

	public ColumnGraphClient(GraphControl.Cluster root, Hashtable columns, Hashtable edgeColumns, int level,
			int maxLevel) {
		this.columns = columns;
		this.edgeColumns = edgeColumns;
		this.level = level;
		this.maxLevel = maxLevel;
		this.r = root;
	}

	public void addNode(String id, String label, float x, float y) {
		try {
			TransientColumn c = (TransientColumn) columns.get(id);
			boolean box = false;
			if (label.indexOf(".") >= 0) {
				box = true;
			}
			float radius = 50f;
			if (c == null) {
				c = new TransientColumn();
				if (box) {
					c.c = new ColumnCluster(r, 10f, 10f, level, "Box Column Cluster", "Box Node");
					c.c.setLabel(new String[] { label });
				} else {
					c.c = new ColumnCluster(r, 50f, radius, level, "Column Cluster", "Tube Node");
					// constants.getProperty("ImportedColumnClusterStyle"),
					// constants.getProperty("ImportedColumnNodeStyle"));
					StringTokenizer st = new StringTokenizer(label, "-");
					ArrayList labelLines = new ArrayList();
					String line = new String();
					while (st.hasMoreTokens()) {
						// if the elements are small then we can fit more than
						// one per line
						String element = st.nextToken();
						if (line.length() == 0) {
							line = element;
						} else if (line.length() + element.length() < 9) {
							line = line + "-" + element;
						} else {
							labelLines.add(line);
							line = element;
						}
					}
					labelLines.add(line);
					c.c.setLabel(toStringArray(labelLines));
				}
				c.level = level - 1;
				columns.put(id, c);
			}
			while (c.level < level) {
				c.level++;
				GraphControl.Node n = null;
				if (c.level == level) {
					if (box) {
						n = c.c.addStraightNode(200f);
						if (c.level % 2 == 0) {
							n.setColour(0.9f, 0.9f, 0.7f);
							// 1f * (float) level / (float) maxLevel);
						} else {
							n.setColour(0.9f, 0.6f, 0.9f);
						}
					} else {
						n = c.c.addStraightNode(radius);
						if (c.level % 2 == 0) {
							n.setColour(0.9f, 0.9f, 0.7f);
						} else {
							n.setColour(0.9f, 0.6f, 0.9f);
						}
					}
					nodes.put(id, n);
				} else {
					c.c.skipLevel();
				}
			}
		} catch (Exception e) {
			WilmaMain.showErrorDialog("Error adding node: id=" + id + " label=" + label, e);
		}
	}

	public void addEdge(String startID, String endID, String label, String arrowPosition) {
		TransientColumn c = (TransientColumn) edgeColumns.get(label + "," + startID + "," + endID);

		if (c == null) {
			c = new TransientColumn();
			c.c = new ColumnCluster(r, 10f, 10f, level, "Box Column Cluster", "Box Node");
			c.c.setLabel(new String[] { label });
			edgeColumns.put(label + "," + startID + "," + endID, c);
			c.level = level - 1;

		}
		while (c.level < level) {
			c.level++;
			if (c.level == level) {
				c.n = c.c.addStraightNode(200f);
				c.n.setColour(0.8f * (float) level / (float) maxLevel, 0.8f * (float) level / (float) maxLevel, 1f);
			} else {
				c.c.skipLevel();
			}
		}
		GraphControl.Node start = (GraphControl.Node) nodes.get(startID);
		GraphControl.Node end = (GraphControl.Node) nodes.get(endID);
		GraphControl.Edge e1 = null;
		GraphControl.Edge e2 = null;
		float radius = 0.04f;
		String edgeType = "SplineTube";
		if (arrowPosition.equals("both")) {
			// in the following line start and c.n should be swapped for the
			// arrow to
			// be correct...
			// however that's not really what we want the layout to look like so
			// until
			// I make a splineTube
			// with the arrow at the wrong end ... somehow... it stays
			e1 = r.addEdge(start, c.n, edgeType, radius);
			e2 = r.addEdge(c.n, end, edgeType, radius);
		} else if (arrowPosition.equals("last")) {
			e1 = r.addEdge(start, c.n, edgeType, radius);
			e2 = r.addEdge(c.n, end, edgeType, radius);
		}
		e1.setColour(0.3f, 0.3f, 0.3f);
		e2.setColour(0.3f, 0.3f, 0.3f);
		/*
		 * 0.8f * (float) level / (float) maxLevel, 0.8f * (float) level /
		 * (float) maxLevel, 1f); e2.setColour( 0.8f * (float) level / (float)
		 * maxLevel, 0.8f * (float) level / (float) maxLevel, 1f);
		 */
	}

	public void addEdge(String startID, String endID, String arrowPosition) {
		GraphControl.Node start = (GraphControl.Node) nodes.get(startID);
		GraphControl.Node end = (GraphControl.Node) nodes.get(endID);
		if (start == null) {
			throw new Error("ERROR: couldn't find node: " + startID);
		}
		if (end == null) {
			throw new Error("ERROR: couldn't find node: " + endID);
		}
		GraphControl.Edge e;
		if (arrowPosition.equals("both")) {
			e = r.addEdge(start, end, "SplineTube"); // ,(float)Math.random()/10f);
			// r.addEdge(end, start,"Arrow");
		} else if (arrowPosition.equals("last")) {
			e = r.addEdge(start, end, "SplineTube"); // ,(float)Math.random()/10f);
		} else {
			e = r.addEdge(start, end, "SplineTube"); // ,(float)Math.random()/10f);
		}
		// e.setColour(0.9f, 0.9f, 1f * (float) level / (float) maxLevel);
		e.setColour(0.3f, 0.3f, 0.3f);
	}

}

public class ColumnsImporter {
	static GMLParser parser;

	public static void load(GraphControl g, String dirName) {

		try {
			File directory = new File(dirName);
			File[] files;
			files = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".gml");
				}
			});
			if (files.length == 0) {
				JOptionPane.showMessageDialog(g.getGraphCanvas().getParent(), "No GML files found in chosen directory",
						"No Input Files Found Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int level = 0;
			GraphControl.Cluster r = g.getRootCluster();
			r.deleteAll();
			r.freeze();
			String[] strataNames = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				strataNames[i] = files[i].getName();
			}
			r.setUserData(strataNames);
			// r = r.addCluster();
			// r.hide();
			org.wilmascope.dotlayout.DotLayout d = new DotLayout();
			d.init(r.getCluster());
			d.setXScale(18f);
			d.setYScale(8f);
			r.setLayoutEngine(d);
			Hashtable columns = new Hashtable();
			Hashtable edgeColumns = new Hashtable();
			for (int i = 0; i < files.length; i++) {
				System.out.println("loading " + files[i].getAbsolutePath());
				FileInputStream f = new FileInputStream(files[i]);

				if (parser == null) {
					parser = new GMLParser(f);
				} else {
					parser.ReInit(f);
				}

				parser.graph(new ColumnGraphClient(r, columns, edgeColumns, level++, files.length));
			}
			if (columns.size() > 0) {
				r.unfreeze();
				g.centreGraph();
			}
			colourEdgeGroups(r, files.length);
		} catch (FileNotFoundException e) {
			WilmaMain.showErrorDialog("File Not Found", e);
		} catch (ParseException e) {
			WilmaMain.showErrorDialog("Parse Error", e);
		}
	}

	static void colourEdgeGroups(GraphControl.Cluster r, int noStrata) {
		EdgeList edges = r.getCluster().getInternalEdges();
		Hashtable<String, Hashtable<Integer, Edge>> masterEdges = new Hashtable<String, Hashtable<Integer, Edge>>();
		for (Edge e : edges) {
			Integer s = new Integer(((NodeColumnLayout) e.getStart().getLayout()).getStratum());
			String key = e.getStart().getOwner().hashCode() + " " + e.getEnd().getOwner().hashCode();
			Hashtable<Integer, Edge> edgesByStratum = masterEdges.get(key);
			if (edgesByStratum == null) {
				edgesByStratum = new Hashtable<Integer, Edge>();
				masterEdges.put(key, edgesByStratum);
			}
			if (!edgesByStratum.containsKey(s)) {
				edgesByStratum.put(s, e);
			}
		}
		for (String key : masterEdges.keySet()) {
			Hashtable edgesByStratum = (Hashtable) masterEdges.get(key);
			String s = new String();
			TreeSet<Integer> sortedStrata = new TreeSet<Integer>(edgesByStratum.keySet());
			int lastStratum = Integer.MIN_VALUE;
			for (Iterator strata = sortedStrata.iterator(); strata.hasNext();) {
				int stratum = ((Integer) strata.next()).intValue();
				s = s + " " + stratum;
				Edge e = (Edge) edgesByStratum.get(new Integer(stratum));
				if (stratum != 0 && lastStratum != stratum - 1) {
					((GraphElementView) e.getView()).setColour(0, 1f, 0);
					if (lastStratum >= 0) {
						Edge f = (Edge) edgesByStratum.get(new Integer(lastStratum));
						((GraphElementView) f.getView()).setColour(1f, 0, 0);
					}
				}
				if (stratum < (noStrata - 1) && !strata.hasNext()) {
					((GraphElementView) e.getView()).setColour(1f, 0, 0);
				}
				lastStratum = stratum;
			}
			System.out.println(s);
		}
	}
}
