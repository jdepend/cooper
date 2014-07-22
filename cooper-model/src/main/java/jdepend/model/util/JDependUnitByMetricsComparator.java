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
				rtn = a.getName().compareTo(b.getName());
			} else if (this.metrics.equals(MetricsMgr.LC)) {
				rtn = new Integer(a.getLineCount()).compareTo(new Integer(b.getLineCount()));
			} else if (this.metrics.equals(MetricsMgr.CN)) {
				rtn = new Integer(a.getClassCount()).compareTo(new Integer(b.getClassCount()));
			} else if (this.metrics.equals(MetricsMgr.AC)) {
				rtn = new Integer(a.getAbstractClassCount()).compareTo(new Integer(b.getAbstractClassCount()));
			} else if (this.metrics.equals(MetricsMgr.CC)) {
				rtn = new Integer(a.getConcreteClassCount()).compareTo(new Integer(b.getConcreteClassCount()));
			} else if (this.metrics.equals(MetricsMgr.Ca)) {
				rtn = new Integer(a.getAfferentCoupling()).compareTo(new Integer(b.getAfferentCoupling()));
			} else if (this.metrics.equals(MetricsMgr.Ce)) {
				rtn = new Integer(a.getEfferentCoupling()).compareTo(new Integer(b.getEfferentCoupling()));
			} else if (this.metrics.equals(MetricsMgr.A)) {
				rtn = new Float(a.getAbstractness()).compareTo(new Float(b.getAbstractness()));
			} else if (this.metrics.equals(MetricsMgr.I)) {
				rtn = new Float(a.getStability()).compareTo(new Float(b.getStability()));
			} else if (this.metrics.equals(MetricsMgr.D)) {
				rtn = new Float(a.getDistance()).compareTo(new Float(b.getDistance()));
			} else if (this.metrics.equals(MetricsMgr.CaCoupling)) {
				rtn = new Float(a.caCoupling()).compareTo(new Float(b.caCoupling()));
			} else if (this.metrics.equals(MetricsMgr.CeCoupling)) {
				rtn = new Float(a.ceCoupling()).compareTo(new Float(b.ceCoupling()));
			} else if (this.metrics.equals(MetricsMgr.Coupling)) {
				rtn = new Float(a.getCoupling()).compareTo(new Float(b.getCoupling()));
			} else if (this.metrics.equals(MetricsMgr.Cohesion)) {
				rtn = new Float(a.getCohesion()).compareTo(new Float(b.getCohesion()));
			} else if (this.metrics.equals(MetricsMgr.Balance)) {
				rtn = new Float(a.getBalance()).compareTo(new Float(b.getBalance()));
			} else if (this.metrics.equals(MetricsMgr.Encapsulation)) {
				if (a.getEncapsulation() == null && b.getEncapsulation() == null) {
					rtn = 0;
				} else if (a.getEncapsulation() == null && b.getEncapsulation() != null) {
					rtn = 1;
				} else if (a.getEncapsulation() != null && b.getEncapsulation() == null) {
					rtn = -1;
				} else {
					rtn = a.getEncapsulation().compareTo(b.getEncapsulation());
				}
			} else if (this.metrics.equals(MetricsMgr.OO)) {
				rtn = new Float(a.getObjectOriented()).compareTo(new Float(b.getObjectOriented()));
			} else if (a.extendMetrics(metrics) != null) {
				rtn = new Float(a.extendMetrics(metrics).getMetrics()).compareTo(new Float(b.extendMetrics(metrics)
						.getMetrics()));
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

}
