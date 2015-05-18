package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;

public class AdjustAbstractTODOIdentifyer implements TODOIdentifyer {

	private static final Float AdjustAbstractTODOItemOrder = 10F;

	@Override
	public List<TODOItem> identify(TODOIdentifyInfo info) throws JDependException {

		List<TODOItem> list = new ArrayList<TODOItem>();

		TODOItem item = null;
		for (Component component : info.getResult().getComponents()) {
			if (component.getDistance() > 0.8F) {
				item = new AdjustAbstractTODOItem(component);
				if (component.getStability() < 0.5) {
					item.setContent("组件[" + component.getName() + "]的抽象程度不够");
				} else {
					item.setContent("组件[" + component.getName() + "]的抽象程度过大");
				}

				item.setOrder(AdjustAbstractTODOItemOrder + component.getDistance());

				list.add(item);
			}
		}

		return list;
	}

}
