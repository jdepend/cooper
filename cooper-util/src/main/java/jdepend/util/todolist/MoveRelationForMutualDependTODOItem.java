package jdepend.util.todolist;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MathUtil;
import jdepend.model.Relation;

public final class MoveRelationForMutualDependTODOItem extends MoveRelationTODOItem {

	public MoveRelationForMutualDependTODOItem(RelationData relationData) {
		super(relationData);
	}

	protected boolean decision() throws JDependException {
		if (this.getRelationData().getRelation().getAttentionType() != Relation.MutualDependAttentionType) {
			throw new JDependException("该关系不是彼此依赖");
		}
		// 根据耦合值判断需要移动Relation
		if (MathUtil.isZero(getRelationData().currentCaIntensity) || MathUtil.isZero(getRelationData().dependCeIntensity)) {
			if (getRelationData().currentCeIntensity > getRelationData().dependCaIntensity) {
				this.moveRelationInfo = new MoveRelationInfo(getRelationData().depend, getRelationData().dependOther);
				this.moveRelationInfo.setTargetComponent(this.getRelationData().getRelation().getCurrent()
						.getComponent());
			} else {
				this.moveRelationInfo = new MoveRelationInfo(getRelationData().current, getRelationData().currentOther);
				this.moveRelationInfo.setTargetComponent(this.getRelationData().getRelation().getDepend()
						.getComponent());
			}
			this.moveRelationInfo.setChangeDir(true);
		} else {
			Float currentIntensity = getRelationData().currentCaIntensity + getRelationData().currentCeIntensity;
			Float dependIntensity = getRelationData().dependCaIntensity + getRelationData().dependCeIntensity;
			if (currentIntensity > dependIntensity) {
				this.moveRelationInfo = new MoveRelationInfo(getRelationData().depend, getRelationData().dependOther);
				this.moveRelationInfo.setTargetComponent(this.getRelationData().getRelation().getCurrent()
						.getComponent());
			} else {
				this.moveRelationInfo = new MoveRelationInfo(getRelationData().current, getRelationData().currentOther);
				this.moveRelationInfo.setTargetComponent(this.getRelationData().getRelation().getDepend()
						.getComponent());
			}
			this.moveRelationInfo.setChangeDir(false);
		}
		return true;
	}

	@Override
	public String getAccording() {
		return "两个组件存在彼此依赖，应该避免强度比较弱的依赖线";
	}

	@Override
	public String getContent() {
		return this.getRelationData().getRelation().getCurrent().getName() + " 与 "
				+ this.getRelationData().getRelation().getDepend().getName() + " 存在彼此依赖，并且前者对后者的依赖较少，请解决该依赖";
	}
}
