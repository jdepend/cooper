package jdepend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jdepend.framework.util.MathUtil;
import jdepend.model.component.PackageSubJDependUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;

/**
 * 分组信息计算器
 * 
 * @author wangdg
 * 
 */
public final class GroupInfoCalculator {

	private GroupCouplingInfo groupCouplingInfo;

	private GroupCohesionInfo groupCohesionInfo;

	private Float balance;

	public GroupInfoCalculator(JavaClassUnit self) {

		List<GroupCouplingItem> groupCouplingItems = new ArrayList<GroupCouplingItem>();
		// 收集有关系的分析单元
		Collection<Component> relationComponents = new HashSet<Component>();
		for (JavaClassUnit javaClass : self.getEfferents()) {
			if (!relationComponents.contains(javaClass.getComponent())) {
				relationComponents.add(javaClass.getComponent());
			}
		}
		for (JavaClassUnit javaClass : self.getAfferents()) {
			if (!relationComponents.contains(javaClass.getComponent())) {
				relationComponents.add(javaClass.getComponent());
			}
		}
		// 计算分组耦合值
		for (Component component : relationComponents) {
			RelationDetail caCouplingDetail = self.caCouplingDetail(component);
			RelationDetail ceCouplingDetail = self.ceCouplingDetail(component);
			float caCoupling = caCouplingDetail.getIntensity();
			float ceCoupling = ceCouplingDetail.getIntensity();
			float coupling = caCoupling + ceCoupling;
			GroupCouplingItem info = new GroupCouplingItem(component.getName(), coupling);
			if (caCoupling > 0F) {
				info.addDetail(caCouplingDetail.getItems());
			}
			if (ceCoupling > 0F) {
				info.addDetail(ceCouplingDetail.getItems());
			}
			groupCouplingItems.add(info);
		}

		groupCouplingInfo = new GroupCouplingInfo();
		groupCouplingInfo.setGroupCouplingItems(groupCouplingItems);

		// 计算分组内聚信息
		this.groupCohesionInfo = new GroupCohesionInfo();
		List<GroupCohesionItem> groupCohesionItems = new ArrayList<GroupCohesionItem>();

		for (JavaClassRelationItem relationItem : self.getJavaClass().getCeItems()) {
			if (self.getComponent().containsClass(relationItem.getTarget())) {
				GroupCohesionItem item = new GroupCohesionItem(relationItem.getTarget().getName());
				item.addItem(relationItem);
				groupCohesionItems.add(item);
			}
		}
		for (JavaClassRelationItem relationItem : self.getJavaClass().getCaItems()) {
			if (self.getComponent().containsClass(relationItem.getSource())) {
				GroupCohesionItem item = new GroupCohesionItem(relationItem.getSource().getName());
				item.addItem(relationItem);
				groupCohesionItems.add(item);
			}
		}

		this.groupCohesionInfo.setGroupCohesionItems(groupCohesionItems);
	}

	public GroupInfoCalculator(PackageSubJDependUnit self) {

		AnalysisResult result = self.getResult();
		// 得到包所属的组件包含的包集合
		Collection<JavaPackage> javaPackages = result
				.getTheClass(self.getJavaPackage().getClasses().iterator().next().getId()).getComponent()
				.getJavaPackages();

		Float cohesion = 0F;
		List<GroupCohesionItem> groupCohesionItems = new ArrayList<GroupCohesionItem>();
		List<GroupCouplingItem> groupCouplingItems = new ArrayList<GroupCouplingItem>();

		for (Relation relation : self.getRelations()) {
			// 判断关系的目标端是否在所属的组件中，在就计算内聚值，外就计算分组耦合值
			if (javaPackages.contains(relation.getOpposite(self).getJavaPackages().iterator().next())) {
				cohesion += relation.getIntensity();
				GroupCohesionItem item = new GroupCohesionItem(relation.getOpposite(self).getName());
				item.addDetail(relation.getItems());
				groupCohesionItems.add(item);
			} else {
				GroupCouplingItem item = new GroupCouplingItem(relation.getOpposite(self).getName(),
						relation.getIntensity());
				item.addDetail(relation.getItems());
				groupCouplingItems.add(item);
			}
		}

		this.groupCohesionInfo = new GroupCohesionInfo();
		this.groupCohesionInfo.setGroupCohesionItems(groupCohesionItems);

		this.groupCouplingInfo = new GroupCouplingInfo();
		this.groupCouplingInfo.setGroupCouplingItems(groupCouplingItems);
	}

	private void calBalance() {
		Float averageDifference = groupCouplingInfo.getAverageDifference();
		Float cohesion = this.groupCohesionInfo.getCohesion();
		if (MathUtil.isZero(cohesion)) {
			if (MathUtil.isZero(averageDifference)) {
				balance = 0.5F;
			} else {
				balance = 0F;
			}
		} else {
			balance = cohesion / (cohesion + averageDifference);
		}
	}

	public GroupCouplingInfo getGroupCouplingInfo() {
		return groupCouplingInfo;
	}

	public GroupCohesionInfo getGroupCohesionInfo() {
		return groupCohesionInfo;
	}

	public Float getBalance() {
		if (this.balance == null) {
			this.calBalance();
		}
		return balance;
	}
}
