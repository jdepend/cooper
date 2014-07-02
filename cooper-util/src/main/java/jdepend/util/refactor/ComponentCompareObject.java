package jdepend.util.refactor;

import jdepend.model.Measurable;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisResultSummary;

public class ComponentCompareObject extends CompareObject {

	public ComponentCompareObject(Object value, String id, String metrics) {
		super(value, id, metrics);
	}

	@Override
	public Object getOriginalityValue(AnalysisResult result) {
		Measurable measurable;
		if (this.getId().equals(AnalysisResultSummary.Name)) {
			measurable = result.getSummary();
		} else {
			measurable = result.getTheComponent(this.getId());
		}
		if (measurable != null) {
			return measurable.getValue(this.getMetrics());
		} else {
			return null;
		}
	}

}
