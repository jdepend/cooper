package jdepend.util.todolist;

import java.util.Collection;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Relation;
import jdepend.util.refactor.RefactorToolFactory;

public abstract class MoveRelationTODOItem extends TODOItem {

	private RelationData relationData;

	protected MoveRelationInfo moveRelationInfo;

	public MoveRelationTODOItem(Relation relation) {
		super();
		this.relationData = new RelationData(relation);
	}

	@Override
	public StringBuilder execute() throws JDependException {

		if (moveRelationInfo.getMoveClasses() != null && moveRelationInfo.getTargetComponent() != null) {
			// 将依赖的Class移入到被依赖的Class所在的组件中
			RefactorToolFactory.createTool().moveClass(moveRelationInfo.getMoveClasses(),
					moveRelationInfo.getTargetComponent());
			// 记录日志
			BusiLogUtil.getInstance().businessLog(Operation.moveToClass);
		}
		return null;
	}

	public final boolean isMove() throws JDependException {
		this.relationData.init();
		return this.decision();
	}

	protected abstract boolean decision() throws JDependException;

	public boolean isChangeDir() {
		return moveRelationInfo.isChangeDir();
	}

	protected RelationData getRelationData() {
		return relationData;
	}

	protected MoveRelationInfo getMoveRelationInfo() {
		return moveRelationInfo;
	}

	@Override
	public StringBuilder getInfo() {
		if (moveRelationInfo.getMoveClasses() != null && moveRelationInfo.getMoveClasses().size() > 0) {
			StringBuilder info = new StringBuilder();

			for (JavaClass javaClass : moveRelationInfo.getMoveClasses()) {
				info.append(javaClass.getName());
				info.append(" 将从 ");
				info.append(javaClass.getComponent().getName());
				info.append(" 移动到 ");
				info.append(moveRelationInfo.getTargetComponent().getName());
				info.append("\n");
			}

			Collection<JavaClassRelationItem> items = this.moveRelationInfo.getFromClassRelations();
			if (!items.isEmpty()) {
				info.append("这些类与源组件的关系明细为：\n");
				for (JavaClassRelationItem item : items) {
					info.append(item);
					info.append("\n");
				}
			}
			return info;
		} else {
			return null;
		}
	}

}
