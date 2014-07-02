package jdepend.util.refactor;

import jdepend.model.Measurable;
import jdepend.model.result.AnalysisResult;

public class JavaClassCompareObject extends CompareObject {

	public JavaClassCompareObject(Object value, String id, String metrics) {
		super(value, id, metrics);
	}

	@Override
	public Object getOriginalityValue(AnalysisResult result) {
		Measurable measurable = result.getTheClass(this.getId());
		if (measurable != null) {
			return measurable.getValue(this.getMetrics());
		} else {
			return null;
		}
	}

}
