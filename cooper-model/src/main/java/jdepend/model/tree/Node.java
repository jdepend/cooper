package jdepend.model.tree;

import java.io.Serializable;
import java.util.List;

public interface Node extends Comparable<Node>, Serializable {

	public String getName();

	public String getPath();

	public Integer getSize();

	public Integer getLayer();

	public void setLayer(Integer layer);

	public void addLayer(Integer layer);

	public int compareTo(Node n);

	public Node getParent();

	public void setParent(Node parent);

	public List<? extends Node> getChildren();

	public void addChild(Node child);
}
