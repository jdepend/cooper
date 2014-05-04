package jdepend.model.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.model.Component;
import jdepend.model.result.AnalysisResult;

public class AreaCreatorWithComponentLayer extends AbstractAreaCreator {

	@Override
	public Map<String, Collection<Component>> calCoverCount(AnalysisResult result) {
		Map<String, Collection<Component>> areaComponents = new HashMap<String, Collection<Component>>();

		String name;
		for (Component component : result.getComponents()) {
			if (component.isDefinedComponentLevel()) {
				name = Component.layerDesc(component.getLayer());
				if (!areaComponents.containsKey(name)) {
					areaComponents.put(name, new ArrayList<Component>());
				}
				areaComponents.get(name).add(component);
			}
		}

		return areaComponents;
	}
}
