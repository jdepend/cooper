package jdepend.model;

import java.util.HashMap;
import java.util.Map;

import jdepend.model.result.AnalysisResult;

public class MetricsEvaluate {

	private static Map<String, Boolean> evaluateBigs;

	static {
		evaluateBigs = new HashMap<String, Boolean>();

		evaluateBigs.put(AnalysisResult.Metrics_TotalScore, true);
		evaluateBigs.put(AnalysisResult.Metrics_D, true);
		evaluateBigs.put(AnalysisResult.Metrics_Balance, true);
		evaluateBigs.put(AnalysisResult.Metrics_RelationRationality, true);
		evaluateBigs.put(AnalysisResult.Metrics_Encapsulation, true);
		evaluateBigs.put(AnalysisResult.Metrics_RelationComponentScale, false);
		evaluateBigs.put(AnalysisResult.Metrics_Coupling, false);
		evaluateBigs.put(AnalysisResult.Metrics_Cohesion, true);
		evaluateBigs.put(Relation.CurrentCohesion, true);
		evaluateBigs.put(Relation.DependCohesion, true);
		evaluateBigs.put(Relation.Intensity, false);
		evaluateBigs.put(Relation.Balance, true);
		evaluateBigs.put(Relation.AttentionLevel, false);
		evaluateBigs.put(Relation.AttentionType, false);
		evaluateBigs.put(Relation.isProblem, false);
		evaluateBigs.put(JavaClassUnit.Stable, false);
		evaluateBigs.put(JavaClassUnit.isPrivateElement, true);
		evaluateBigs.put(MetricsMgr.A, true);
		evaluateBigs.put(MetricsMgr.I, false);
		evaluateBigs.put(MetricsMgr.D, true);
		evaluateBigs.put(MetricsMgr.V, true);
		evaluateBigs.put(MetricsMgr.Coupling, false);
		evaluateBigs.put(MetricsMgr.Cohesion, true);
		evaluateBigs.put(MetricsMgr.Balance, true);
		evaluateBigs.put(MetricsMgr.Encapsulation, true);
		evaluateBigs.put(MetricsMgr.Cycle, false);
		evaluateBigs.put(MetricsMgr.A, true);

	}

	public static Boolean evaluate(int result, String metrics) {

		Boolean biger = evaluateBigs.get(metrics);
		if (biger == null) {
			return null;
		} else {
			if (result < 0) {
				return !biger;
			} else {
				return biger;
			}
		}
	}

}
