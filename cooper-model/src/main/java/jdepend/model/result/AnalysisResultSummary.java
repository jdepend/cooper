package jdepend.model.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.util.MetricsFormat;
import jdepend.model.Component;
import jdepend.model.MetricsInfo;
import jdepend.model.MetricsMgr;
import jdepend.model.ObjectMeasured;
import jdepend.model.profile.model.AnalysisResultProfile;

public class AnalysisResultSummary extends ObjectMeasured implements Serializable {

	private static final long serialVersionUID = -5109171634600473818L;

	public static final String Name = "小结";

	private Float abstractness;

	private int afferentCoupling;

	private int efferentCoupling;

	private Float balance;

	private float cohesion;

	private float coupling;

	private Float distance;

	private Float encapsulation;

	private int abstractClassCount;

	private int classCount;

	private int concreteClassCount;

	private int lineCount;

	private Float stability;

	private Float volatility;

	private int javaPackageCount;

	private int componentCount;

	private int relationCount;

	public AnalysisResultSummary() {

	}

	public AnalysisResultSummary(AnalysisResultSummary summary) {

		this.abstractness = summary.abstractness;
		this.afferentCoupling = summary.afferentCoupling;
		this.efferentCoupling = summary.efferentCoupling;
		this.balance = summary.balance;
		this.cohesion = summary.cohesion;
		this.coupling = summary.coupling;
		this.distance = summary.distance;
		this.encapsulation = summary.encapsulation;
		this.abstractClassCount = summary.abstractClassCount;
		this.classCount = summary.classCount;
		this.concreteClassCount = summary.concreteClassCount;
		this.lineCount = summary.lineCount;
		this.stability = summary.stability;
		this.volatility = summary.volatility;
		this.javaPackageCount = summary.javaPackageCount;
		this.componentCount = summary.componentCount;
		this.relationCount = summary.relationCount;
	}

