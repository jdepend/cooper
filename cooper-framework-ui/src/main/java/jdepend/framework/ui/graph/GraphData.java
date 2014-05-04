package jdepend.framework.ui.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class GraphData implements Serializable {

	private List<GraphDataItem> items = new ArrayList<GraphDataItem>();

	private int colCount;

	public int getColCount() {
		return colCount;
	}

	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	public List<GraphDataItem> getItems() {
		return items;
	}

	public void addItem(GraphDataItem item) {
		if (!items.contains(item)) {
			items.add(item);
		}
	}

	public List<String> getGroups() {
		List<String> groups = new ArrayList<String>();
		for (GraphDataItem item : items) {
			if (item.getGroup() != null && !groups.contains(item.getGroup())) {
				groups.add(item.getGroup());
			}
		}
		return groups;
	}

	public List<GraphDataItem> getTheGroupItems(String group) {
		List<GraphDataItem> rtns = new ArrayList<GraphDataItem>();
		for (GraphDataItem item : items) {
			if (item.getGroup().equals(group)) {
				rtns.add(item);
			}
		}
		return rtns;
	}

}
