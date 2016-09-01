package jdepend.client.report.util;

import jdepend.metadata.JavaClassRelationItem;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;

public class ReportUtil {

	public static float calCouplingIntensity(JavaClassRelationItem item, AnalysisResult result) {
		// 只计算组件间耦合
		if (JavaClassUnitUtil.crossComponent(item, result)) {
			return item.getRelationIntensity();
		} else {
			return 0F;
		}
	}

	public static float calCohesionIntensity(JavaClassRelationItem item, AnalysisResult result) {
		// 只计算组件内内聚
		if (!JavaClassUnitUtil.crossComponent(item, result)) {
			return item.getRelationIntensity();
		} else {
			return 0F;
		}
	}
}
