package jdepend.report.util;

import java.util.HashMap;
import java.util.Map;

import jdepend.framework.util.BundleUtil;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.MetricsMgr;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;

public class ReportConstant {

	public static final String Name = BundleUtil.getString(BundleUtil.Metrics_Name);
	public static final String Title = BundleUtil.getString(BundleUtil.Metrics_Title);
	public static final String DependType = BundleUtil.getString(BundleUtil.Relation_ClassDependType);
	public static final String LC = BundleUtil.getString(BundleUtil.Metrics_LC);
	public static final String CN = BundleUtil.getString(BundleUtil.Metrics_CN);
	public static final String CC = BundleUtil.getString(BundleUtil.Metrics_CC);
	public static final String AC = BundleUtil.getString(BundleUtil.Metrics_AC);
	public static final String Ca = BundleUtil.getString(BundleUtil.Metrics_Ca);
	public static final String Ce = BundleUtil.getString(BundleUtil.Metrics_Ce);
	public static final String A = BundleUtil.getString(BundleUtil.Metrics_A);
	public static final String I = BundleUtil.getString(BundleUtil.Metrics_I);
	public static final String D = BundleUtil.getString(BundleUtil.Metrics_D);
	public static final String V = BundleUtil.getString(BundleUtil.Metrics_V);
	public static final String Coupling = BundleUtil.getString(BundleUtil.Metrics_Coupling);
	public static final String Cohesion = BundleUtil.getString(BundleUtil.Metrics_Cohesion);
	public static final String Balance = BundleUtil.getString(BundleUtil.Metrics_Balance);
	public static final String OO = BundleUtil.getString(BundleUtil.Metrics_OO);
	public static final String Encapsulation = BundleUtil.getString(BundleUtil.Metrics_Encapsulation);
	public static final String Cycle = BundleUtil.getString(BundleUtil.Metrics_Cycle);

	public static final String Component_Area = BundleUtil.getString(BundleUtil.Metrics_Area);

	public static final String JavaClass_State = BundleUtil.getString(BundleUtil.Metrics_State);
	public static final String JavaClass_Stable = BundleUtil.getString(BundleUtil.Metrics_Stable);
	public static final String JavaClass_isPrivateElement = BundleUtil.getString(BundleUtil.Metrics_isPrivateElement);
	public static final String JavaClass_isExt = BundleUtil.getString(BundleUtil.Metrics_isExt);

	public static final String Relation_CurrentName = BundleUtil.getString(BundleUtil.Relation_CurrentElement);
	public static final String Relation_DependName = BundleUtil.getString(BundleUtil.Relation_DependElement);
	public static final String Relation_Intensity = BundleUtil.getString(BundleUtil.Relation_Intensity);
	public static final String Relation_CurrentCohesion = BundleUtil.getString(BundleUtil.Relation_CurrentCohesion);
	public static final String Relation_DependCohesion = BundleUtil.getString(BundleUtil.Relation_DependCohesion);
	public static final String Relation_Balance = BundleUtil.getString(BundleUtil.Relation_Balance);
	public static final String Relation_AttentionType = BundleUtil.getString(BundleUtil.Relation_AttentionType);
	public static final String Relation_AttentionLevel = BundleUtil.getString(BundleUtil.Relation_AttentionLevel);

	public static final String Result_Metrics_D = BundleUtil.getString(BundleUtil.Metrics_D);
	public static final String Result_Metrics_Balance = BundleUtil.getString(BundleUtil.Metrics_Balance);
	public static final String Result_Metrics_RelationRationality = BundleUtil
			.getString(BundleUtil.Metrics_RelationRationality);
	public static final String Result_Metrics_OO = BundleUtil.getString(BundleUtil.Metrics_OO);
	public static final String Result_Metrics_Encapsulation = BundleUtil.getString(BundleUtil.Metrics_Encapsulation);

	public static final String DependInterface = BundleUtil.getString(BundleUtil.TableHead_DependInterface);

	public static final String CurrentJC = BundleUtil.getString(BundleUtil.TableHead_CurrentJC);
	public static final String DependJC = BundleUtil.getString(BundleUtil.TableHead_DependJC);

