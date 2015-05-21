package jdepend.model.util;

import jdepend.model.CalculateMetricsTool;
import jdepend.model.JDependUnit;

public class MetricsTool {

	/**
	 * @return instabilityWithCount (0-1).
	 */
	public static Float instabilityWithCount(JDependUnit unit) {
		return new CalculateMetricsTool(unit).stabilityWithCount();
	}

	/**
	 * @return instabilityWithIntensity (0-1).
	 */
	public static Float instabilityWithIntensity(JDependUnit unit) {
		return new CalculateMetricsTool(unit).stabilityWithIntensity();
	}

	public static Float instabilityWithCountScale() {
		return CalculateMetricsTool.stabilityWithCountScale;
	}

}
