package jdepend.model.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.util.MetricsFormat;
import jdepend.model.Component;
import jdepend.model.MetricsInfo;
import jdepend.model.MetricsMgr;
import jdepend.model.ObjectMeasured;

public final class AnalysisResultSummary extends ObjectMeasured implements Serializable {

	private static final long serialVersionUID = -5109171634600473818L;

	private static final String LogicAVE = "AVE";// 求平均
	private static final String LogicSUM = "SUM";// 求和

	public static final String Name = "小结";

	private float abstractness;

	private int afferentCoupling;

	private int efferentCoupling;

	private float balance;

	private float cohesion;

	private float coupling;

	private float distance;

	private float encapsulation;

	private int abstractClassCount;

	private int classCount;

	private int concreteClassCount;

	private int lineCount;

	private float instability;

	private float objectOriented;

	private float volatility;

	private int javaPackageCount;

	private int componentCount;

	private int relationCount;

	@Override
	public float getAbstractness() {
		return this.abstractness;
	}

	@Override
	public int getAfferentCoupling() {
		return this.afferentCoupling;
	}

	@Override
	public int getEfferentCoupling() {
		return this.efferentCoupling;
	}

	@Override
	public float getBalance() {
		return this.balance;
	}

	@Override
	public float getCohesion() {
		return this.cohesion;
	}

	@Override
	public boolean getContainsCycle() {
		return false;
	}

	@Override
	public float getCoupling() {
		return this.coupling;
	}

	@Override
	public float getDistance() {
		return this.distance;
	}

	@Override
	public float getEncapsulation() {
		return this.encapsulation;
	}

	@Override
	public MetricsInfo extendMetrics(String metrics) {
		return null;
	}

	@Override
	public int getAbstractClassCount() {
		return this.abstractClassCount;
	}

	@Override
	public int getClassCount() {
		return this.classCount;
	}

	@Override
	public int getConcreteClassCount() {
		return this.concreteClassCount;
	}

	@Override
	public int getLineCount() {
		return this.lineCount;
	}

	@Override
	public String getTitle() {
		return Name;
	}

	@Override
	public float getStability() {
		return this.instability;
	}

	@Override
	public float getObjectOriented() {
		return this.objectOriented;
	}

	@Override
	public float getVolatility() {
		return this.volatility;
	}

	@Override
	public String getName() {
		return Name;
	}

	public void setAbstractness(float abstractness) {
		this.abstractness = abstractness;
	}

	public void setAfferentCoupling(int afferentCoupling) {
		this.afferentCoupling = afferentCoupling;
	}

