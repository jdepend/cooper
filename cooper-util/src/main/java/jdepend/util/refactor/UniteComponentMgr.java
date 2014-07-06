package jdepend.util.refactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;
import jdepend.model.component.UniteComponent;

/**
 * 组件合并管理器
 * 
 * @author ibmuser
 * 
 */
final class UniteComponentMgr {

	private static UniteComponentMgr _mgr = new UniteComponentMgr();

	private UniteComponentInfo uniteComponentInfo = new UniteComponentInfo();

	private UniteComponentConf currentAction;

	public static UniteComponentMgr getInstance() {
		return _mgr;
	}

	public void addUniteComponent(String name, int layer, Collection<String> components) throws JDependException {
		this.currentAction = this.uniteComponentInfo.add(name, layer, components);
	}

	public UniteComponentInfo getUniteComponentInfo() {
		return this.uniteComponentInfo;
	}

	public void clear() {
		this.uniteComponentInfo.clear();
	}

	/**
	 * 合并组件
	 * 
	 * @param units
	 * @return
	 * @return
	 * @throws JDependException
	 */
	public void unite() throws JDependException {

		AdjustHistory.getInstance().addMemento();

		AdjustHistory.getInstance().setActions(this.calActions());

		List<Component> newUnits = this.doUnite(JDependUnitMgr.getInstance().getComponents());
		JDependUnitMgr.getInstance().setComponents(newUnits);
		// 清空缓存
		JDependUnitMgr.getInstance().getResult().clearCache();
		// 保存调整之后的结果
		AdjustHistory.getInstance().setCurrent(JDependUnitMgr.getInstance().getResult());
	}

	private List<String> calActions() {

		if (this.currentAction == null) {
			return new ArrayList<String>();
		} else {
			List<String> actions = new ArrayList<String>();
			StringBuilder content = new StringBuilder();
			for (String component : this.currentAction.getComponents()) {
				content.append(component);
				content.append("\n");
			}
			content.append(" 合并到 ");
			content.append("\n");
			content.append(this.currentAction.getName());
			actions.add(content.toString());
			return actions;
		}
	}

	protected List<Component> doUnite(List<Component> units) {

		List<Component> newUnits = new ArrayList<Component>();
		UniteComponent newComponent;
		// 增加未受影响的组件
		for (Component unit : units) {
			if (!this.currentAction.getComponents().contains(unit.getName())) {
				newUnits.add(unit);
			}
		}
		// 创建合并组件
		newComponent = new UniteComponent(this.currentAction.getName());
		newComponent.setSubComponents(this.currentAction.getComponents());
		newComponent.setLayer(this.currentAction.getLayer());
		for (String component : this.currentAction.getComponents()) {
			for (Component unit1 : units) {
				if (unit1.getName().equals(component)) {
					for (JavaClass javaClass : unit1.getClasses()) {
						newComponent.addJavaClass(javaClass);
					}
				}
			}
		}
		newUnits.add(newComponent);

		return newUnits;
	}
}
