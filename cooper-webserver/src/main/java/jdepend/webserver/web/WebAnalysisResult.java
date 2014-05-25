package jdepend.webserver.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MetricsFormat;
import jdepend.knowledge.AdviseInfo;
import jdepend.knowledge.ExpertFactory;
import jdepend.knowledge.Structure;
import jdepend.knowledge.StructureCategory;
import jdepend.knowledge.architectpattern.ArchitectPatternMgr;
import jdepend.knowledge.architectpattern.ArchitectPatternResult;
import jdepend.model.Measurable;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisResultSummary;
import jdepend.model.util.RelationByMetricsComparator;

public class WebAnalysisResult extends AnalysisResult {

	private static final long serialVersionUID = 6554084973911277359L;

	public WebAnalysisResult(AnalysisResult result) {
		super(result);
	}

	public String getProblemRelationScale() {
		return MetricsFormat.toFormattedPercent(this.getAttentionRelationScale());
	}

	public String getDAdvise() {
		return this.getAdvise(StructureCategory.DDomainAnalysis);
	}

	public String getBalanceAdvise() {
		return this.getAdvise(StructureCategory.CohesionDomainAnalysis);
	}

	public String getEncapsulationAdvise() {
		return this.getAdvise(StructureCategory.EncapsulationDomainAnalysis);
	}

	public String getRelationRationalityAdvise() {
		return this.getAdvise(StructureCategory.RelationRationalityDomainAnalysis);
	}

	public List<Measurable> getSummarys() {
		List<Measurable> summarys = new ArrayList<Measurable>(this.getComponents());
		summarys.add(this.getSummary());
		return summarys;
	}

	@Override
	public Collection<Relation> getRelations() {
		List<Relation> relations = new ArrayList<Relation>(super.getRelations());
		Collections.sort(relations, new RelationByMetricsComparator(Relation.AttentionLevel, false));
		return relations;
	}

	public ArchitectPatternResult getArchitectPatternResult() {
		ArchitectPatternResult apResult = null;
		try {
			return ArchitectPatternMgr.getInstance().identify(this);
		} catch (JDependException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getAdvise(StructureCategory category) {
		Structure structure = new Structure();
		structure.setCategory(category);
		structure.setData(this);
		try {
			AdviseInfo advise = new ExpertFactory().createExpert().advise(structure);
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
