package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import jdepend.model.Component;
import jdepend.model.MetricsMgr;
import jdepend.model.util.JDependUnitByMetricsComparator;

public class SplitComponentTODOIdentifyer implements TODOIdentifyer {

	private List<TODOItem> list;

	private Collection<Component> splitComponents;

	private static final Float SplitComponentTODOItemOrder = 100F;

	@Override
	public List<TODOItem> identify(TODOIdentifyInfo info) throws TODOListException {
		list = new ArrayList<TODOItem>();
		splitComponents = new HashSet<Component>();

		this.identifySplitCompoentWithBalance(info);
		this.identifySplitCompoentWithFissileObject(info);

		return list;

	}

	/**
	 * 根据内聚性指数拆分
	 */
	private void identifySplitCompoentWithBalance(TODOIdentifyInfo info) {
		SplitComponentTODOItem item = null;
		for (Component component : info.getResult().getComponents()) {
			if (!this.splitComponents.contains(component) && component.getBalance() != null
					&& component.getBalance() < 0.2F) {
				item = new SplitComponentTODOItem(component);
				if (item.isSplit()) {
					item.setContent("组件[" + component.getName() + "]内聚性差，需要拆分");
					item.setAccording("组件内的Class应该关系更紧密，而与其他组件中的Class关系疏远");
					item.setOrder(SplitComponentTODOItemOrder - component.getBalance());

					this.splitComponents.add(component);
					this.list.add(item);
				}
			}
		}
	}

	/**
	 * 根据易分对象识别需要拆分的组件
	 */
	private void identifySplitCompoentWithFissileObject(TODOIdentifyInfo info) {

		List<Component> components = info.getResult().getComponents();
		Collections.sort(components, new JDependUnitByMetricsComparator(MetricsMgr.Ce, false));
		int Ca;
		int Ce;
		SplitComponentTODOItem item = null;
		L: for (Component component : components) {
			if (!this.splitComponents.contains(component)) {
				Ca = component.getAfferentCoupling();
				Ce = component.getEfferentCoupling();
				if (Ca == 1 && Ca < Ce) {
					item = new SplitComponentTODOItem(component);
					if (item.isSplit()) {
						item.setContent("组件[" + component.getName() + "]作为易分对象又被其他组件依赖，需要拆分");
						item.setAccording("作为易分对象又被其他组件依赖，需要拆分");
						item.setOrder(SplitComponentTODOItemOrder - component.getBalance());

						this.splitComponents.add(component);
						this.list.add(item);
						break L;
					}
				}
			}
		}
	}

}
