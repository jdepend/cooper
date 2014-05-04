package jdepend.util.refactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;

class JavaClassMoveToAdjust {

	public void adjust(Collection<JavaClass> javaClasses, Component target) throws JDependException {

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

		AdjustHistory.getInstance().setActions(this.calActions(javaClasses, target));

		this.doAdjust(javaClasses, target);
		// 清空缓存
		JDependUnitMgr.getInstance().getResult().clearCache();
		// 保存调整之后的结果
		AdjustHistory.getInstance().setCurrent(JDependUnitMgr.getInstance().getResult());
	}

	protected List<String> calActions(Collection<JavaClass> javaClasses, JDependUnit target) {
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
		return actions;

	}

	protected void doAdjust(Collection<JavaClass> javaClasses, Component target) throws JDependException {

		for (JavaClass javaClass : javaClasses) {
			if (!javaClass.getComponent().equals(target)) {
				// 得到源组件
				JDependUnit sourceComponent = javaClass.getComponent();
				// 删除执行的JavaClass
				sourceComponent.getClasses().remove(javaClass);
				// 设置新的所属组件
				javaClass.setComponent(target);
				// 增加到新的组件中
				target.getClasses().add(javaClass);
			}
		}
	}
}
