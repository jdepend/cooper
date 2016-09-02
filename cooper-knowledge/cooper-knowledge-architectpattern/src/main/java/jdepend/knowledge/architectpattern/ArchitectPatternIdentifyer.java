package jdepend.knowledge.architectpattern;

import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;

public interface ArchitectPatternIdentifyer {
	
	public void identify(AnalysisResult result) throws JDependException;
	
	public void setWorker(ArchitectPatternWorker worker);

}
