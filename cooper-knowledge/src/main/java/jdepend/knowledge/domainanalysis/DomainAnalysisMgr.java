package jdepend.knowledge.domainanalysis;

import java.util.ArrayList;
import java.util.List;

import jdepend.knowledge.domainanalysis.impl.ArchitectPatternDomainAnalysis;
import jdepend.knowledge.domainanalysis.impl.CohesionDomainAnalysis;
import jdepend.knowledge.domainanalysis.impl.DDomainAnalysis;
import jdepend.knowledge.domainanalysis.impl.EncapsulationDomainAnalysis;
import jdepend.knowledge.domainanalysis.impl.InheritDomainAnalysis;
import jdepend.knowledge.domainanalysis.impl.LowScoreItemIdentifier;
import jdepend.knowledge.domainanalysis.impl.RelationRationalityDomainAnalysis;
import jdepend.knowledge.domainanalysis.impl.SummaryDomainAnalysis;

public final class DomainAnalysisMgr {

	private static DomainAnalysisMgr mgr = new DomainAnalysisMgr();

	private List<DomainAnalysis> domainAnalysises;

	public static DomainAnalysisMgr getIntance() {
		return mgr;
	}

	private DomainAnalysisMgr() {
		this.init();
	}

	public List<DomainAnalysis> getDomainAnalysises() {
		return this.domainAnalysises;
	}

	public DomainAnalysis getTheDomainAnalysis(StructureCategory sc) throws ExpertException {
		for (DomainAnalysis domainAnalysis : domainAnalysises) {
			if (domainAnalysis.getStructureCategory().equals(sc)) {
				return domainAnalysis;
			}
		}
		throw new ExpertException("未找到类型为[" + sc + "]的领域分析器");
	}

	private void init() {
		domainAnalysises = new ArrayList<DomainAnalysis>();

		domainAnalysises.add(new ArchitectPatternDomainAnalysis());
		domainAnalysises.add(new CohesionDomainAnalysis());
		domainAnalysises.add(new DDomainAnalysis());
		domainAnalysises.add(new EncapsulationDomainAnalysis());
		domainAnalysises.add(new InheritDomainAnalysis());
		domainAnalysises.add(new LowScoreItemIdentifier());
		domainAnalysises.add(new RelationRationalityDomainAnalysis());
		domainAnalysises.add(new SummaryDomainAnalysis());
	}

	public void addDomainAnalysis(DomainAnalysis domainAnalysis) {
		this.domainAnalysises.add(domainAnalysis);
	}

}
