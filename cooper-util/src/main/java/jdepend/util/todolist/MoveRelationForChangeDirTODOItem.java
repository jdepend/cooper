package jdepend.util.todolist;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MathUtil;
import jdepend.model.Relation;

public final class MoveRelationForChangeDirTODOItem extends MoveRelationTODOItem {

	public MoveRelationForChangeDirTODOItem(Relation relation) {
		super(relation);
	}

	protected boolean decision() throws JDependException {
		if (relation.getAttentionType() != Relation.ComponentLayerAttentionType
				&& relation.getAttentionType() != Relation.SDPAttentionType
				&& relation.getAttentionType() != Relation.CycleDependAttentionType) {
			throw new JDependException("该关系不是下层组件依赖了上层组件或者稳定的组件依赖不稳定的组件或者循环依赖");
		}
		// 根据耦合值判断需要移动Relation
		if (MathUtil.isZero(getCollectData().currentCaIntensity) || MathUtil.isZero(getCollectData().dependCeIntensity)) {
			if (getCollectData().currentCeIntensity > getCollectData().dependCaIntensity) {
				this.moveClasses = getCollectData().depend.getClasses();
				this.targetComponent = this.relation.getCurrent().getComponent();
			} else {
				this.moveClasses = getCollectData().current.getClasses();
				this.targetComponent = this.relation.getDepend().getComponent();
			}
			this.isChangeDir = true;

			return true;
		} else if (MathUtil.isZero(getCollectData().currentCeIntensity)
				&& getCollectData().currentCaIntensity < this.relation.getIntensity()) {
			this.moveClasses = getCollectData().current.getClasses();
			this.targetComponent = this.relation.getDepend().getComponent();
			this.isChangeDir = false;
			return true;
		} else if (MathUtil.isZero(getCollectData().dependCaIntensity)
				&& getCollectData().dependCeIntensity < this.relation.getIntensity()) {
			this.moveClasses = getCollectData().depend.getClasses();
			this.targetComponent = this.relation.getCurrent().getComponent();
			this.isChangeDir = false;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getAccording() {
		return relation.getAttentionType() == Relation.ComponentLayerAttentionType ? "下层组件依赖了上层组件" : (relation
				.getAttentionType() == Relation.SDPAttentionType ? "违反稳定依赖原则" : "该关系在循环依赖链上表现为最弱的关系");
	}

	@Override
	public String getContent() {
		return relation.getCurrent().getName() + " 依赖了 " + relation.getDepend().getName();
	}
}
