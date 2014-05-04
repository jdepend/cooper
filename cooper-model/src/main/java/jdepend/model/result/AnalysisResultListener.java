package jdepend.model.result;

import jdepend.framework.exception.JDependException;

public interface AnalysisResultListener {
	
	/**
	 * 当分析执行完后设置result时调用
	 * 
	 * @param result
	 * @throws JDependException
	 */
	public void onExecuted(AnalysisResult result) throws JDependException;

}
