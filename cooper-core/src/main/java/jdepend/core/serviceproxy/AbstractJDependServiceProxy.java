package jdepend.core.serviceproxy;

import jdepend.framework.exception.JDependException;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.BuildListener;
import jdepend.parse.ParseListener;
import jdepend.service.local.AnalyseListener;

public abstract class AbstractJDependServiceProxy implements JDependServiceProxy {

	public final AnalysisResult analyze() throws JDependException {
		JDependUnitMgr.getInstance().clear();
		AnalysisResult result = this.doAnalyze();
		JDependUnitMgr.getInstance().setExecuteResult(result);
		return result;
	}

	protected abstract AnalysisResult doAnalyze() throws JDependException;

	public void addParseListener(ParseListener listener) {
	}

	public void addBuildListener(BuildListener listener) {
	}

	public void addAnalyseListener(AnalyseListener listener) {
	}
}
