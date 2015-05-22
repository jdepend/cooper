package jdepend.knowledge.domainanalysis;

import jdepend.framework.exception.JDependException;

public class DefaultExpert implements Expert {

	public AdviseInfo advise(Structure structure) throws JDependException {
		return DomainAnalysisMgr.getIntance().getTheDomainAnalysis(structure.getCategory())
				.advise(structure.getName(), structure.getData());
	}

	@Override
	public void registDomainAnalysis(DomainAnalysis domainAnalysis) {
		DomainAnalysisMgr.getIntance().addDomainAnalysis(domainAnalysis);
	}

}
