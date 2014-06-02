package jdepend.report.util;

import jdepend.model.JavaClassRelationItem;

public class ReportUtil {

	public static float calCouplingIntensity(JavaClassRelationItem item) {
		// 只计算组件间耦合
		if (!item.getCurrent().getComponent().containsClass(item.getDepend())) {
			return item.getRelationIntensity();
		} else {
			return 0F;
		}
	}

	public static float calCohesionIntensity(JavaClassRelationItem item) {
		// 只计算组件内内聚
		if (item.getCurrent().getComponent().containsClass(item.getDepend())) {
			return item.getRelationIntensity();
		} else {
			return 0F;
		}
	}
}
