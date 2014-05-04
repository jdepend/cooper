/*
 * Created on Aug 11, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.wilmascope.gmlparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.wilmascope.columnlayout.ColumnCluster;
import org.wilmascope.columnlayout.NodeColumnLayout;
import org.wilmascope.control.GraphControl;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.global.GlobalConstants;

/**
 * @author dwyer
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
class AugmentedGMLClient implements AugmentedGraphClient {
	Hashtable nodes = new Hashtable();
	GraphControl.Cluster root;
	Vector seriesKeys;
	boolean fixedColumnWidthMode = GlobalConstants.getInstance().getBooleanValue("FixedColumnWidthMode");

	public void setSeriesKeys(Vector keys) {
		if (keys == null || keys.size() == 0) {
			throw new Error("No series keys found!");
		}
		this.seriesKeys = keys;
	}

	public AugmentedGMLClient(GraphControl.Cluster root) {
		this.root = root;
	}

	public void addEdge(String startID, String endID, String label) {
		System.out.println("Adding Edge: " + startID + " " + endID + " " + label);
	}

	public void addEdge(String startID, String endID) {
		ColumnCluster startCluster = (ColumnCluster) nodes.get(startID);
		ColumnCluster endCluster = (ColumnCluster) nodes.get(endID);
		GraphControl.Node startNodes[] = startCluster.getClusterFacade().getNodes();
		GraphControl.Node endNodes[] = endCluster.getClusterFacade().getNodes();
		for (int i = 0; i < startNodes.length; i++) {
			float r = startNodes[i].getRadius() + endNodes[i].getRadius() + (float) Math.random();
			// System.out.println("Radius = " + r);
			if (r > 0.7f) {
				GraphControl.Edge e = root.addEdge(startNodes[i], endNodes[i], "SplineTube", r / 100f);
				float red = r / 3f > 1.0f ? 1.0f : r / 3f;
				float blue = r / 3f > 1.0f ? 0f : 1.0f - r / 3f;
				e.setColour(red, 0, blue);
			}
		}
	}

	public void addNode(String id, String label) {
		GlobalConstants constants = GlobalConstants.getInstance();
		ColumnCluster c = new ColumnCluster(root, 100f, 1f, 0, "Column Cluster", "Tube Node");
		if (label == null)
			label = " ";
		c.setLabel(new String[] { label });
		GraphControl.Node n = null;
		for (int i = 0; i < seriesKeys.size(); i++) {
			n = c.addStraightNode(3f);
			n.setColour(0.9f, 0.9f, 1f * (float) i / (float) seriesKeys.size());
			((NodeColumnLayout) n.getNode().getLayout()).setHeight(1f);
		}
		nodes.put(id, c);
	}

	public void addNode(String id, String label, Hashtable series) {
		GlobalConstants constants = GlobalConstants.getInstance();
		ColumnCluster c = new ColumnCluster(root, 1f, 1f, 0, constants.getProperty("ImportedColumnClusterStyle"),
				constants.getProperty("ImportedColumnNodeStyle"));
		if (label == null)
			label = " ";
		c.setLabel(new String[] { label });
		GraphControl.Node n = null;
		float max = 0;
		if (fixedColumnWidthMode) {
			for (Enumeration e = series.elements(); e.hasMoreElements();) {
				float r = Float.parseFloat((String) e.nextElement());
				if (r > max) {
					max = r;
				}
			}
		}
		for (Enumeration e = series.elements(); e.hasMoreElements();) {
			float r = Float.parseFloat((String) e.nextElement());
			if (fixedColumnWidthMode) {
				r = 200 * r / max;
			}
			if (constants.getProperty("ImportedColumnClusterStyle").equals("Box Column Cluster")) {
				r = 50f * (float) Math.log(r);
			} else {
				r = (float) Math.sqrt(r);
			}
			n = c.addStraightNode(r);
		}
		nodes.put(id, c);
	}

}

public class GMLLoader {
	GraphControl gc;
	AugmentedGMLParser parser = null;

	public GMLLoader(GraphControl gc, String fileName) {
		this.gc = gc;
		GraphControl.Cluster r = gc.getRootCluster();
		r.deleteAll();
		r.freeze();
		// r = r.addCluster();
		// r.hide();
		org.wilmascope.dotlayout.DotLayout d = new org.wilmascope.dotlayout.DotLayout();
		d.setXScale(20f);
		d.setYScale(6f);
		r.setLayoutEngine(d);
		try {
			FileInputStream f = new FileInputStream(fileName);
			if (parser == null) {
				parser = new AugmentedGMLParser(f);
			} else {
				parser.ReInit(f);
			}

			AugmentedGMLClient augmentedGMLClient = new AugmentedGMLClient(gc.getRootCluster());
			parser.graph(augmentedGMLClient);

			String[] strataNames = new String[0];
			strataNames = (String[]) augmentedGMLClient.seriesKeys.toArray(strataNames);
			r.setUserData(strataNames);
			r.unfreeze();
		} catch (FileNotFoundException e) {
			WilmaMain.showErrorDialog("File Not Found", e);
		} catch (ParseException e) {
			WilmaMain.showErrorDialog("Parse Error", e);
		}
	}
}
