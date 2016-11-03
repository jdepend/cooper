package jdepend.model;

import jdepend.model.result.AnalysisResult;

public class MetricsEvaluate {
	
	public static Boolean evaluate(int result, String metrics) {
		
		if (metrics.equals(AnalysisResult.Metrics_TotalScore)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(AnalysisResult.Metrics_D)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(AnalysisResult.Metrics_Balance)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(AnalysisResult.Metrics_RelationRationality)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(AnalysisResult.Metrics_Encapsulation)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
		} else if (metrics.equals(AnalysisResult.Metrics_RelationComponentScale)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(AnalysisResult.Metrics_Coupling)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(AnalysisResult.Metrics_Cohesion)) {
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
		} else if (metrics.equals(Relation.isProblem)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(JavaClassUnit.Stable)) {
			if (result < 0) {
				return true;
			} else {
				return false;
			}
		} else if (metrics.equals(JavaClassUnit.isPrivateElement)) {
			if (result < 0) {
				return false;
			} else {
				return true;
			}
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
				return false;
			} else {
				return true;
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
		} else {
			return null;
		}
	}

}
