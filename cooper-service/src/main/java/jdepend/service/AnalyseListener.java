package jdepend.service;

import jdepend.model.result.AnalysisResult;

public interface AnalyseListener extends Comparable<AnalyseListener>{

	void onAnalyse(AnalysisResult result) throws ServiceException;
	
	Integer order();
}
