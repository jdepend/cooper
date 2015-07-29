package jdepend.model.util;

import java.util.Comparator;

import jdepend.model.JDependUnit;
import jdepend.model.MetricsMgr;

/**
 * 按指标比较包的比较器
 * 
 * @author <b>Abner</b>
 * 
 */
public class JDependUnitByMetricsComparator implements Comparator {

	private String metrics = null;

	private boolean asc = true;

	public JDependUnitByMetricsComparator() {
	}

	public JDependUnitByMetricsComparator(String metrics) {
		this.metrics = metrics;
	}

	public JDependUnitByMetricsComparator(String metrics, boolean asc) {
		this(metrics);
		this.asc = asc;
	}

	@Override
	public int compare(Object p1, Object p2) {

		JDependUnit a = (JDependUnit) p1;
		JDependUnit b = (JDependUnit) p2;

		int rtn = 0;

		if (this.metrics != null) {
			if (this.metrics.equals(MetricsMgr.Name)) {
				rtn = compare(a.getName(), b.getName());
			} else if (this.metrics.equals(MetricsMgr.LC)) {
				rtn = compare(a.getLineCount(), b.getLineCount());
			} else if (this.metrics.equals(MetricsMgr.CN)) {
				rtn = compare(a.getClassCount(), b.getClassCount());
			} else if (this.metrics.equals(MetricsMgr.AC)) {
				rtn = compare(a.getAbstractClassCount(), b.getAbstractClassCount());
			} else if (this.metrics.equals(MetricsMgr.CC)) {
				rtn = compare(a.getConcreteClassCount(), b.getConcreteClassCount());
			} else if (this.metrics.equals(MetricsMgr.Ca)) {
				rtn = compare(a.getAfferentCoupling(), b.getAfferentCoupling());
			} else if (this.metrics.equals(MetricsMgr.Ce)) {
				rtn = compare(a.getEfferentCoupling(), b.getEfferentCoupling());
			} else if (this.metrics.equals(MetricsMgr.A)) {
				rtn = compare(a.getAbstractness(), b.getAbstractness());
			} else if (this.metrics.equals(MetricsMgr.I)) {
				rtn = compare(a.getStability(), b.getStability());
			} else if (this.metrics.equals(MetricsMgr.D)) {
				rtn = compare(a.getDistance(), b.getDistance());
			} else if (this.metrics.equals(MetricsMgr.CaCoupling)) {
				rtn = compare(a.caCoupling(), b.caCoupling());
			} else if (this.metrics.equals(MetricsMgr.CeCoupling)) {
				rtn = compare(a.ceCoupling(), b.ceCoupling());
			} else if (this.metrics.equals(MetricsMgr.Coupling)) {
				rtn = compare(a.getCoupling(), b.getCoupling());
			} else if (this.metrics.equals(MetricsMgr.Cohesion)) {
				rtn = compare(a.getCohesion(), b.getCohesion());
			} else if (this.metrics.equals(MetricsMgr.Balance)) {
				rtn = compare(a.getBalance(), b.getBalance());
			} else if (this.metrics.equals(MetricsMgr.Encapsulation)) {
				rtn = compare(a.getEncapsulation(), b.getEncapsulation());
			} else if (a.extendMetrics(metrics) != null) {
				rtn = compare(a.extendMetrics(metrics).getMetrics(), b.extendMetrics(metrics).getMetrics());
			} else {
				rtn = a.getName().compareTo(b.getName());
			}
		} else {
			rtn = a.getName().compareTo(b.getName());
		}

		if (this.asc) {
			return rtn;
		} else {
			return -rtn;
		}

	}

	private int compare(Float e1, Float e2) {
		if (e1 == null && e2 == null) {
			return 0;
		} else if (e1 == null && e2 != null) {
			return 1;
		} else if (e1 != null && e2 == null) {
			return -1;
		} else {
			return e1.compareTo(e2);
		}
	}

	private int compare(Integer e1, Integer e2) {
		if (e1 == null && e2 == null) {
			return 0;
		} else if (e1 == null && e2 != null) {
			return 1;
		} else if (e1 != null && e2 == null) {
			return -1;
		} else {
			return e1.compareTo(e2);
		}
	}

	private int compare(String e1, String e2) {
		if (e1 == null && e2 == null) {
			return 0;
		} else if (e1 == null && e2 != null) {
			return 1;
		} else if (e1 != null && e2 == null) {
			return -1;
		} else {
			return e1.compareTo(e2);
		}
	}

}
