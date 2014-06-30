package jdepend.util.todolist;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MathUtil;
import jdepend.model.Relation;

public final class MoveRelationForReduceCouplingTODOItem extends MoveRelationTODOItem {

	public MoveRelationForReduceCouplingTODOItem(Relation relation) {
		super(relation);
	}

	protected boolean decision() throws JDependException {
		if (this.relation.isAttention()) {
			throw new JDependException("该关系值得关注，不应进入解决高耦合低内聚TODOItem");
		}
		// 根据耦合值判断是否需要移动Relation
		if (!MathUtil.isZero(getCollectData().currentCeIntensity)
				&& !MathUtil.isZero(getCollectData().currentCaIntensity)
				&& !MathUtil.isZero(getCollectData().dependCeIntensity)
				&& !MathUtil.isZero(getCollectData().dependCaIntensity)) {
			return false;
		} else {

			// 计算current偶合值
			float currentIntensity;
			boolean isCurrentChangeDir;
			if (!MathUtil.isZero(getCollectData().currentCeIntensity)
					&& MathUtil.isZero(getCollectData().currentCaIntensity)) {
				currentIntensity = getCollectData().currentCeIntensity;
				isCurrentChangeDir = true;
			} else if (MathUtil.isZero(getCollectData().currentCeIntensity)
					&& !MathUtil.isZero(getCollectData().currentCaIntensity)) {
				currentIntensity = getCollectData().currentCaIntensity;
				isCurrentChangeDir = false;
			} else {
				currentIntensity = Float.MAX_VALUE;
				isCurrentChangeDir = false;
			}
			// 计算depend偶合值
			float dependIntensity;
			boolean isDependChangeDir;
			if (!MathUtil.isZero(getCollectData().dependCeIntensity)
					&& MathUtil.isZero(getCollectData().dependCaIntensity)) {
				dependIntensity = getCollectData().dependCeIntensity;
				isDependChangeDir = false;
			} else if (MathUtil.isZero(getCollectData().dependCeIntensity)
					&& !MathUtil.isZero(getCollectData().dependCaIntensity)) {
				dependIntensity = getCollectData().dependCaIntensity;
				isDependChangeDir = true;
			} else {
				dependIntensity = Float.MAX_VALUE;
				isDependChangeDir = false;
			}
			// 计算移动的Class和目标组件
			float relationIntensity = this.relation.getIntensity();
			if (currentIntensity >= relationIntensity && dependIntensity >= relationIntensity) {
				return false;
			} else {
				if (currentIntensity > dependIntensity) {
					this.moveClasses = getCollectData().depend.getClasses();
					this.targetComponent = this.relation.getCurrent().getComponent();
					this.isChangeDir = isDependChangeDir;
				} else {
					this.moveClasses = getCollectData().current.getClasses();
					this.targetComponent = this.relation.getDepend().getComponent();
					this.isChangeDir = isCurrentChangeDir;
				}
				return true;
			}
		}
	}

	@Override
	public String getAccording() {
		return "违反高内聚低耦合原则";
	}

	@Override
	public String getContent() {
		return relation.getCurrent().getName() + " 依赖了 " + relation.getDepend().getName() + " 该移动"
				+ (this.isChangeDir() ? "" : "不") + "会改变依赖方向";
	}
}
