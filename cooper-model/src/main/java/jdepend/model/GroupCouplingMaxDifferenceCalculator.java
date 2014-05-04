package jdepend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分组耦合最大顺序差值计算器
 * 
 * @author wangdg
 * 
 */
public final class GroupCouplingMaxDifferenceCalculator {

	private List<GroupCouplingItem> groupCouplingInfos = new ArrayList<GroupCouplingItem>();

	private List<Float> differences = new ArrayList<Float>();

	private Float maxDifference = 0F;

	public GroupCouplingMaxDifferenceCalculator(JavaClass self) {
		// 收集有关系的分析单元
		List<JDependUnit> relationUnits = new ArrayList<JDependUnit>();
		for (JavaClass javaClass : self.getEfferents()) {
			if (!self.getComponent().containsClass(javaClass) && !relationUnits.contains(javaClass.getComponent())) {
				relationUnits.add(javaClass.getComponent());

			}
		}
		for (JavaClass javaClass : self.getAfferents()) {
			if (!self.getComponent().containsClass(javaClass) && !relationUnits.contains(javaClass.getComponent())) {
				relationUnits.add(javaClass.getComponent());
			}
		}
		// 计算分组耦合值
		for (JDependUnit unit : relationUnits) {
			GroupCouplingItem info = new GroupCouplingItem(unit.getName(), self.coupling(unit));
			if (info.coupling > 0F) {
				info.addDetail(self.caCouplingDetail(unit));
				info.addDetail(self.ceCouplingDetail(unit));
			}
			groupCouplingInfos.add(info);
		}

		// 计算分组最大顺序差值
		if (groupCouplingInfos.size() == 1) {
			differences.add(groupCouplingInfos.get(0).coupling);
			maxDifference = groupCouplingInfos.get(0).coupling;
		} else {
			Collections.sort(groupCouplingInfos);
			float difference;
			for (int i = 0; i < groupCouplingInfos.size() - 1; i++) {
				difference = groupCouplingInfos.get(i + 1).coupling - groupCouplingInfos.get(i).coupling;
				this.differences.add(difference);
				if (difference > maxDifference) {
					maxDifference = difference;
				}
			}
		}
	}

	public List<GroupCouplingItem> getGroupCouplingItems() {
		return groupCouplingInfos;
	}

	public List<Float> getDifferences() {
		return differences;
	}

	public Float getMaxDifference() {
		return maxDifference;
	}
}
