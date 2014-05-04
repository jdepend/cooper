package jdepend.knowledge.domainanalysis;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jdepend.framework.util.BundleUtil;
import jdepend.knowledge.AbstractDomainAnalysis;
import jdepend.knowledge.AdviseInfo;
import jdepend.knowledge.StructureCategory;
import jdepend.model.Component;
import jdepend.model.MetricsMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;

public final class EncapsulationDomainAnalysis extends AbstractDomainAnalysis {

	public EncapsulationDomainAnalysis() {
		super("封装性分析器", "用于识别封装性最差的组件");
	}

	@Override
	protected AdviseInfo doAdvise(String name, AnalysisResult result) {
		List<Component> components = result.getComponents();
		Collections.sort(components, new JDependUnitByMetricsComparator(MetricsMgr.Encapsulation));
		Iterator<Component> it = components.iterator();
		Component component = null;
		while (it.hasNext()) {
			component = it.next();
			if (component.isInner() && component.getClassCount() > 1) {
				AdviseInfo info = new AdviseInfo();
				info.setDesc(BundleUtil.getString(BundleUtil.Advise_Encapsulation_Small));
				info.addComponentName(component.getName());
				return info;
			}
		}
		return null;

	}

	@Override
	public StructureCategory getStructureCategory() {
		return StructureCategory.EncapsulationDomainAnalysis;
	}
}
