package jdepend.service.local;

import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;

public interface AnalyseListener extends Comparable<AnalyseListener>{

	void onAnalyse(AnalysisResult result) throws JDependException;
	
	Integer order();
}
