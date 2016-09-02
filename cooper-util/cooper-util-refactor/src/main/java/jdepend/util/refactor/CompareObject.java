package jdepend.util.refactor;

import jdepend.model.result.AnalysisResult;

public abstract class CompareObject {

	private String id;
	private String metrics;
	private Object value;

	public CompareObject(Object value, String id, String metrics) {
		super();
		this.value = value;
		this.id = id;
		this.metrics = metrics;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMetrics() {
		return metrics;
	}

	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public abstract Object getOriginalityValue(AnalysisResult result);

	public Boolean evaluate(int result, String metrics) {
		return null;
	}
}
