package jdepend.util.todolist;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MathUtil;
import jdepend.model.Relation;

public final class MoveRelationForMutualDependTODOItem extends MoveRelationTODOItem {

	public MoveRelationForMutualDependTODOItem(Relation relation) {
		super(relation);
	}

	protected boolean decision() throws JDependException {
		if (relation.getAttentionType() != Relation.MutualDependAttentionType) {
			throw new JDependException("该关系不是彼此依赖");
		}
		// 根据耦合值判断需要移动Relation
		if (MathUtil.isZero(getCollectData().currentCaIntensity) || MathUtil.isZero(getCollectData().dependCeIntensity)) {
			if (getCollectData().currentCeIntensity > getCollectData().dependCaIntensity) {
				this.moveRelationInfo = new MoveRelationInfo(getCollectData().depend, getCollectData().dependOther);
				this.moveRelationInfo.setTargetComponent(this.relation.getCurrent().getComponent());
			} else {
				this.moveRelationInfo = new MoveRelationInfo(getCollectData().current, getCollectData().currentOther);
				this.moveRelationInfo.setTargetComponent(this.relation.getDepend().getComponent());
			}
			this.moveRelationInfo.setChangeDir(true);
		} else {
			Float currentIntensity = getCollectData().currentCaIntensity + getCollectData().currentCeIntensity;
			Float dependIntensity = getCollectData().dependCaIntensity + getCollectData().dependCeIntensity;
			if (currentIntensity > dependIntensity) {
				this.moveRelationInfo = new MoveRelationInfo(getCollectData().depend, getCollectData().dependOther);
				this.moveRelationInfo.setTargetComponent(this.relation.getCurrent().getComponent());
			} else {
				this.moveRelationInfo = new MoveRelationInfo(getCollectData().current, getCollectData().currentOther);
				this.moveRelationInfo.setTargetComponent(this.relation.getDepend().getComponent());
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
		return relation.getCurrent().getName() + " 与 " + relation.getDepend().getName() + " 存在彼此依赖，并且前者对后者的依赖较少，请解决该依赖";
	}
}
