package jdepend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdepend.framework.util.MathUtil;
import jdepend.model.util.FangCha;

public class GroupCouplingInfo {

	private List<GroupCouplingItem> groupCouplingItems;

	private List<Float> differences;

	private Float averageDifference;

	public GroupCouplingInfo(List<GroupCouplingItem> groupCouplingItems) {

		this.groupCouplingItems = groupCouplingItems;

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

		this.averageDifference = 0F;
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

		List<Double> nums = new ArrayList<Double>();
		for (GroupCouplingItem item : this.groupCouplingItems) {
			nums.add(new Double(item.coupling));
		}
	}

	public List<GroupCouplingItem> getGroupCouplingItems() {
		return groupCouplingItems;
	}

	public List<Float> getDifferences() {
		return differences;
	}

	public Float getAverageDifference() {
		return averageDifference;
	}
}
