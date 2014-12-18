package jdepend.util.refactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;

final class DefaultRefactorTool implements RefactorTool {

	@Override
	public void createComponent(String componentName, int componentLayer) throws JDependException {
		// 保存当前快照
		AdjustHistory.getInstance().addMemento();
		List<String> actions = new ArrayList<String>();
		actions.add("创建新组件[" + componentName + "]");
		AdjustHistory.getInstance().setActions(actions);
		// 添加新组件
		JDependUnitMgr.getInstance().getResult().addComponent(componentName, componentLayer);
		// 清空缓存
		JDependUnitMgr.getInstance().getResult().clearCache();
		// 保存调整之后的结果
		AdjustHistory.getInstance().setCurrent(JDependUnitMgr.getInstance().getResult());
	}

	@Override
	public void deleteComponent(String componentName) throws JDependException {
		// 保存当前快照
		AdjustHistory.getInstance().addMemento();
		List<String> actions = new ArrayList<String>();
		actions.add("删除组件[" + componentName + "]");
		AdjustHistory.getInstance().setActions(actions);
		// 删除组件
		JDependUnitMgr.getInstance().getResult().deleteTheComponent(componentName);
		// 清空缓存
		JDependUnitMgr.getInstance().getResult().clearCache();
		// 保存调整之后的结果
		AdjustHistory.getInstance().setCurrent(JDependUnitMgr.getInstance().getResult());
	}

	@Override
	public void moveClass(Collection<JavaClass> javaClasses, Component target) throws JDependException {
		boolean adjust = false;
		for (JavaClass javaClass : javaClasses) {
			if (!javaClass.getComponent().equals(target)) {
				adjust = true;
				break;
			}
		}

		if (!adjust) {
			return;
		}

		AdjustHistory.getInstance().addMemento();

		List<String> actions = new ArrayList<String>();
		StringBuilder action;
		for (JavaClass javaClass : javaClasses) {
			action = new StringBuilder();
			action.append(javaClass.getName());
			action.append(" 从 ");
			action.append(javaClass.getComponent().getName());
			action.append(" 移动到 ");
			action.append(target.getName());

			actions.add(action.toString());
		}

		AdjustHistory.getInstance().setActions(actions);

		for (JavaClass javaClass : javaClasses) {
			if (!javaClass.getComponent().equals(target)) {
				// 删除执行的JavaClass
				javaClass.getComponent().removeJavaClass(javaClass);
				// 增加到新的组件中
				target.addJavaClass(javaClass);
			}
		}
		// 清空缓存
		JDependUnitMgr.getInstance().getResult().clearCache();
		// 保存调整之后的结果
		AdjustHistory.getInstance().setCurrent(JDependUnitMgr.getInstance().getResult());

	}

	@Override
	public void uniteComponent(String name, int layer, Collection<String> components) throws JDependException {

		AdjustHistory.getInstance().addMemento();

		List<String> actions = new ArrayList<String>();
		StringBuilder content = new StringBuilder();

		UniteComponentConf uniteComponentConf = new UniteComponentConf(name, layer, components);
		for (String component : uniteComponentConf.getComponents()) {
			content.append(component);
			content.append("\n");
		}
		content.append(" 合并到 ");
		content.append("\n");
		content.append(uniteComponentConf.getName());
		actions.add(content.toString());

		AdjustHistory.getInstance().setActions(actions);

		List<Component> units = JDependUnitMgr.getInstance().getComponents();
		List<Component> newUnits = new ArrayList<Component>();
		UniteComponent newComponent;
		// 增加未受影响的组件
		for (Component unit : units) {
			if (!uniteComponentConf.getComponents().contains(unit.getName())) {
				newUnits.add(unit);
			}
		}
		// 创建合并组件
		newComponent = new UniteComponent(uniteComponentConf.getName());
		newComponent.setSubComponents(uniteComponentConf.getComponents());
		newComponent.setLayer(uniteComponentConf.getLayer());
		newComponent.unite();

		newUnits.add(newComponent);

		JDependUnitMgr.getInstance().setComponents(newUnits);
		// 清空缓存
		JDependUnitMgr.getInstance().getResult().clearCache();
		// 保存调整之后的结果
		AdjustHistory.getInstance().setCurrent(JDependUnitMgr.getInstance().getResult());
	}
}
