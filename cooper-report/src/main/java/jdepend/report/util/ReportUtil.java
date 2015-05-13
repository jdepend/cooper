package jdepend.report.util;

import jdepend.metadata.JavaClassRelationItem;
import jdepend.model.util.JavaClassUnitUtil;

public class ReportUtil {

	public static float calCouplingIntensity(JavaClassRelationItem item) {
		// 只计算组件间耦合
		if (JavaClassUnitUtil.crossComponent(item)) {
			return item.getRelationIntensity();
		} else {
			return 0F;
		}
	}

	public static float calCohesionIntensity(JavaClassRelationItem item) {
		// 只计算组件内内聚
		if (!JavaClassUnitUtil.crossComponent(item)) {
			return item.getRelationIntensity();
		} else {
			return 0F;
		}
	}
}
