package jdepend.knowledge.motive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jdepend.model.AreaComponent;
import jdepend.model.Component;
import jdepend.model.JDependUnitMgr;

public class MotiveCreator {

	private MotiveContainer motiveContainer;

	public MotiveCreator(MotiveContainer motiveContainer) {
		this.motiveContainer = motiveContainer;
	}

	public Collection<Motive> create() {

		Collection<Motive> motives = new ArrayList<Motive>();
		StringBuilder desc = null;
		StringBuilder desc1 = null;

		List<AreaComponent> areaComponents = this.motiveContainer.getAreas();
		int areaComponentCount = areaComponents.size();
		if (areaComponentCount > 1) {
			Collections.sort(areaComponents);
			if (areaComponentCount == 2) {
				if (areaComponents.get(0).instability() > areaComponents.get(1).instability()) {
					desc = new StringBuilder();
					desc.append("系统被设计成两个组件层，一个是平台层[");
					desc.append(areaComponents.get(1).getName());
					desc.append("]，一个是业务层[");
					desc.append(areaComponents.get(0).getName());
					desc.append("]");

					motives.add(new Motive(desc.toString()));
				}
			} else if (areaComponentCount > 3) {
				int count = 0;
				L: for (int index = areaComponentCount - 1; index > 1; index--) {
					if (areaComponents.get(index - 1).instability() > areaComponents.get(index).instability()) {
						count++;
						if (count > 1) {
							desc = new StringBuilder();
							desc.append("系统被设计成多个组件层，组件层由不稳定到稳定的顺序为[");
							for (AreaComponent areaComponent : areaComponents) {
								desc.append(areaComponent.getName());
								desc.append(",");
							}
							desc.delete(desc.length() - 1, desc.length());
							desc.append("]");
							motives.add(new Motive(desc.toString()));
							break L;
						}
					}
				}
			}
			// 识别复用的组件层
			desc = new StringBuilder();
			AreaComponent areaComponent;
			M: for (int index = areaComponentCount - 1; index > 0; index--) {
				areaComponent = areaComponents.get(index);
				if (areaComponent.instability() == 0) {
					desc.append(areaComponent.getName());
					desc.append(",");
				} else {
					break M;
				}
			}
			if (desc.length() > 0) {
				desc.delete(desc.length() - 1, desc.length());
				desc1 = new StringBuilder();
				desc1.append("[");
				desc1.append(desc);
				desc1.append("]被设计成可以独立复用的组件层");

				motives.add(new Motive(desc1.toString()));
			}
		}
		// 识别设计为被复用的组件
		desc = new StringBuilder();
		List<Component> components = JDependUnitMgr.getInstance().getComponents();
		for (Component component : components) {
			if (component.stability() == 0) {
				desc.append(component.getName());
				desc.append(",");
			}
		}
		if (desc.length() > 0) {
			desc.delete(desc.length() - 1, desc.length());
			desc1 = new StringBuilder();
			desc1.append("[");
			desc1.append(desc);
			desc1.append("]被设计成可以复用的组件");

			motives.add(new Motive(desc1.toString()));
		}

		return motives;
	}

}
