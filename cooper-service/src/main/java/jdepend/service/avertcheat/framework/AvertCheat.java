package jdepend.service.avertcheat.framework;

import jdepend.model.result.AnalysisRunningContext;

/**
 * 防作弊器接口
 * 
 * @author wangdg
 * 
 */
public interface AvertCheat {

	public String getName();

	public String getTip();

	public boolean enable(AnalysisRunningContext context);

}
