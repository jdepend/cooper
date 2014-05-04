package org.wilmascope.dotparser;

public interface GraphClient {
	public EdgeClient addEdge(NodeClient start, NodeClient end);

	public NodeClient addNode(String id);

	public void setBoundingBox(String bounds);
}
