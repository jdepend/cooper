package jdepend.model.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jdepend.model.Component;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.ComponentPathSegment;

/**
 * 按着组件路径创建组件区域
 * 
 * @author user
 *
 */
public class AreaCreatorWithPathInfo extends AbstractAreaCreator {
	
	@Override
	public String getName() {
		return AreaComponentProfile.AccordingPathInfo;
	}

	@Override
	public Collection<AreaComponentInfo> calCoverCount(AnalysisResult result) {

		Collection<AreaComponentInfo> areaComponents = new ArrayList<AreaComponentInfo>();

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
				AreaComponentInfo componentInfo = new AreaComponentInfo();
				componentInfo.setName(areaKey);
				componentInfo.setLayer(0);
				componentInfo.setComponents(components);
				areaComponents.add(componentInfo);
			}
		}

		return areaComponents;
	}

}
