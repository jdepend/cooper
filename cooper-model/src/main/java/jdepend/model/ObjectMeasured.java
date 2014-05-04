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
			return this.afferentCoupling();
		} else if (metrics.equals(MetricsMgr.Ce)) {
			return this.efferentCoupling();
		} else if (metrics.equals(MetricsMgr.A)) {
			return MetricsFormat.toFormattedMetrics(this.abstractness());
		} else if (metrics.equals(MetricsMgr.I)) {
			return MetricsFormat.toFormattedMetrics(this.stability());
		} else if (metrics.equals(MetricsMgr.D)) {
			return MetricsFormat.toFormattedMetrics(this.distance());
		} else if (metrics.equals(MetricsMgr.V)) {
			return MetricsFormat.toFormattedMetrics(this.volatility());
		} else if (metrics.equals(MetricsMgr.Cycle)) {
			if (this.containsCycle()) {
				return MetricsMgr.Cyclic;
			} else {
				return MetricsMgr.NoCyclic;
			}
		} else if (metrics.equals(MetricsMgr.Cohesion)) {
			return MetricsFormat.toFormattedMetrics(this.cohesion());
		} else if (metrics.equals(MetricsMgr.Coupling)) {
			return MetricsFormat.toFormattedMetrics(this.coupling());
		} else if (metrics.equals(MetricsMgr.Balance)) {
			return MetricsFormat.toFormattedMetrics(this.balance());
		} else if (metrics.equals(MetricsMgr.Encapsulation)) {
			return MetricsFormat.toFormattedMetrics(this.encapsulation());
		} else if (metrics.equals(MetricsMgr.OO)) {
			return MetricsFormat.toFormattedMetrics(this.objectOriented());
		} else if (this.extendMetrics(metrics) != null) {
			return MetricsFormat.toFormattedMetrics(this.extendMetrics(metrics).getMetrics());
		} else {
			return null;
		}
	}
}
