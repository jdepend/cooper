package jdepend.util.analyzer.element;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.Method;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class SearchDAONoPageMethod extends AbstractAnalyzer {

	private String daoEndClassName;
	private String pageType;

	public SearchDAONoPageMethod() {
		super("搜索DAO上的没有分页信息的方法", Analyzer.Attention, "搜索DAO上的没有分页信息的方法");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {
		if (daoEndClassName == null || daoEndClassName.length() == 0) {
			throw new JDependException("没有daoEndClassName参数的定义");
		}
		if (pageType == null || pageType.length() == 0) {
			throw new JDependException("没有pageType参数的定义");
		}
		for (JavaClass javaClass : result.getClasses()) {
			if (javaClass.getName().endsWith(daoEndClassName)) {
				for (Method method : javaClass.getSelfMethods()) {
					if (method.existReturn()) {
						if (method.getReturnTypes().contains("java.util.List")) {
							if (!method.getArgumentTypes().contains(pageType)) {
								this.print(javaClass.getName() + "." + method.getName());
								this.print("\n");
							}
						}
					}
				}
			}
		}
	}

	public String getDaoEndClassName() {
		return daoEndClassName;
	}

	public void setDaoEndClassName(String daoEndClassName) {
		this.daoEndClassName = daoEndClassName;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

}
