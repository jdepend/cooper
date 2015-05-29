package jdepend.core.local.score;

import java.util.Comparator;

import jdepend.model.result.AnalysisResult;

/**
 * 按指标比较包的比较器
 * 
 * @author <b>Abner</b>
 * 
 */
public class ScoreByItemComparator implements Comparator {

	private String metrics = null;

	private boolean asc = true;

	public ScoreByItemComparator() {
	}

	public ScoreByItemComparator(String metrics) {
		this.metrics = metrics;
	}

	public ScoreByItemComparator(String metrics, boolean asc) {
		this(metrics);
		this.asc = asc;
	}

	@Override
	public int compare(Object p1, Object p2) {

		ScoreInfo a = (ScoreInfo) p1;
		ScoreInfo b = (ScoreInfo) p2;

		int rtn = 0;

		if (this.metrics != null) {
			if (this.metrics.equals(AnalysisResult.Metrics_LC)) {
				rtn = a.lc.compareTo(b.lc);
			} else if (this.metrics.equals(AnalysisResult.Metrics_D)) {
				rtn = a.distance.compareTo(b.distance);
			} else if (this.metrics.equals(AnalysisResult.Metrics_Balance)) {
				rtn = a.balance.compareTo(b.balance);
			} else if (this.metrics.equals(AnalysisResult.Metrics_RelationRationality)) {
				rtn = a.relation.compareTo(b.relation);
			} else if (this.metrics.equals(AnalysisResult.Metrics_TotalScore)) {
				rtn = a.score.compareTo(b.score);
			} else if (this.metrics.equals(AnalysisResult.Metrics_Encapsulation)) {
				rtn = a.encapsulation.compareTo(b.encapsulation);
			} else {
				rtn = a.score.compareTo(b.score);
			}
		} else {
			rtn = a.score.compareTo(b.score);
		}

		if (this.asc) {
			return rtn;
		} else {
			return -rtn;
		}

	}

}
