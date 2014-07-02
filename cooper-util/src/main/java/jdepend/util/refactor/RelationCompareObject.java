package jdepend.util.refactor;

import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;

public class RelationCompareObject extends CompareObject {

	public RelationCompareObject(Object value, String id, String metrics) {
		super(value, id, metrics);
	}

	@Override
	public Object getOriginalityValue(AnalysisResult result) {
		String current = this.getId().substring(0, this.getId().indexOf('|'));
		String depend = this.getId().substring(this.getId().indexOf('|') + 1);
		Relation relation = result.getTheRelation(current, depend);
		if (relation != null) {
			return relation.getValue(this.getMetrics());
		} else {
			return null;
		}
	}

}
