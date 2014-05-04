package jdepend.util.refactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.component.MemoryComponent;

final class DefaultRefactorTool implements RefactorTool {

	@Override
	public void createComponent(String componentName, int componentLayer) throws JDependException {
		// 保存当前快照
		AdjustHistory.getInstance().addMemento();
		List<String> actions = new ArrayList<String>();
		actions.add("创建新组件[" + componentName + "]");
		AdjustHistory.getInstance().setActions(actions);
		// 添加新组件
		List<Component> units = JDependUnitMgr.getInstance().getComponents();
		MemoryComponent newComponent = new MemoryComponent(componentName);
		newComponent.setLayer(componentLayer);
		units.add(newComponent);
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
		List<Component> units = JDependUnitMgr.getInstance().getComponents();
		JDependUnit deleteUnit = null;
		for (JDependUnit unit : units) {
			if (unit.getName().equals(componentName)) {
				deleteUnit = unit;
				units.remove(unit);
				break;
			}
		}
		// 删除JavaClass间的关系
		if (deleteUnit != null) {
			Iterator<JavaClassRelationItem> it;
			for (JavaClass javaClass : deleteUnit.getClasses()) {
				for (JavaClass dependClass : javaClass.getCaList()) {
					it = dependClass.getCeItems().iterator();
					while (it.hasNext()) {
						if (it.next().getDepend().equals(javaClass)) {
							it.remove();
						}
					}
				}
				for (JavaClass dependClass : javaClass.getCeList()) {
					it = dependClass.getCaItems().iterator();
					while (it.hasNext()) {
						if (it.next().getDepend().equals(javaClass)) {
							it.remove();
						}
					}
				}
			}

		}
		// 清空缓存
		JDependUnitMgr.getInstance().getResult().clearCache();
		// 保存调整之后的结果
		AdjustHistory.getInstance().setCurrent(JDependUnitMgr.getInstance().getResult());
	}

	@Override
	public void moveClass(Collection<JavaClass> javaClasses, Component target) throws JDependException {
		(new JavaClassMoveToAdjust()).adjust(javaClasses, target);

	}

	@Override
	public void uniteComponent(String componentName, int layer, Collection<String> components) throws JDependException {
		UniteComponentMgr.getInstance().addUniteComponent(componentName, layer, components);
		UniteComponentMgr.getInstance().unite();
	}

	@Override
	public void clear() throws JDependException {
		// 清空合并历史
		UniteComponentMgr.getInstance().clear();
	}

}
