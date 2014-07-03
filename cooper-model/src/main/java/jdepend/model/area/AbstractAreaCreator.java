package jdepend.model.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jdepend.model.AreaComponent;
import jdepend.model.Component;
import jdepend.model.result.AnalysisResult;

public abstract class AbstractAreaCreator implements AreaCreator {

	private Collection<AreaComponentInfo> areaComponentInfos;

	@Override
	public final int coverCount(AnalysisResult result) {
		this.areaComponentInfos = this.calCoverCount(result);
		if (this.areaComponentInfos != null) {
			int count = 0;
			for (AreaComponentInfo areaComponentInfo : areaComponentInfos) {
				count += areaComponentInfo.getComponents().size();
			}
			return count;
		} else {
			return 0;
		}
	}

	@Override
	public int areaCount() {
		if (areaComponentInfos != null) {
			return this.areaComponentInfos.size();
		} else {
			return 0;
		}
	}

	public abstract Collection<AreaComponentInfo> calCoverCount(AnalysisResult result);

	@Override
	public List<AreaComponent> create() {
		List<AreaComponent> areaComponents = new ArrayList<AreaComponent>();

		if (this.areaComponentInfos != null) {
			AreaComponent areaComponent;
			Collection<Component> components;
			for (AreaComponentInfo areaComponentInfo : areaComponentInfos) {
				components = areaComponentInfo.getComponents();
				if (components != null && components.size() > 0) {
					areaComponent = new AreaComponent(areaComponentInfo.getLayer(), areaComponentInfo.getName());
					areaComponent.setComponentList(components);
					areaComponents.add(areaComponent);
				}
			}
		}
		Collections.sort(areaComponents);
		return areaComponents;
	}

	protected static AreaComponentInfo getTheAreaComponentInfo(Collection<AreaComponentInfo> areaComponents, String name) {
		for (AreaComponentInfo areaComponent : areaComponents) {
			if (areaComponent.getName().equals(name)) {
				return areaComponent;
			}
		}
		return null;
	}

}
