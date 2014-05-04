package jdepend.model.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jdepend.model.AreaComponent;
import jdepend.model.Component;
import jdepend.model.result.AnalysisResult;

public abstract class AbstractAreaCreator implements AreaCreator {

	private Map<String, Collection<Component>> areaComponentInfo;

	@Override
	public final int coverCount(AnalysisResult result) {
		this.areaComponentInfo = this.calCoverCount(result);
		if (this.areaComponentInfo != null) {
			int count = 0;
			for (String name : areaComponentInfo.keySet()) {
				count += areaComponentInfo.get(name).size();
			}
			return count;
		} else {
			return 0;
		}
	}

	@Override
	public int areaCount() {
		if (areaComponentInfo != null) {
			return this.areaComponentInfo.size();
		} else {
			return 0;
		}
	}

	public abstract Map<String, Collection<Component>> calCoverCount(AnalysisResult result);

	@Override
	public List<AreaComponent> create() {
		List<AreaComponent> areaComponents = new ArrayList<AreaComponent>();

		if (this.areaComponentInfo != null) {
			AreaComponent areaComponent;
			Collection<Component> compoents;
			for (String name : areaComponentInfo.keySet()) {
				compoents = areaComponentInfo.get(name);
				if (compoents != null && compoents.size() > 0) {
					areaComponent = new AreaComponent(name);
					areaComponent.setComponentList(compoents);
					areaComponents.add(areaComponent);
				}
			}
		}
		Collections.sort(areaComponents);
		return areaComponents;
	}

}
