package jdepend.util.refactor;

import jdepend.model.MetricsMgr;

public class CompareInfo {

	private String metrics;
	private Object value;
	private Object originality;
	private int result;
	private Boolean evaluate;

	public String getMetrics() {
		return metrics;
	}

	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getOriginality() {
		return originality;
	}

	public void setOriginality(Object originality) {
		this.originality = originality;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
		this.evaluate = evaluate(result, metrics);
	}

	public Boolean getEvaluate() {
		return evaluate;
	}

	public boolean isDiff() {
		return !this.value.equals(this.originality);
	}

	private static Boolean evaluate(int result, String metrics) {
		if (metrics.equals(MetricsMgr.Name) || metrics.equals(MetricsMgr.LC) || metrics.equals(MetricsMgr.CN)
				|| metrics.equals(MetricsMgr.CC) || metrics.equals(MetricsMgr.AC) || metrics.equals(MetricsMgr.Ca)
				|| metrics.equals(MetricsMgr.Ce) || metrics.equals(MetricsMgr.State)) {
			return null;
		} else if (metrics.equals(MetricsMgr.A)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(MetricsMgr.I)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(MetricsMgr.D)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(MetricsMgr.A)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(MetricsMgr.V)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(MetricsMgr.Coupling)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(MetricsMgr.Cohesion)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(MetricsMgr.Balance)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(MetricsMgr.OO)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(MetricsMgr.Encapsulation)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(MetricsMgr.Cycle)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(MetricsMgr.Stable)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(MetricsMgr.isPrivateElement)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return null;
		}
	}
}
