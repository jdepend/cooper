package jdepend.knowledge;

import jdepend.framework.domain.PersistentBean;
import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;

public abstract class AbstractDomainAnalysis extends PersistentBean implements DomainAnalysis {

	private boolean enable = true;

	public AdviseInfo advise(String name, AnalysisResult data) throws JDependException {
		if (!enable) {
			return null;
		} else {
			return doAdvise(name, data);
		}
	}

	protected abstract AdviseInfo doAdvise(String name, AnalysisResult data) throws JDependException;

	public AbstractDomainAnalysis() {
		super();
	}

	public AbstractDomainAnalysis(String name, String tip) {
		super(name, tip, "domainanalysis");
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
