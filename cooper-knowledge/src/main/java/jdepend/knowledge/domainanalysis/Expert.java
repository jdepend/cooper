package jdepend.knowledge.domainanalysis;


public interface Expert {

	public AdviseInfo advise(Structure structure) throws ExpertException;
	
	public void registDomainAnalysis(DomainAnalysis domainAnalysis);

}
