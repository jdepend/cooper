package jdepend.knowledge.domainanalysis;

import jdepend.framework.exception.JDependException;
import jdepend.knowledge.AbstractDomainAnalysis;
import jdepend.knowledge.AdviseInfo;
import jdepend.knowledge.StructureCategory;
import jdepend.knowledge.architectpattern.ArchitectPatternMgr;
import jdepend.knowledge.architectpattern.ArchitectPatternResult;
import jdepend.model.result.AnalysisResult;

public final class ArchitectPatternDomainAnalysis extends AbstractDomainAnalysis {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5627444730583565640L;

	public ArchitectPatternDomainAnalysis() {
		super("架构模式识别分析器", "用于识别目标系统的整体结构模式");
	}

	@Override
	protected AdviseInfo doAdvise(String name, AnalysisResult result) throws JDependException {

		ArchitectPatternResult apResult = ArchitectPatternMgr.getInstance().identify(result);
		String desc = apResult.getResult();
		if (desc != null) {
			return new AdviseInfo(desc);
		} else {
			return null;
		}
	}

	@Override
	public StructureCategory getStructureCategory() {
		return StructureCategory.ArchitectPatternDomainAnalysis;
	}
}
