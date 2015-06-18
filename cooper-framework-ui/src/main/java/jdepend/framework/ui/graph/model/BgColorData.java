package jdepend.framework.ui.graph.model;

import java.util.ArrayList;
import java.util.List;

public final class BgColorData {

	private String type;

	private List<RegionColor> data = new ArrayList<RegionColor>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<RegionColor> getData() {
		return data;
	}

	public void addData(RegionColor region) {
		this.data.add(region);
	}
}
