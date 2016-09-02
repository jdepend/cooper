package jdepend.knowledge.domainanalysis;


public class DefaultExpert implements Expert {

	public AdviseInfo advise(Structure structure) throws ExpertException {
		return DomainAnalysisMgr.getIntance().getTheDomainAnalysis(structure.getCategory())
				.advise(structure.getName(), structure.getData());
	}

	@Override
	public void registDomainAnalysis(DomainAnalysis domainAnalysis) {
		DomainAnalysisMgr.getIntance().addDomainAnalysis(domainAnalysis);
	}

}
