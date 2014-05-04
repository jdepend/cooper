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
				rtn = new Integer(a.afferentCoupling()).compareTo(new Integer(b.afferentCoupling()));
			} else if (this.metrics.equals(MetricsMgr.Ce)) {
				rtn = new Integer(a.efferentCoupling()).compareTo(new Integer(b.efferentCoupling()));
			} else if (this.metrics.equals(MetricsMgr.A)) {
				rtn = new Float(a.abstractness()).compareTo(new Float(b.abstractness()));
			} else if (this.metrics.equals(MetricsMgr.I)) {
				rtn = new Float(a.stability()).compareTo(new Float(b.stability()));
			} else if (this.metrics.equals(MetricsMgr.D)) {
				rtn = new Float(a.distance()).compareTo(new Float(b.distance()));
			} else if (this.metrics.equals(MetricsMgr.CaCoupling)) {
				rtn = new Float(a.caCoupling()).compareTo(new Float(b.caCoupling()));
			} else if (this.metrics.equals(MetricsMgr.CeCoupling)) {
				rtn = new Float(a.ceCoupling()).compareTo(new Float(b.ceCoupling()));
			} else if (this.metrics.equals(MetricsMgr.Coupling)) {
				rtn = new Float(a.coupling()).compareTo(new Float(b.coupling()));
			} else if (this.metrics.equals(MetricsMgr.Cohesion)) {
				rtn = new Float(a.cohesion()).compareTo(new Float(b.cohesion()));
			} else if (this.metrics.equals(MetricsMgr.Balance)) {
				rtn = new Float(a.balance()).compareTo(new Float(b.balance()));
			} else if (this.metrics.equals(MetricsMgr.Encapsulation)) {
				rtn = new Float(a.encapsulation()).compareTo(new Float(b.encapsulation()));
			} else if (this.metrics.equals(MetricsMgr.OO)) {
				rtn = new Float(a.objectOriented()).compareTo(new Float(b.objectOriented()));
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
