package jdepend.model;

import java.util.HashMap;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;

/**
 * 动态指标管理器
 * 
 * @author <b>Abner</b>
 * 
 */
public final class MetricsMgr {

	public static final String Name = "Name";
	public static final String Title = "Title";
	public static final String LC = "LC";
	public static final String CN = "CN";
	public static final String CC = "CC";
	public static final String AC = "AC";
	public static final String Ca = "Ca";
	public static final String Ce = "Ce";
	public static final String A = "A";
	public static final String I = "I";
	public static final String D = "D";
	public static final String V = "V";
	public static final String CaCoupling = "CaCoupling";
	public static final String CeCoupling = "CeCoupling";
	public static final String Coupling = "Coupling";
	public static final String Cohesion = "Cohesion";
	public static final String Balance = "Balance";
	public static final String OO = "OO";
	public static final String Cycle = "Cycle";
	public static final String Encapsulation = "Encapsulation";

	public static final String Cyclic = "存在";

	public static final String HaveState = "有";
	public static final String Stability = "稳定";
	public static final String Private = "私有";
	public static final String isExt = "是";

	public static final String NoValue = "";

	private static MetricsMgr mgr = new MetricsMgr();

	protected Map<String, Metrics> metricses = new HashMap<String, Metrics>();

	private MetricsMgr() {
	}

	public static MetricsMgr getInstance() {
		return mgr;
	}

	public void addMetrics(String name, Metrics metrics) throws JDependException {

		// if(this.metricses.containsKey(name))
		// throw new JDependException("名为[" + name + "]已经注册！");

		this.metricses.put(name, metrics);
	}

	public Metrics getMetrics(String name) {
		return metricses.get(name);
	}

	public Map<String, Metrics> getMetricses() {
		return metricses;
	}

}
