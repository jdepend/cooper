package jdepend.util.todolist;

import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;

public interface TODOIdentifyer {
	
	public List<TODOItem> identify(AnalysisResult result) throws JDependException;

}
