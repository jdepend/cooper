package jdepend.util.refactor;

import jdepend.framework.util.MathUtil;
import jdepend.model.AreaComponent;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.MetricsMgr;
import jdepend.model.Relation;

public class CompareInfo {

	private String metrics;
	private Object value;
	private Object originality;
	private Integer result;
	private Boolean evaluate;

	public static final int NEW = 100;

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
		if (this.result == null) {
			this.calResult();
		}
		return result;
	}

	private void setResult(int result) {
		this.result = result;
		this.evaluate = evaluate(result, metrics);
	}

	public void calResult() {
		if (originality == null) {
			this.setResult(NEW);
			return;
		}
		if (value instanceof Float) {
			Float newValue = (Float) value;
			Float oldValue = (Float) originality;
			this.setResult(MathUtil.compare(newValue, oldValue));
		} else if (value instanceof String) {
			String newValue = (String) value;
			String oldValue = (String) originality;
			this.setResult(MathUtil.compare(newValue, oldValue));
		} else if (value instanceof Integer) {
			Integer newValue = (Integer) value;
			Integer oldValue = (Integer) originality;
			this.setResult(MathUtil.compare(newValue, oldValue));
		} else if (value instanceof AreaComponent) {
			Integer valueLayer = ((AreaComponent) value).getLayer();
			Integer originalityLayer = ((AreaComponent) originality).getLayer();
			this.setResult(MathUtil.compare(valueLayer, originalityLayer));
		} else {
			this.setResult(0);
		}
	}

	public Boolean getEvaluate() {
		return evaluate;
	}

	public boolean isDiff() {
		return !this.value.equals(this.originality);
	}

	private static Boolean evaluate(int result, String metrics) {
		if (result == NEW) {
			return null;
		} else if (metrics.equals(MetricsMgr.Name) || metrics.equals(MetricsMgr.LC) || metrics.equals(MetricsMgr.CN)
				|| metrics.equals(MetricsMgr.CC) || metrics.equals(MetricsMgr.AC) || metrics.equals(MetricsMgr.Ca)
				|| metrics.equals(MetricsMgr.Ce) || metrics.equals(JavaClass.State)
				|| metrics.equals(Relation.CurrentName) || metrics.equals(Relation.DependName)
				|| metrics.equals(Component.Area)) {
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
		} else if (metrics.equals(JavaClass.Stable)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(JavaClass.isPrivateElement)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(Relation.CurrentCohesion)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(Relation.DependCohesion)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(Relation.Intensity)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(Relation.Balance)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(Relation.AttentionLevel)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(Relation.AttentionType)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return null;
		}
	}
}
