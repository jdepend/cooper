package jdepend.util.todolist;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MathUtil;
import jdepend.model.Relation;

public final class MoveRelationForChangeDirTODOItem extends MoveRelationTODOItem {

	public MoveRelationForChangeDirTODOItem(TODORelationData relationData) {
		super(relationData);
	}

	protected boolean decision() throws JDependException {
		if (this.getRelationData().getRelation().getAttentionType() != Relation.ComponentLayerAttentionType
				&& this.getRelationData().getRelation().getAttentionType() != Relation.SDPAttentionType
				&& this.getRelationData().getRelation().getAttentionType() != Relation.CycleDependAttentionType) {
			throw new JDependException("该关系不是下层组件依赖了上层组件或者稳定的组件依赖不稳定的组件或者循环依赖");
		}
		// 根据耦合值判断需要移动Relation
		if (MathUtil.isZero(getRelationData().currentCaIntensity)
				|| MathUtil.isZero(getRelationData().dependCeIntensity)) {
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

			return true;
		} else if (MathUtil.isZero(getRelationData().currentCeIntensity)
				&& getRelationData().currentCaIntensity < this.getRelationData().getRelation().getIntensity()) {
			this.moveRelationInfo = new MoveRelationInfo(getRelationData().current, getRelationData().currentOther);
			this.moveRelationInfo.setTargetComponent(this.getRelationData().getRelation().getDepend().getComponent());
			this.moveRelationInfo.setChangeDir(false);
			return true;
		} else if (MathUtil.isZero(getRelationData().dependCaIntensity)
				&& getRelationData().dependCeIntensity < this.getRelationData().getRelation().getIntensity()) {
			this.moveRelationInfo = new MoveRelationInfo(getRelationData().depend, getRelationData().dependOther);
			this.moveRelationInfo.setTargetComponent(this.getRelationData().getRelation().getCurrent().getComponent());
			this.moveRelationInfo.setChangeDir(false);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getAccording() {
		return this.getRelationData().getRelation().getAttentionType() == Relation.ComponentLayerAttentionType ? "下层组件依赖了上层组件"
				: (this.getRelationData().getRelation().getAttentionType() == Relation.SDPAttentionType ? "违反稳定依赖原则"
						: "该关系在循环依赖链上表现为最弱的关系");
	}

	@Override
	public String getContent() {
		return this.getRelationData().getRelation().getCurrent().getName() + " 依赖了 "
				+ this.getRelationData().getRelation().getDepend().getName();
	}
}