	public void setEfferentCoupling(int efferentCoupling) {
		this.efferentCoupling = efferentCoupling;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public void setCohesion(float cohesion) {
		this.cohesion = cohesion;
	}

	public void setCoupling(float coupling) {
		this.coupling = coupling;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public void setEncapsulation(float encapsulation) {
		this.encapsulation = encapsulation;
	}

	public float getInstability() {
		return instability;
	}

	public void setInstability(float instability) {
		this.instability = instability;
	}

	public void setObjectOriented(float objectOriented) {
		this.objectOriented = objectOriented;
	}

	public void setVolatility(float volatility) {
		this.volatility = volatility;
	}

	public void setAbstractClassCount(int abstractClassCount) {
		this.abstractClassCount = abstractClassCount;
	}

	public void setClassCount(int classCount) {
		this.classCount = classCount;
	}

	public void setConcreteClassCount(int concreteClassCount) {
		this.concreteClassCount = concreteClassCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public int getJavaPackageCount() {
		return javaPackageCount;
	}

	public void setJavaPackageCount(int javaPackageCount) {
		this.javaPackageCount = javaPackageCount;
	}

	public int getComponentCount() {
		return componentCount;
	}

	public void setComponentCount(int componentCount) {
		this.componentCount = componentCount;
	}

	public int getRelationCount() {
		return relationCount;
	}

	public void setRelationCount(int relationCount) {
		this.relationCount = relationCount;
	}

	public static AnalysisResultSummary calSummary(AnalysisResult result) {

		String[] metrics = new String[] { MetricsMgr.LC, MetricsMgr.CN, MetricsMgr.CC, MetricsMgr.AC, MetricsMgr.Ca,
				MetricsMgr.Ce, MetricsMgr.A, MetricsMgr.V, MetricsMgr.I, MetricsMgr.D, MetricsMgr.Coupling,
				MetricsMgr.Cohesion, MetricsMgr.Balance, MetricsMgr.OO, MetricsMgr.Encapsulation };

		String[] logic = new String[metrics.length];

		logic[0] = LogicSUM;
		logic[1] = LogicSUM;
		logic[2] = LogicSUM;
		logic[3] = LogicSUM;
		logic[4] = LogicSUM;
		logic[5] = LogicSUM;
		logic[6] = LogicAVE;
		logic[7] = LogicAVE;
		logic[8] = LogicAVE;
		logic[9] = LogicAVE;
		logic[10] = LogicAVE;
		logic[11] = LogicAVE;
		logic[12] = LogicAVE;
		logic[13] = LogicAVE;
		logic[14] = LogicAVE;

		List<Object[]> data = new ArrayList<Object[]>();
		Object[] rowData;
		for (Component unit : result.getComponents()) {
			if (unit.isInner()) {// 只计算内部分析单元
				rowData = new Object[metrics.length];
				for (int i = 0; i < metrics.length; i++) {
					rowData[i] = unit.getValue(metrics[i]);
				}
				data.add(rowData);
			}
		}

		return getSummry(data, logic, result);
	}

	private static AnalysisResultSummary getSummry(List<Object[]> objs, String[] logic, AnalysisResult result) {

		AnalysisResultSummary resultSummry = new AnalysisResultSummary();

		if (objs.size() == 0)
			return resultSummry;

		Object[] summry = new Object[objs.get(0).length];
		for (int i = 0; i < logic.length; i++) {
			if (objs.get(0)[i] instanceof Integer) {
				summry[i] = 0;
			} else if (objs.get(0)[i] instanceof Float) {
				summry[i] = 0F;
			}
		}

		for (int row = 0; row < objs.size(); row++) {
			for (int col = 0; col < logic.length; col++) {
				if (logic[col] != null && objs.get(row)[col] != null) {
					if (objs.get(row)[col] instanceof Float) {
						summry[col] = (Float) summry[col] + (Float) objs.get(row)[col];
					} else if (objs.get(row)[col] instanceof Integer) {
						summry[col] = (Integer) summry[col] + (Integer) objs.get(row)[col];
					}
				}

			}
		}
		for (int col = 0; col < logic.length; col++) {
			if (logic[col] != null) {
				if (logic[col].equals(LogicAVE)) {
					if (summry[col] instanceof Integer) {
						summry[col] = (Integer) ((Integer) summry[col] / objs.size());
					} else {
						summry[col] = ((Float) summry[col]) / objs.size();
					}

				} else if (logic[col].equals(LogicSUM)) {
					if (summry[col] instanceof Float) {
						summry[col] = (Float) summry[col];
					}
				}
			}
		}

		resultSummry.setJavaPackageCount(result.getJavaPackages().size());
		resultSummry.setLineCount((Integer) summry[0]);
		resultSummry.setClassCount((Integer) summry[1]);
		resultSummry.setConcreteClassCount((Integer) summry[2]);
		resultSummry.setAbstractClassCount((Integer) summry[3]);
		resultSummry.setAfferentCoupling((Integer) summry[4]);
		resultSummry.setEfferentCoupling((Integer) summry[5]);
		resultSummry.setAbstractness(MetricsFormat.toFormattedMetrics((Float) summry[6]));
		resultSummry.setVolatility(MetricsFormat.toFormattedMetrics((Float) summry[7]));
		resultSummry.setInstability(MetricsFormat.toFormattedMetrics((Float) summry[8]));
		resultSummry.setDistance(MetricsFormat.toFormattedMetrics((Float) summry[9]));
		resultSummry.setCoupling(MetricsFormat.toFormattedMetrics((Float) summry[10]));
		resultSummry.setCohesion(MetricsFormat.toFormattedMetrics((Float) summry[11]));
		resultSummry.setBalance(MetricsFormat.toFormattedMetrics((Float) summry[12]));
		resultSummry.setObjectOriented(MetricsFormat.toFormattedMetrics((Float) summry[13]));
		resultSummry.setComponentCount(objs.size());
		resultSummry.setEncapsulation(MetricsFormat.toFormattedMetrics((Float) summry[14]));

		resultSummry.setRelationCount(result.getRelations().size());

		return resultSummry;
	}

	@Override
	public String toString() {

		StringBuilder info = new StringBuilder();

		info.append("共分析" + this.getComponentCount() + "个分析单元，得到" + this.getJavaPackageCount() + "个packages，"
				+ this.getClassCount() + "个javaclass，代码行数为" + this.getLineCount() + "行。\n");
		info.append("其中具有抽象类计数资格的类" + this.getAbstractClassCount() + "个\n");
		info.append("   具体类" + this.getConcreteClassCount() + "个\n");
		info.append("   关系" + this.getAfferentCoupling() + "个。\n");
		info.append("   抽象程度" + MetricsFormat.toFormattedMetrics(this.getAbstractness())
				+ "	[具有抽象类计数资格的类数量/类总数，值越大越抽象]\n");
		info.append("   易变性" + MetricsFormat.toFormattedMetrics(this.getVolatility()) + "	[稳定的类数量/类总数，值越大越不易变]\n");
		info.append("   不稳定性" + MetricsFormat.toFormattedMetrics(this.getInstability())
				+ "	[传出数量/(传出数量 + 传入数量)，值越小越稳定]\n");
		info.append("   合理性" + MetricsFormat.toFormattedMetrics(this.getDistance())
				+ " [abs(抽象程度 + 易变性 + 不稳定性 - 1)，值越接近零越理想]\n");
		info.append("   耦合值" + MetricsFormat.toFormattedMetrics(this.getCoupling())
				+ " [sum(依赖的JavaClass * Relation强度)，值越小越好]\n");
		info.append("   内聚值" + MetricsFormat.toFormattedMetrics(this.getCohesion())
				+ " [sum(内部的JavaClass * Relation强度)，值越大越好]\n");
		info.append("   内聚性指数" + MetricsFormat.toFormattedMetrics(this.getBalance())
				+ " [内聚值/(内聚值+分组耦合最大顺序差值），值越大越好]\n");
		info.append("   面向对象指数" + MetricsFormat.toFormattedMetrics(this.getObjectOriented()) + " [私有属性 / 公开方法，值越大越好]\n");
		info.append("   封装性" + MetricsFormat.toFormattedMetrics(this.getEncapsulation()) + " [私有类比例，值越大越好]\n");

		return info.toString();
	}
}
