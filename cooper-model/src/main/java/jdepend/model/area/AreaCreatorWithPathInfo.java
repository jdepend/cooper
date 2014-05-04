package jdepend.model.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import jdepend.model.Component;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.ComponentPathSegment;

public class AreaCreatorWithPathInfo extends AbstractAreaCreator {

	@Override
	public Map<String, Collection<Component>> calCoverCount(AnalysisResult result) {

		Map<String, Collection<Component>> areaComponents = new HashMap<String, Collection<Component>>();

		List<String> paths = new ArrayList<String>();
		for (Component unit : result.getComponents()) {
			paths.add(unit.getPath());
		}
		List<ComponentPathSegment> segments = ComponentPathSegment.create(paths);
		int maxCount = segments.get(0).getCount();

		List<String> areaKeys = new ArrayList<String>();
		for (ComponentPathSegment segment : segments) {
			if (segment.getCount() < maxCount && segment.getCount() > 1) {
				areaKeys.add(segment.getName());
			}
		}
		// 生成AreaComponentInfo
		Collection<Component> areaComponented = new HashSet<Component>();
		Collection<Component> components = null;
		for (String areaKey : areaKeys) {
			components = new ArrayList<Component>();
			for (Component component : result.getComponents()) {
				if (!areaComponented.contains(component) && component.getPath().indexOf(areaKey) != -1) {
					components.add(component);
					areaComponented.add(component);
				}

			}
			if (components.size() > 0) {
				areaComponents.put(areaKey, components);
			}
		}

		return areaComponents;
	}

}
