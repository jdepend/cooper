package jdepend.model.area;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.model.Component;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.result.AnalysisResult;

/**
 * 按人工指定的组件层次创建组件区域
 * 
 * @author user
 *
 */
public class AreaCreatorWithComponentLayer extends AbstractAreaCreator {
	
	@Override
	public String getName() {
		return AreaComponentProfile.AccordingComponentLayer;
	}

	@Override
	public Collection<AreaComponentInfo> calCoverCount(AnalysisResult result) {
		Collection<AreaComponentInfo> areaComponents = new ArrayList<AreaComponentInfo>();

		String name;
		AreaComponentInfo componentInfo;
		for (Component component : result.getComponents()) {
			if (component.isDefinedComponentLevel()) {
				name = Component.layerDesc(component.getLayer());
				componentInfo = getTheAreaComponentInfo(areaComponents, name);
				if (componentInfo == null) {
					componentInfo = new AreaComponentInfo();
					componentInfo.setLayer(component.getLayer());
					componentInfo.setName(name);
					areaComponents.add(componentInfo);
				}
				componentInfo.getComponents().add(component);
			}
		}

		return areaComponents;
	}
}
