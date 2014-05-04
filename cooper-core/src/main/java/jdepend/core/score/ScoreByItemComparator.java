package jdepend.core.score;

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
			if (this.metrics.equals(AnalysisResult.LCName)) {
				rtn = a.lc.compareTo(b.lc);
			} else if (this.metrics.equals(AnalysisResult.DName)) {
				rtn = a.d.compareTo(b.d);
			} else if (this.metrics.equals(AnalysisResult.BalanceName)) {
				rtn = a.balance.compareTo(b.balance);
			} else if (this.metrics.equals(AnalysisResult.RelationRationalityName)) {
				rtn = a.relation.compareTo(b.relation);
			} else if (this.metrics.equals(AnalysisResult.ScoreName)) {
				rtn = a.score.compareTo(b.score);
			} else if (this.metrics.equals(AnalysisResult.OOName)) {
				rtn = a.oo.compareTo(b.oo);
			} else if (this.metrics.equals(AnalysisResult.EncapsulationName)) {
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