	public static final String SummaryText = "SummaryText";
	public static final String SummaryXML = "SummaryXML";
	public static final String RelationText = "RelationText";
	public static final String CouplingText = "CouplingText";
	public static final String CohesionText = "CohesionText";
	public static final String NoticesText = "NoticesText";

	public static final Map<String, String> Tips = new HashMap<String, String>();

	static {
		Tips.put(AC, "具有抽象类计数资格的类数量");
		Tips.put(Ca, "依赖本分析单元的分析单元数量");
		Tips.put(Ce, "本分析单元依赖的分析单元数量");
		Tips.put(A, "具有抽象类计数资格的类比例，值越大越抽象");
		Tips.put(V, "稳定的类比例，值越大越不易变");
		Tips.put(I, "传出比例，值越小越稳定");
		Tips.put(D, "abs(抽象程度 + 易变性 + 稳定性 - 1)，值越接近零越合理");
		Tips.put(Coupling, "sum(依赖的JavaClass * Relation强度)，值越小越好");
		Tips.put(Cohesion, "sum(内部的JavaClass * Relation强度)，值越大越好");
		Tips.put(OO, "私有属性 / 公开方法，值越大越好");
		Tips.put(Encapsulation, "私有类比例，值越大越好");
		Tips.put(Balance, "内聚值/(内聚值+分组耦合最大顺序差值)，分析单元内聚性指标。值越大越好");
		Tips.put(Relation_Balance, "关系双方内聚值 - 关系耦合值，关系的平衡指数。值越大越好");
	}

	public static String toMetrics(String title) {
		return toMetricses.get(title);
	}

	private static final Map<String, String> toMetricses = new HashMap<String, String>();

	static {
		toMetricses.put(Name, MetricsMgr.Name);
		toMetricses.put(LC, MetricsMgr.LC);
		toMetricses.put(CN, MetricsMgr.CN);
		toMetricses.put(CC, MetricsMgr.CC);
		toMetricses.put(AC, MetricsMgr.AC);
		toMetricses.put(Ca, MetricsMgr.Ca);
		toMetricses.put(Ce, MetricsMgr.Ce);
		toMetricses.put(A, MetricsMgr.A);
		toMetricses.put(V, MetricsMgr.V);
		toMetricses.put(I, MetricsMgr.I);
		toMetricses.put(D, MetricsMgr.D);
		toMetricses.put(Coupling, MetricsMgr.Coupling);
		toMetricses.put(Cohesion, MetricsMgr.Cohesion);
		toMetricses.put(Balance, MetricsMgr.Balance);
		toMetricses.put(Encapsulation, MetricsMgr.Encapsulation);
		toMetricses.put(OO, MetricsMgr.OO);
		toMetricses.put(Cycle, MetricsMgr.Cycle);

		toMetricses.put(Component_Area, Component.Area);

		toMetricses.put(JavaClass_State, JavaClass.State);
		toMetricses.put(JavaClass_Stable, JavaClass.Stable);
		toMetricses.put(JavaClass_isPrivateElement, JavaClass.isPrivateElement);

		toMetricses.put(Relation_CurrentName, Relation.CurrentName);
		toMetricses.put(Relation_DependName, Relation.DependName);
		toMetricses.put(Relation_Intensity, Relation.Intensity);
		toMetricses.put(Relation_CurrentCohesion, Relation.CurrentCohesion);
		toMetricses.put(Relation_DependCohesion, Relation.DependCohesion);
		toMetricses.put(Relation_Balance, Relation.Balance);
		toMetricses.put(Relation_AttentionType, Relation.AttentionType);
		toMetricses.put(Relation_AttentionLevel, Relation.AttentionLevel);
	}

	private static final Map<String, String> toMetricsNames = new HashMap<String, String>();

	static {
		toMetricsNames.put(AnalysisResult.Metrics_D, Result_Metrics_D);
		toMetricsNames.put(AnalysisResult.Metrics_Balance, Result_Metrics_Balance);
		toMetricsNames.put(AnalysisResult.Metrics_RelationRationality, Result_Metrics_RelationRationality);
		toMetricsNames.put(AnalysisResult.Metrics_OO, Result_Metrics_OO);
		toMetricsNames.put(AnalysisResult.Metrics_Encapsulation, Result_Metrics_Encapsulation);
	}

	public static String toMetricsName(String metrics) {
		return toMetricsNames.get(metrics);
	}

}
