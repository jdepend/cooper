package jdepend.knowledge;

import java.io.Serializable;

import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;

/**
 * 领域分析器
 * 
 * @author wangdg
 * 
 */
public interface DomainAnalysis extends Serializable {

	public String getName();

	public String getTip();

	public StructureCategory getStructureCategory();

	public boolean isEnable();

	public void setEnable(boolean enable);

	/**
	 * 
	 * @param name
	 *            结构名称
	 * @param data
	 *            结构内容
	 * @return
	 * @throws JDependException 
	 */
	public AdviseInfo advise(String name, AnalysisResult data) throws JDependException;

}
