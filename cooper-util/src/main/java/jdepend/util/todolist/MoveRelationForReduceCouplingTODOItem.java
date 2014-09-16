package jdepend.util.todolist;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.MathUtil;
import jdepend.model.Relation;

public final class MoveRelationForReduceCouplingTODOItem extends MoveRelationTODOItem {

	private boolean deleteRelation = false;

	public MoveRelationForReduceCouplingTODOItem(RelationData relationData) {
		super(relationData);
	}

	protected boolean decision() throws JDependException {
		if (this.getRelationData().getRelation().isAttention()) {
			throw new JDependException("该关系值得关注，不应进入解决高耦合低内聚TODOItem");
		}
		// 根据耦合值判断是否需要移动Relation
		if (!MathUtil.isZero(getRelationData().currentCeIntensity)
				&& !MathUtil.isZero(getRelationData().currentCaIntensity)
				&& !MathUtil.isZero(getRelationData().dependCeIntensity)
				&& !MathUtil.isZero(getRelationData().dependCaIntensity)) {
			return false;
		} else {
			// 计算current偶合值
			float currentIntensity;
			boolean isCurrentChangeDir;
			if (MathUtil.isZero(getRelationData().currentCeIntensity)
					&& MathUtil.isZero(getRelationData().currentCaIntensity)) {
				this.moveRelationInfo = new MoveRelationInfo(getRelationData().current, getRelationData().currentOther);
				this.moveRelationInfo.setTargetComponent(this.getRelationData().getRelation().getDepend()
						.getComponent());
				this.deleteRelation = true;
				return true;
			} else if (!MathUtil.isZero(getRelationData().currentCeIntensity)
					&& MathUtil.isZero(getRelationData().currentCaIntensity)) {
				currentIntensity = getRelationData().currentCeIntensity;
				isCurrentChangeDir = true;
			} else if (MathUtil.isZero(getRelationData().currentCeIntensity)
					&& !MathUtil.isZero(getRelationData().currentCaIntensity)) {
				currentIntensity = getRelationData().currentCaIntensity;
				isCurrentChangeDir = false;
			} else {
				currentIntensity = Float.MAX_VALUE;
				isCurrentChangeDir = false;
			}
			// 计算depend偶合值
			float dependIntensity;
			boolean isDependChangeDir;
			if (MathUtil.isZero(getRelationData().currentCeIntensity)
					&& MathUtil.isZero(getRelationData().currentCaIntensity)) {
				this.moveRelationInfo = new MoveRelationInfo(getRelationData().depend, getRelationData().dependOther);
				this.moveRelationInfo.setTargetComponent(this.getRelationData().getRelation().getCurrent()
						.getComponent());
				this.deleteRelation = true;
				return true;
			} else if (!MathUtil.isZero(getRelationData().dependCeIntensity)
					&& MathUtil.isZero(getRelationData().dependCaIntensity)) {
				dependIntensity = getRelationData().dependCeIntensity;
				isDependChangeDir = false;
			} else if (MathUtil.isZero(getRelationData().dependCeIntensity)
					&& !MathUtil.isZero(getRelationData().dependCaIntensity)) {
				dependIntensity = getRelationData().dependCaIntensity;
				isDependChangeDir = true;
			} else {
				dependIntensity = Float.MAX_VALUE;
				isDependChangeDir = false;
			}
			// 计算移动的Class和目标组件
			float relationIntensity = this.getRelationData().getRelation().getIntensity();
			if (currentIntensity >= relationIntensity && dependIntensity >= relationIntensity) {
				return false;
			} else {
				if (currentIntensity > dependIntensity) {
					this.moveRelationInfo = new MoveRelationInfo(getRelationData().depend,
							getRelationData().dependOther);
					this.moveRelationInfo.setTargetComponent(this.getRelationData().getRelation().getCurrent()
							.getComponent());
					this.moveRelationInfo.setChangeDir(isDependChangeDir);
				} else {
					this.moveRelationInfo = new MoveRelationInfo(getRelationData().current,
							getRelationData().currentOther);
					this.moveRelationInfo.setTargetComponent(this.getRelationData().getRelation().getDepend()
							.getComponent());
					this.moveRelationInfo.setChangeDir(isCurrentChangeDir);
				}
				if (this.moveRelationInfo.isChangeDir()) {
					if (this.getRelationData().getRelation().getDepend().getComponent()
							.stability(this.getRelationData().getRelation().getCurrent().getComponent())) {
						return false;
					}
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
		StringBuilder info = new StringBuilder();
		info.append(this.getRelationData().getRelation().getCurrent().getName());
		info.append(" 依赖了 ");
		info.append(this.getRelationData().getRelation().getDepend().getName());
		info.append(" 该移动");
		if (this.deleteRelation) {
			info.append("会删除该关系");
		} else {
			info.append((this.isChangeDir() ? "" : "不") + "会改变依赖方向");
		}
		return info.toString();
	}
}
