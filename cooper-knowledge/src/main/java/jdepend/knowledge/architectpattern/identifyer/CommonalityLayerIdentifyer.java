package jdepend.knowledge.architectpattern.identifyer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.knowledge.architectpattern.AbstractArchitectPatternIdentifyer;
import jdepend.knowledge.architectpattern.domain.IdealComponent;
import jdepend.model.Component;
import jdepend.model.MetricsMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;

public final class CommonalityLayerIdentifyer extends AbstractArchitectPatternIdentifyer {

	@Override
	public void identify(AnalysisResult result) throws JDependException {
		List<Component> components = new ArrayList<Component>();
		for(Component component : result.getComponents()){
			components.add(new IdealComponent(component));
		}
		Collections.sort(components, new JDependUnitByMetricsComparator(MetricsMgr.Ca));
		// 计算传入差值
		List<Integer> diffs = new ArrayList<Integer>();
		for (int index = 0; index < components.size() - 1; index++) {
			diffs.add(components.get(index + 1).getAfferents().size() - components.get(index).getAfferents().size());
		}
		// 计算传入差值均值
		float averageDiff;
		Integer countDiff = 0;
		for (Integer diff : diffs) {
			countDiff += diff;
		}
		averageDiff = countDiff * 1F / diffs.size();
		// 计算大于均值的索引
		int index = 0;
		List<Integer> indexes = new ArrayList<Integer>();
		for (Integer diff : diffs) {
			index++;
			if (diff > averageDiff) {
				indexes.add(index);
			}
		}
		indexes.add(components.size());
		// 对组件进行分组
		int start = 0;
		int layerIndex = 0;
		Map<String, Collection<Component>> layers = new LinkedHashMap<String, Collection<Component>>();
		List<Component> layerComponents;
		for (Integer i : indexes) {
			layerComponents = new ArrayList<Component>();
			for (int j = start; j < i; j++) {
				layerComponents.add(components.get(j));
			}
			layers.put(String.valueOf(layerIndex), layerComponents);
			layerIndex++;
			start += layerComponents.size();
		}
		// 设置结果
		this.getWorker().setLayers(layers);
	}

}
