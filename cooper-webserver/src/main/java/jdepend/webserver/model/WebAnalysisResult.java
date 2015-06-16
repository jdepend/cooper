package jdepend.webserver.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jdepend.core.domain.WisdomAnalysisResult;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MetricsFormat;
import jdepend.knowledge.architectpattern.ArchitectPatternMgr;
import jdepend.knowledge.architectpattern.ArchitectPatternResult;
import jdepend.knowledge.domainanalysis.AdviseInfo;
import jdepend.knowledge.domainanalysis.StructureCategory;
import jdepend.model.Measurable;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.RelationByMetricsComparator;

public class WebAnalysisResult extends WisdomAnalysisResult {

	private static final long serialVersionUID = 6554084973911277359L;

	public WebAnalysisResult(AnalysisResult result) {
		super(result);
	}

	public String getProblemRelationScale() {
		return MetricsFormat.toFormattedPercent(this.getAttentionRelationScale());
	}

	public String getDAdvise() {
		return this.advise(StructureCategory.DDomainAnalysis);
	}

	public String getBalanceAdvise() {
		return this.advise(StructureCategory.CohesionDomainAnalysis);
	}

	public String getEncapsulationAdvise() {
		return this.advise(StructureCategory.EncapsulationDomainAnalysis);
	}

	public String getRelationRationalityAdvise() {
		return this.advise(StructureCategory.RelationRationalityDomainAnalysis);
	}

	public List<Measurable> getSummarys() {
		List<Measurable> summarys = new ArrayList<Measurable>(this.getComponents());
		summarys.add(new WebAnalysisResultSummary(this.getSummary()));
		return summarys;
	}

	@Override
	public Collection<Relation> getRelations() {
		List<Relation> relations = new ArrayList<Relation>(super.getRelations());
		Collections.sort(relations, new RelationByMetricsComparator(Relation.AttentionLevel, false));
		return relations;
	}

	public ArchitectPatternResult getArchitectPatternResult() {
		try {
			return ArchitectPatternMgr.getInstance().identify(this);
		} catch (JDependException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String advise(StructureCategory category) {
		try {
			AdviseInfo advise = this.getAdvise(category);
			if (advise != null) {
				return advise.toString();
			} else {
				return null;
			}
		} catch (JDependException e) {
			e.printStackTrace();
			return null;
		}
	}

}
