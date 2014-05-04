package jdepend.model;

public abstract class EmptyMetrics implements Metrics {

	public void beforeAnalyze() {
	}

	public MetricsInfo getMetrics(JDependUnit unit) {
		return null;
	}

}
