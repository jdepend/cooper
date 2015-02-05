package jdepend.model;

import jdepend.framework.util.MetricsFormat;

public abstract class ObjectMeasured implements Measurable {

	/**
	 * 得到特定指标的值
	 * 
	 * @param metrics
	 * @return
	 */
	public Object getValue(String metrics) {

		switch (metrics) {
		case MetricsMgr.Name:
			return this.getName();
		case MetricsMgr.Title:
			return this.getTitle();
		case MetricsMgr.LC:
			return this.getLineCount();
		case MetricsMgr.CN:
			return this.getClassCount();
		case MetricsMgr.AC:
			return this.getAbstractClassCount();
		case MetricsMgr.CC:
			return this.getConcreteClassCount();
		case MetricsMgr.Ca:
			return this.getAfferentCoupling();
		case MetricsMgr.Ce:
			return this.getEfferentCoupling();
		case MetricsMgr.A:
			return MetricsFormat.toFormattedMetrics(this.getAbstractness());
		case MetricsMgr.I:
			return MetricsFormat.toFormattedMetrics(this.getStability());
		case MetricsMgr.D:
			return MetricsFormat.toFormattedMetrics(this.getDistance());
		case MetricsMgr.V:
			return MetricsFormat.toFormattedMetrics(this.getVolatility());
		case MetricsMgr.Cycle:
			if (this.getContainsCycle()) {
				return MetricsMgr.Cyclic;
			} else {
				return MetricsMgr.NoValue;
			}
		case MetricsMgr.Cohesion:
			return MetricsFormat.toFormattedMetrics(this.getCohesion());
		case MetricsMgr.Coupling:
			return MetricsFormat.toFormattedMetrics(this.getCoupling());
		case MetricsMgr.Balance:
			return MetricsFormat.toFormattedMetrics(this.getBalance());
		case MetricsMgr.Encapsulation:
			return MetricsFormat.toFormattedMetrics(this.getEncapsulation());

		default:
			if (this.extendMetrics(metrics) != null) {
				return MetricsFormat.toFormattedMetrics(this.extendMetrics(metrics).getMetrics());
			} else {
				return null;
			}
		}

	}
}
