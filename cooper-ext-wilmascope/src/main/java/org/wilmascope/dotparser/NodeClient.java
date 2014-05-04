package org.wilmascope.dotparser;

import java.util.StringTokenizer;

public abstract class NodeClient {
	public NodeClient(String id) {
		this.id = id;
	}

	public abstract void setPosition(int x, int y);

	public void setPosition(String s) {
		StringTokenizer st = new StringTokenizer(s, ",");
		setPosition(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
	}

	public String getID() {
		return id;
	}

	public String id;
}
