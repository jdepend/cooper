package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MathUtil;
import jdepend.model.GroupCouplingItem;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;
import jdepend.util.refactor.RefactorToolFactory;

public final class SplitCompoentTODOItem extends TODOItem {

	private JDependUnit unit;

	private transient Map<String, Collection<JavaClass>> moveInfos;

	public SplitCompoentTODOItem(JDependUnit unit) {
		super();
		this.unit = unit;
	}

	public JDependUnit getUnit() {
		return unit;
	}

	public boolean isSplit() {
		// 收集“寄养”JavaClass，并计算其“亲父母”
		moveInfos = new HashMap<String, Collection<JavaClass>>();
		for (JavaClass javaClass : unit.getClasses()) {
			if (MathUtil.isZero(javaClass.getBalance())) {
				GroupCouplingItem info = javaClass.getGroupCouplingInfo().getGroupCouplingItems().get(0);
				if (moveInfos.get(info.name) == null) {
					moveInfos.put(info.name, new ArrayList<JavaClass>());
				}
				moveInfos.get(info.name).add(javaClass);
			}
		}
		return true;
	}

	@Override
	public StringBuilder execute() throws JDependException {
		// 移动“寄养”JavaClass到自己的“亲父母”那里
		for (String target : moveInfos.keySet()) {
			RefactorToolFactory.createTool().moveClass(moveInfos.get(target),
					JDependUnitMgr.getInstance().getTheComponent(target));
		}

		return null;
	}

	@Override
	public StringBuilder getInfo() {
		if (this.moveInfos != null && this.moveInfos.size() > 0) {
			StringBuilder info = new StringBuilder();
			for (String target : moveInfos.keySet()) {
				for (JavaClass javaClass : moveInfos.get(target)) {
					info.append(javaClass.getName());
					info.append(" 将从 ");
					info.append(javaClass.getComponent().getName());
					info.append(" 移动到 ");
					info.append(target);
					info.append("\n");
				}
			}
			return info;
		} else {
			return null;
		}
	}
}
