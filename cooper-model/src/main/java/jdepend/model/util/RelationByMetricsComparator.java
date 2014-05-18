package jdepend.model.util;

import java.util.Comparator;

import jdepend.model.Relation;

/**
 * 按指标比较关系的比较器
 * 
 * @author <b>Abner</b>
 * 
 */
public class RelationByMetricsComparator implements Comparator {

	private String metrics = null;

	private boolean asc = true;

	public RelationByMetricsComparator() {
	}

	public RelationByMetricsComparator(String metrics) {
		this.metrics = metrics;
	}

	public RelationByMetricsComparator(String metrics, boolean asc) {
		this(metrics);
		this.asc = asc;
	}

	@Override
	public int compare(Object p1, Object p2) {

		Relation a = (Relation) p1;
		Relation b = (Relation) p2;

		int rtn = 0;

		if (this.metrics != null) {
			if (this.metrics.equals(Relation.Intensity)) {
				rtn = new Float(a.getIntensity()).compareTo(b.getIntensity());
			} else if (this.metrics.equals(Relation.AttentionLevel)) {
				rtn = new Float(a.getAttentionLevel()).compareTo(b.getAttentionLevel());
			} else if (this.metrics.equals(Relation.AttentionType)) {
				rtn = new Integer(a.getAttentionType()).compareTo(b.getAttentionType());
			} else if (this.metrics.equals(Relation.Balance)) {
				rtn = new Float(a.getBalance()).compareTo(b.getBalance());
			} else {
				rtn = new Float(a.getIntensity()).compareTo(b.getIntensity());
			}
		} else {
			rtn = new Float(a.getIntensity()).compareTo(b.getIntensity());
		}

		if (this.asc) {
			return rtn;
		} else {
			return -rtn;
		}

	}

}
