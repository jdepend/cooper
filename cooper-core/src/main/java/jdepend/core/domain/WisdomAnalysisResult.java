package jdepend.core.domain;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MathUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.knowledge.domainanalysis.AdviseInfo;
import jdepend.knowledge.domainanalysis.ExpertFactory;
import jdepend.knowledge.domainanalysis.Structure;
import jdepend.knowledge.domainanalysis.StructureCategory;
import jdepend.model.result.AnalysisResult;

/**
 * 提供结构建议的Result
 * 
 * @author user
 *
 */
public class WisdomAnalysisResult extends AnalysisResult {

	public WisdomAnalysisResult(AnalysisResult result) {
		super(result);
	}

	public StringBuilder getAdvise() throws JDependException {

		StringBuilder adviseInfo = new StringBuilder();
		// 分项得分较少的项目
		try {
			AdviseInfo advise = this.getAdvise(StructureCategory.LowScoreItemIdentifier);
			if (advise != null) {
				adviseInfo.append(advise.getDesc());
				adviseInfo.append(advise.getComponentNameInfo());
				adviseInfo.append("\n\n");
			}
		} catch (JDependException e) {
			e.printStackTrace();
		}
		// 抽象程度合理性
		try {
			AdviseInfo advise = this.getAdvise(StructureCategory.DDomainAnalysis);
			if (advise != null) {
				adviseInfo.append(advise.getComponentNameInfo());
				adviseInfo.append(advise.getDesc());
				adviseInfo.append("\n");
			}
		} catch (JDependException e) {
			e.printStackTrace();
		}
		// 内聚性指数
		try {
			AdviseInfo advise = this.getAdvise(StructureCategory.CohesionDomainAnalysis);
			if (advise != null) {
				adviseInfo.append(advise);
				adviseInfo.append("\n");
			}
		} catch (JDependException e) {
			e.printStackTrace();
		}
		// 关系合理性
		Float rs = this.getAttentionRelationScale();
		if (MathUtil.isZero(rs)) {
			adviseInfo.append("未发现组件间存在异常的关系");
		} else {
			adviseInfo.append("组件间存在异常的关系比例为" + MetricsFormat.toFormattedPercent(rs));
		}
		adviseInfo.append("\n\n");
	
		adviseInfo.append(this.getAdvise(StructureCategory.Summary));

		return adviseInfo;

	}

	public AdviseInfo getAdvise(StructureCategory category) throws JDependException {
		return this.getAdvise(null, category);
	}

	public AdviseInfo getAdvise(String name, StructureCategory category) throws JDependException {
		Structure structure = new Structure();
		structure.setName(name);
		structure.setCategory(category);
		structure.setData(this);
		return new ExpertFactory().createExpert().advise(structure);
	}

}
