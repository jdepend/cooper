package jdepend.webserver.web;

import jdepend.framework.util.MetricsFormat;
import jdepend.model.result.AnalysisResult;

public class WebAnalysisResult extends AnalysisResult {

	private static final long serialVersionUID = 6554084973911277359L;

	public WebAnalysisResult(AnalysisResult result) {
		super(result.getComponents(), result.getRunningContext());
	}

	public String getProblemRelationScale() {
		return MetricsFormat.toFormattedPercent(this.getAttentionRelationScale());
	}

}
