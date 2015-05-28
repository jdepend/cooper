package jdepend.webserver.model;

import jdepend.model.AreaComponent;
import jdepend.model.result.AnalysisResultSummary;

public class WebAnalysisResultSummary extends AnalysisResultSummary {

	public WebAnalysisResultSummary(AnalysisResultSummary summary) {
		super(summary);
	}

	public AreaComponent getAreaComponent() {
		return new AreaComponent();
	}

}
