package jdepend.knowledge.domainanalysis;

import jdepend.framework.exception.JDependException;

public interface Expert {

	public AdviseInfo advise(Structure structure) throws JDependException;
	
	public void registDomainAnalysis(DomainAnalysis domainAnalysis);

}
