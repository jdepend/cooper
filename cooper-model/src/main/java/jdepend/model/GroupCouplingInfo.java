package jdepend.model;

import java.util.ArrayList;
import java.util.List;

public class GroupCouplingInfo {
	
	private List<GroupCouplingItem> groupCouplingItems = new ArrayList<GroupCouplingItem>();

	private List<Float> differences = new ArrayList<Float>();

	private Float maxDifference = 0F;

	public List<GroupCouplingItem> getGroupCouplingItems() {
		return groupCouplingItems;
	}

	public void setGroupCouplingItems(List<GroupCouplingItem> groupCouplingItems) {
		this.groupCouplingItems = groupCouplingItems;
	}

	public List<Float> getDifferences() {
		return differences;
	}

	public void setDifferences(List<Float> differences) {
		this.differences = differences;
	}

	public Float getMaxDifference() {
		return maxDifference;
	}

	public void setMaxDifference(Float maxDifference) {
		this.maxDifference = maxDifference;
	}
}
