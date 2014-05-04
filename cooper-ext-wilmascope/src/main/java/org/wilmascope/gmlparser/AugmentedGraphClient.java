package org.wilmascope.gmlparser;

import java.util.Hashtable;
import java.util.Vector;

public interface AugmentedGraphClient {
	public void setSeriesKeys(Vector keys);

	public void addNode(String id, String label);

	public void addNode(String id, String label, Hashtable series);

	public void addEdge(String startID, String endID, String label);

	public void addEdge(String startID, String endID);
}
