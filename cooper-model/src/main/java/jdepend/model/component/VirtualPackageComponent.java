package jdepend.model.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jdepend.model.GroupCouplingInfo;
import jdepend.model.GroupCouplingItem;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;
import jdepend.model.Relation;
import jdepend.model.SubJDependUnit;

public class VirtualPackageComponent extends VirtualComponent implements SubJDependUnit {

	private Float cohesion;

	private Float balance;

	private GroupCouplingInfo groupCouplingInfo;

	public VirtualPackageComponent(JavaPackage javaPackage) {
		super(javaPackage.getName());

		for (JavaClass javaClass : javaPackage.getClasses()) {
			this.joinJavaClass(javaClass);
			if (this.getResult() == null) {
				this.setResult(javaClass.getResult());
			}
		}
	}

	@Override
	public float getBalance() {
		if (this.balance == null) {
			this.calculate();
		}
		return balance;
	}

	public JavaPackage getJavaPackage() {
		return this.getJavaPackages().iterator().next();
	}

	@Override
	public float getCohesion() {
		if (this.cohesion == null) {
			this.calculate();
		}
		return cohesion;
	}

	@Override
	public GroupCouplingInfo getGroupCouplingInfo() {
		if (this.groupCouplingInfo == null) {
			this.calculate();
		}
		return groupCouplingInfo;
	}

	private void calculate() {
		// 得到包所属的组件包含的包集合
		Collection<JavaPackage> javaPackages = this.getJavaPackage().getClasses().iterator().next().getComponent()
				.getJavaPackages();

		cohesion = 0F;
		List<GroupCouplingItem> groupCouplingItems = new ArrayList<GroupCouplingItem>();

		for (Relation relation : this.getRelations()) {
			// 判断关系的目标端是否在所属的组件中，在就计算内聚值，外就计算分组耦合值
			if (javaPackages.contains(relation.getOpposite(this).getJavaPackages().iterator().next())) {
				cohesion += relation.getIntensity();
			} else {
				GroupCouplingItem info = new GroupCouplingItem(relation.getDepend().getName(), relation.getIntensity());
				info.addDetail(relation.getItems());
				groupCouplingItems.add(info);
			}
		}

		groupCouplingInfo = new GroupCouplingInfo();
		groupCouplingInfo.setGroupCouplingItems(groupCouplingItems);

		List<Float> differences = new ArrayList<Float>();
		Float maxDifference = 0F;
		// 计算分组最大顺序差值
		if (groupCouplingItems.size() == 1) {
			differences.add(groupCouplingItems.get(0).coupling);
			maxDifference = groupCouplingItems.get(0).coupling;
		} else {
			Collections.sort(groupCouplingItems);
			float difference;
			for (int i = 0; i < groupCouplingItems.size() - 1; i++) {
				difference = groupCouplingItems.get(i + 1).coupling - groupCouplingItems.get(i).coupling;
				differences.add(difference);
				if (difference > maxDifference) {
					maxDifference = difference;
				}
			}
		}
		groupCouplingInfo.setMaxDifference(maxDifference);
		groupCouplingInfo.setDifferences(differences);

		if (cohesion == 0F) {
			if (maxDifference == 0F) {
				balance = 0.5F;
			} else {
				balance = 0F;
			}
		} else {
			balance = cohesion / (cohesion + maxDifference);
		}
	}
}
