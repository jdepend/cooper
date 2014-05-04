package jdepend.knowledge.pattern.impl1;

import jdepend.model.result.AnalysisResult;

public abstract class AbstractPatternIdentifyer implements PatternIdentifyer {

	private AnalysisResult result;

	protected AnalysisResult getResult() {
		return result;
	}

	protected void setResult(AnalysisResult result) {
		this.result = result;
	}

}
