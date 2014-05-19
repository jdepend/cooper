package jdepend.model.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.model.Component;
import jdepend.model.result.AnalysisResult;

public class AreaCreatorWithInstability extends AbstractAreaCreator {

	@Override
	public Map<String, Collection<Component>> calCoverCount(AnalysisResult result) {
		Map<String, Collection<Component>> areaComponents = new HashMap<String, Collection<Component>>();

		String name;
		for (Component component : result.getComponents()) {
			if (component.getStability() == 0) {
				name = "底层组件";
			} else if (component.getStability() == 1) {
				name = "高层组件";
			} else {
				name = "中层组件";
			}
			if (!areaComponents.containsKey(name)) {
				areaComponents.put(name, new ArrayList<Component>());
			}
			areaComponents.get(name).add(component);
		}

		return areaComponents;
	}
}