	@Override
	public Float getAbstractness() {
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
	public Float getBalance() {
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
	public Float getDistance() {
		return this.distance;
	}

	@Override
	public Float getEncapsulation() {
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
	public Float getStability() {
		return this.stability;
	}

	@Override
	public Float getVolatility() {
		return this.volatility;
	}

	@Override
	public String getName() {
		return Name;
	}

	public void setAbstractness(Float abstractness) {
		this.abstractness = abstractness;
	}

	public void setAfferentCoupling(int afferentCoupling) {
		this.afferentCoupling = afferentCoupling;
	}

	public void setEfferentCoupling(int efferentCoupling) {
		this.efferentCoupling = efferentCoupling;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public void setCohesion(float cohesion) {
		this.cohesion = cohesion;
	}

	public void setCoupling(float coupling) {
		this.coupling = coupling;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public void setEncapsulation(Float encapsulation) {
		this.encapsulation = encapsulation;
	}

	public void setStability(Float stability) {
		this.stability = stability;
	}

	public void setVolatility(Float volatility) {
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

	public AnalysisResultSummary(AnalysisResult result) {

		MetricsSummaryInfo[] metricsSummaryInfos = getMetricsSummaryInfo(result);
		// 计算内部分析单元
		List<Object[]> objs = new ArrayList<Object[]>();
		Object[] rowData;
		for (Component unit : result.getComponents()) {
			if (unit.isInner()) {
				rowData = new Object[metricsSummaryInfos.length];
				for (int i = 0; i < metricsSummaryInfos.length; i++) {
					rowData[i] = unit.getValue(metricsSummaryInfos[i].name);
				}
				objs.add(rowData);
			}
		}

		if (objs.size() > 0) {
			// 初始化summry
			Object[] summry = new Object[metricsSummaryInfos.length];
			for (int i = 0; i < metricsSummaryInfos.length; i++) {
				if (metricsSummaryInfos[i].type.equals(MetricsSummaryInfo.TypeInteger)) {
					summry[i] = 0;
				} else if (metricsSummaryInfos[i].type.equals(MetricsSummaryInfo.TypeFloat)) {
					summry[i] = 0F;
				}
			}

			// 初始化参与计算的组件个数
			Integer[] calComponents = new Integer[metricsSummaryInfos.length];
			for (int col = 0; col < metricsSummaryInfos.length; col++) {
				calComponents[col] = 0;
			}

			// 初始化参与计算的组件代码行数总和
			Integer[] componentSizes = new Integer[metricsSummaryInfos.length];
			for (int col = 0; col < metricsSummaryInfos.length; col++) {
				componentSizes[col] = 0;
			}

			// 计算参与计算的组件个数和代码行数总和
			for (int row = 0; row < objs.size(); row++) {
				for (int col = 0; col < metricsSummaryInfos.length; col++) {
					if (objs.get(row)[col] != null) {
						calComponents[col]++;
						componentSizes[col] = componentSizes[col] + (Integer) objs.get(row)[0];
					}
				}
			}

			//当指定指标的代码行总数为0时，采用LogicAVE计算汇总值
			for (int col = 0; col < metricsSummaryInfos.length; col++) {
				if (componentSizes[col] == 0) {
					metricsSummaryInfos[col].logic = MetricsSummaryInfo.LogicAVE;
				}
			}

			// 计算汇总数据第一步（求和）
			for (int row = 0; row < objs.size(); row++) {
				for (int col = 0; col < metricsSummaryInfos.length; col++) {
					if (objs.get(row)[col] != null) {
						if (metricsSummaryInfos[col].logic.equals(MetricsSummaryInfo.LogicAVE_WEIGHT)) {
							if (metricsSummaryInfos[col].type.equals(MetricsSummaryInfo.TypeFloat)) {
								summry[col] = (Float) summry[col] + (Float) objs.get(row)[col]
										* (Integer) objs.get(row)[0];
							} else if (metricsSummaryInfos[col].type.equals(MetricsSummaryInfo.TypeInteger)) {
								summry[col] = (Integer) summry[col] + (Integer) objs.get(row)[col]
										* (Integer) objs.get(row)[0];
							}
						} else {
							if (metricsSummaryInfos[col].type.equals(MetricsSummaryInfo.TypeFloat)) {
								summry[col] = (Float) summry[col] + (Float) objs.get(row)[col];
							} else if (metricsSummaryInfos[col].type.equals(MetricsSummaryInfo.TypeInteger)) {
								summry[col] = (Integer) summry[col] + (Integer) objs.get(row)[col];
							}
						}
					}
				}
			}

			// 计算汇总数据第二步（求均）
			for (int col = 0; col < metricsSummaryInfos.length; col++) {
				if (calComponents[col] != 0) {
					if (metricsSummaryInfos[col].logic.equals(MetricsSummaryInfo.LogicAVE)) {
						if (metricsSummaryInfos[col].type.equals(MetricsSummaryInfo.TypeInteger)) {
							summry[col] = (Integer) ((Integer) summry[col] / calComponents[col]);
						} else {
							summry[col] = ((Float) summry[col]) / calComponents[col];
						}
					} else if (metricsSummaryInfos[col].logic.equals(MetricsSummaryInfo.LogicAVE_WEIGHT)) {
						if (componentSizes[col] != 0) {
							if (metricsSummaryInfos[col].type.equals(MetricsSummaryInfo.TypeInteger)) {
								summry[col] = (Integer) ((Integer) summry[col] / (Integer) componentSizes[col]);
							} else {
								summry[col] = ((Float) summry[col]) / new Float((Integer) componentSizes[col]);
							}
						} else {
							summry[col] = null;
						}
					}
				} else {
					summry[col] = null;
				}
			}

			this.setJavaPackageCount(result.getJavaPackages().size());
			this.setLineCount((Integer) summry[0]);
			this.setClassCount((Integer) summry[1]);
			this.setConcreteClassCount((Integer) summry[2]);
			this.setAbstractClassCount((Integer) summry[3]);
			this.setAfferentCoupling((Integer) summry[4]);
			this.setEfferentCoupling((Integer) summry[5]);
			this.setAbstractness(MetricsFormat.toFormattedMetrics((Float) summry[6]));
			this.setVolatility(MetricsFormat.toFormattedMetrics((Float) summry[7]));
			this.setStability(MetricsFormat.toFormattedMetrics((Float) summry[8]));
			this.setDistance(MetricsFormat.toFormattedMetrics((Float) summry[9]));
			this.setCoupling(MetricsFormat.toFormattedMetrics((Float) summry[10]) / 2);
			this.setCohesion(MetricsFormat.toFormattedMetrics((Float) summry[11]));
			this.setBalance(MetricsFormat.toFormattedMetrics((Float) summry[12]));
			this.setEncapsulation(MetricsFormat.toFormattedMetrics((Float) summry[13]));
			this.setComponentCount(objs.size());

			this.setRelationCount(result.getRelations().size());
		}
	}

	private static MetricsSummaryInfo[] getMetricsSummaryInfo(AnalysisResult result) {

		// 得到summry部分指标（A、V、I、D、Balance、Encapsulation）计算规则
		AnalysisResultProfile analysisResultProfile = result.getRunningContext().getProfileFacade()
				.getAnalysisResultProfile();

		String logic = null;
		if (analysisResultProfile.isComponentWeight()) {
			logic = MetricsSummaryInfo.LogicAVE_WEIGHT;
		} else {
			logic = MetricsSummaryInfo.LogicAVE;
		}

		MetricsSummaryInfo[] metricsSummaryInfos;

		metricsSummaryInfos = new MetricsSummaryInfo[14];
		metricsSummaryInfos[0] = new MetricsSummaryInfo(MetricsMgr.LC, MetricsSummaryInfo.TypeInteger,
				MetricsSummaryInfo.LogicSUM);
		metricsSummaryInfos[1] = new MetricsSummaryInfo(MetricsMgr.CN, MetricsSummaryInfo.TypeInteger,
				MetricsSummaryInfo.LogicSUM);
		metricsSummaryInfos[2] = new MetricsSummaryInfo(MetricsMgr.CC, MetricsSummaryInfo.TypeInteger,
				MetricsSummaryInfo.LogicSUM);
		metricsSummaryInfos[3] = new MetricsSummaryInfo(MetricsMgr.AC, MetricsSummaryInfo.TypeInteger,
				MetricsSummaryInfo.LogicSUM);
		metricsSummaryInfos[4] = new MetricsSummaryInfo(MetricsMgr.Ca, MetricsSummaryInfo.TypeInteger,
				MetricsSummaryInfo.LogicSUM);
		metricsSummaryInfos[5] = new MetricsSummaryInfo(MetricsMgr.Ce, MetricsSummaryInfo.TypeInteger,
				MetricsSummaryInfo.LogicSUM);
		metricsSummaryInfos[6] = new MetricsSummaryInfo(MetricsMgr.A, MetricsSummaryInfo.TypeFloat, logic);
		metricsSummaryInfos[7] = new MetricsSummaryInfo(MetricsMgr.V, MetricsSummaryInfo.TypeFloat, logic);
		metricsSummaryInfos[8] = new MetricsSummaryInfo(MetricsMgr.I, MetricsSummaryInfo.TypeFloat, logic);
		metricsSummaryInfos[9] = new MetricsSummaryInfo(MetricsMgr.D, MetricsSummaryInfo.TypeFloat, logic);
		metricsSummaryInfos[10] = new MetricsSummaryInfo(MetricsMgr.Coupling, MetricsSummaryInfo.TypeFloat,
				MetricsSummaryInfo.LogicSUM);
		metricsSummaryInfos[11] = new MetricsSummaryInfo(MetricsMgr.Cohesion, MetricsSummaryInfo.TypeFloat,
				MetricsSummaryInfo.LogicSUM);
		metricsSummaryInfos[12] = new MetricsSummaryInfo(MetricsMgr.Balance, MetricsSummaryInfo.TypeFloat, logic);
		metricsSummaryInfos[13] = new MetricsSummaryInfo(MetricsMgr.Encapsulation, MetricsSummaryInfo.TypeFloat, logic);

		return metricsSummaryInfos;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + abstractClassCount;
		result = prime * result + ((abstractness == null) ? 0 : Float.floatToIntBits(abstractness));
		result = prime * result + afferentCoupling;
		result = prime * result + ((balance == null) ? 0 : Float.floatToIntBits(balance));
		result = prime * result + classCount;
		result = prime * result + Float.floatToIntBits(cohesion);
		result = prime * result + componentCount;
		result = prime * result + concreteClassCount;
		result = prime * result + Float.floatToIntBits(coupling);
		result = prime * result + ((distance == null) ? 0 : Float.floatToIntBits(distance));
		result = prime * result + efferentCoupling;
		result = prime * result + ((encapsulation == null) ? 0 : Float.floatToIntBits(encapsulation));
		result = prime * result + ((stability == null) ? 0 : Float.floatToIntBits(stability));
		result = prime * result + javaPackageCount;
		result = prime * result + lineCount;
		result = prime * result + relationCount;
		result = prime * result + ((volatility == null) ? 0 : Float.floatToIntBits(volatility));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnalysisResultSummary other = (AnalysisResultSummary) obj;
		if (abstractClassCount != other.abstractClassCount)
			return false;
		if (abstractness == null && other.abstractness != null)
			return false;
		if (abstractness != null && other.abstractness == null)
			return false;
		if (abstractness != null && other.abstractness != null)
			if (Float.floatToIntBits(abstractness) != Float.floatToIntBits(other.abstractness))
				return false;
		if (afferentCoupling != other.afferentCoupling)
			return false;
		if (balance == null && other.balance != null)
			return false;
		if (balance != null && other.balance == null)
			return false;
		if (balance != null && other.balance != null)
			if (Float.floatToIntBits(balance) != Float.floatToIntBits(other.balance))
				return false;
		if (classCount != other.classCount)
			return false;
		if (Float.floatToIntBits(cohesion) != Float.floatToIntBits(other.cohesion))
			return false;
		if (componentCount != other.componentCount)
			return false;
		if (concreteClassCount != other.concreteClassCount)
			return false;
		if (Float.floatToIntBits(coupling) != Float.floatToIntBits(other.coupling))
			return false;
		if (Float.floatToIntBits(distance) != Float.floatToIntBits(other.distance))
			return false;
		if (efferentCoupling != other.efferentCoupling)
			return false;
		if (encapsulation == null && other.encapsulation != null)
			return false;
		if (encapsulation != null && other.encapsulation == null)
			return false;
		if (encapsulation != null && other.encapsulation != null)
			if (Float.floatToIntBits(encapsulation) != Float.floatToIntBits(other.encapsulation))
				return false;
		if (stability == null && other.stability != null)
			return false;
		if (stability != null && other.stability == null)
			return false;
		if (stability != null && other.stability != null)
			if (Float.floatToIntBits(stability) != Float.floatToIntBits(other.stability))
				return false;
		if (javaPackageCount != other.javaPackageCount)
			return false;
		if (lineCount != other.lineCount)
			return false;
		if (relationCount != other.relationCount)
			return false;
		if (volatility == null && other.volatility != null)
			return false;
		if (volatility != null && other.volatility == null)
			return false;
		if (volatility != null && other.volatility != null)
			if (Float.floatToIntBits(volatility) != Float.floatToIntBits(other.volatility))
				return false;

		return true;
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
		info.append("   不稳定性" + MetricsFormat.toFormattedMetrics(this.getStability())
				+ "	[传出数量/(传出数量 + 传入数量)，值越小越稳定]\n");
		info.append("   合理性" + MetricsFormat.toFormattedMetrics(this.getDistance())
				+ " [1 - abs(抽象程度  + 不稳定性 - 1)，值越大越理想]\n");
		info.append("   耦合值" + MetricsFormat.toFormattedMetrics(this.getCoupling())
				+ " [sum(依赖的JavaClass * Relation强度)，值越小越好]\n");
		info.append("   内聚值" + MetricsFormat.toFormattedMetrics(this.getCohesion())
				+ " [sum(内部的JavaClass * Relation强度)，值越大越好]\n");
		info.append("   内聚性指数" + MetricsFormat.toFormattedMetrics(this.getBalance())
				+ " [内聚值/(内聚值+分组耦合最大顺序差值），值越大越好]\n");
		info.append("   封装性" + MetricsFormat.toFormattedMetrics(this.getEncapsulation()) + " [私有类比例，值越大越好]\n");

		return info.toString();
	}

	static class MetricsSummaryInfo {

		private static final String LogicAVE = "AVE";// 求平均
		private static final String LogicSUM = "SUM";// 求和

		private static final String LogicAVE_WEIGHT = "AVE_WEIGHT";// 加权求平均

		private static final String TypeInteger = "Integer";
		private static final String TypeFloat = "Float";

		String name;
		String type;
		String logic;

		public MetricsSummaryInfo(String name, String type, String logic) {
			super();
			this.name = name;
			this.type = type;
			this.logic = logic;
		}
	}
}
