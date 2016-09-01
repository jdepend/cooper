package jdepend.model.area;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.model.Component;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.result.AnalysisResult;

/**
 * 按组件的稳定性创建组件区域
 * 
 * @author user
 *
 */
public class AreaCreatorWithInstability extends AbstractAreaCreator {
	
	@Override
	public String getName() {
		return AreaComponentProfile.AccordingInstability;
	}

	@Override
	public Collection<AreaComponentInfo> calCoverCount(AnalysisResult result) {
		Collection<AreaComponentInfo> areaComponents = new ArrayList<AreaComponentInfo>();

		int layer;
		String name;
		AreaComponentInfo componentInfo;
		for (Component component : result.getComponents()) {
			if (component.getStability() == null) {
				layer = 101;
				name = "中层组件";
			} else if (component.getStability() == 0) {
				layer = 100;
				name = "底层组件";
			} else if (component.getStability() == 1) {
				layer = 102;
				name = "高层组件";
			} else {
				layer = 101;
				name = "中层组件";
			}

			componentInfo = getTheAreaComponentInfo(areaComponents, name);
			if (componentInfo == null) {
				componentInfo = new AreaComponentInfo();
				componentInfo.setLayer(layer);
				componentInfo.setName(name);
				areaComponents.add(componentInfo);
			}
			componentInfo.getComponents().add(component);
		}

		return areaComponents;
	}
}
