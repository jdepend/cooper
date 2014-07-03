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
		if (metrics.equals(MetricsMgr.Name)) {
			return this.getName();
		} else if (metrics.equals(MetricsMgr.Title)) {
			return this.getTitle();
		} else if (metrics.equals(MetricsMgr.LC)) {
			return this.getLineCount();
		} else if (metrics.equals(MetricsMgr.CN)) {
			return this.getClassCount();
		} else if (metrics.equals(MetricsMgr.AC)) {
			return this.getAbstractClassCount();
		} else if (metrics.equals(MetricsMgr.CC)) {
			return this.getConcreteClassCount();
		} else if (metrics.equals(MetricsMgr.Ca)) {
			return this.getAfferentCoupling();
		} else if (metrics.equals(MetricsMgr.Ce)) {
			return this.getEfferentCoupling();
		} else if (metrics.equals(MetricsMgr.A)) {
			return MetricsFormat.toFormattedMetrics(this.getAbstractness());
		} else if (metrics.equals(MetricsMgr.I)) {
			return MetricsFormat.toFormattedMetrics(this.getStability());
		} else if (metrics.equals(MetricsMgr.D)) {
			return MetricsFormat.toFormattedMetrics(this.getDistance());
		} else if (metrics.equals(MetricsMgr.V)) {
			return MetricsFormat.toFormattedMetrics(this.getVolatility());
		} else if (metrics.equals(MetricsMgr.Cycle)) {
			if (this.getContainsCycle()) {
				return MetricsMgr.Cyclic;
			} else {
				return MetricsMgr.NoValue;
			}
		} else if (metrics.equals(MetricsMgr.Cohesion)) {
			return MetricsFormat.toFormattedMetrics(this.getCohesion());
		} else if (metrics.equals(MetricsMgr.Coupling)) {
			return MetricsFormat.toFormattedMetrics(this.getCoupling());
		} else if (metrics.equals(MetricsMgr.Balance)) {
			return MetricsFormat.toFormattedMetrics(this.getBalance());
		} else if (metrics.equals(MetricsMgr.Encapsulation)) {
			return MetricsFormat.toFormattedMetrics(this.getEncapsulation());
		} else if (metrics.equals(MetricsMgr.OO)) {
			return MetricsFormat.toFormattedMetrics(this.getObjectOriented());
		} else if (this.extendMetrics(metrics) != null) {
			return MetricsFormat.toFormattedMetrics(this.extendMetrics(metrics).getMetrics());
		} else {
			return null;
		}
	}
}
