package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.util.refactor.RefactorToolFactory;

public abstract class MoveRelationTODOItem extends TODOItem {

	private RelationData relationData;

	protected MoveRelationInfo moveRelationInfo;

	public MoveRelationTODOItem(RelationData relationData) {
		super();
		this.relationData = relationData;
	}

	@Override
	public List<Object> execute() throws JDependException {

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

	public RelationData getRelationData() {
		return relationData;
	}

	@Override
	public List<Object> getInfo() {
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
			List<Object> infos = new ArrayList<Object>();
			infos.add(info);
			infos.add(relationData);
			return infos;
		} else {
			return null;
		}
	}

}
