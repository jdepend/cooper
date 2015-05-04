package jdepend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdepend.framework.util.MathUtil;

public class GroupCouplingInfo {

	private List<GroupCouplingItem> groupCouplingItems;

	private List<Float> differences;

	private Float averageDifference;

	public List<GroupCouplingItem> getGroupCouplingItems() {
		return groupCouplingItems;
	}

	public void setGroupCouplingItems(List<GroupCouplingItem> groupCouplingItems) {
		this.groupCouplingItems = groupCouplingItems;
	}

	public List<Float> getDifferences() {
		if (this.differences == null) {
			this.calDifferences();
		}
		return differences;
	}

	public Float getAverageDifference() {
		if (this.averageDifference == null) {
			this.averageDifference = 0F;
			if (getDifferences().size() == 1) {
				this.averageDifference = getDifferences().get(0);
			} else {
				Float sumDifference = 0F;
				for (Float difference : getDifferences()) {
					sumDifference += difference;
				}
				if (MathUtil.isZero(sumDifference)) {
					this.averageDifference = 0F;
				} else {
					this.averageDifference = sumDifference / getDifferences().size();
				}
			}
		}
		return averageDifference;
	}

	/**
	 * 计算分组顺序差值
	 */
	private void calDifferences() {
		differences = new ArrayList<Float>();
		// 计算分组顺序差值
		if (groupCouplingItems.size() == 1) {
			differences.add(groupCouplingItems.get(0).coupling);
		} else {
			Collections.sort(groupCouplingItems);
			float difference;
			for (int i = 0; i < groupCouplingItems.size() - 1; i++) {
				difference = groupCouplingItems.get(i + 1).coupling - groupCouplingItems.get(i).coupling;
				differences.add(difference);
			}
		}
	}
}
