package jdepend.knowledge.architectpattern.identifyer;

import java.util.Collections;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.knowledge.architectpattern.AbstractArchitectPatternIdentifyer;
import jdepend.model.Component;
import jdepend.model.MetricsMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;

public final class CoreComponentIdentifyer extends AbstractArchitectPatternIdentifyer {

	@Override
	public void identify(AnalysisResult result) throws JDependException {

		List<Component> components = result.getComponents();
		Collections.sort(components, new JDependUnitByMetricsComparator(MetricsMgr.I));
		// 计算不是孤立的组件个数
		int componentSize = 0;
		for (Component component : components) {
			if (component.getAfferents().size() > 0 || component.getEfferents().size() > 0) {
				componentSize++;
			}
		}
		int size;
		for (Component component : components) {
			// 传入净值占整个组件个数的六成以上
			size = component.getAfferents().size() - component.getEfferents().size();
			if (size * 1F / componentSize > 0.6F) {
				this.getWorker().addCoreComponent(component);
			}
		}
	}

}
