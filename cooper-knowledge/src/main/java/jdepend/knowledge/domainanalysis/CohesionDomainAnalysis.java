package jdepend.knowledge.domainanalysis;

import java.util.List;

import jdepend.framework.util.BundleUtil;
import jdepend.knowledge.AbstractDomainAnalysis;
import jdepend.knowledge.AdviseInfo;
import jdepend.knowledge.StructureCategory;
import jdepend.knowledge.util.CohesionUtil;
import jdepend.model.Component;
import jdepend.model.result.AnalysisResult;

public final class CohesionDomainAnalysis extends AbstractDomainAnalysis {

	/**
	 * 
	 */
	private static final long serialVersionUID = -69084094898912310L;

	public CohesionDomainAnalysis() {
		super("内聚性分析器", "用于识别内聚性最差的组件");
	}

	@Override
	protected AdviseInfo doAdvise(String name, AnalysisResult result) {
		List<Component> components = CohesionUtil.sort(result);
		if (components != null && components.size() > 0) {
			AdviseInfo info = new AdviseInfo();
			info.setDesc(BundleUtil.getString(BundleUtil.Advise_Cohesion_Small));
			info.addComponentName(components.get(0).getName());
			return info;
		} else {
			return null;
		}

	}

	@Override
	public StructureCategory getStructureCategory() {
		return StructureCategory.CohesionDomainAnalysis;
	}
}
