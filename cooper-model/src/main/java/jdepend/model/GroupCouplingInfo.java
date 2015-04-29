package jdepend.model;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.util.MathUtil;

public class GroupCouplingInfo {

	private List<GroupCouplingItem> groupCouplingItems = new ArrayList<GroupCouplingItem>();

	private List<Float> differences = new ArrayList<Float>();

	private Float averageDifference;

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

	public Float getAverageDifference() {
		if (this.averageDifference == null) {
			this.averageDifference = 0F;
			if (differences != null) {
				if (differences.size() == 1) {
					this.averageDifference = differences.get(0);
				} else {
					Float sumDifference = 0F;
					for (Float difference : differences) {
						sumDifference += difference;
					}
					if (MathUtil.isZero(sumDifference)) {
						this.averageDifference = 0F;
					} else {
						this.averageDifference = sumDifference / differences.size();
					}
				}
			}
		}
		return averageDifference;
	}
}
