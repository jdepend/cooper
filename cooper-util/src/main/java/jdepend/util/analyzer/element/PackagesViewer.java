package jdepend.util.analyzer.element;

import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public final class PackagesViewer extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5209815140102810522L;

	public PackagesViewer() {
		super("以树的形式浏览包", Analyzer.Attention, "以树的形式浏览包");
	}

	protected void doSearch(AnalysisResult result) throws JDependException {
		this.printTree(result.getJavaPackageTree());
	}

}
