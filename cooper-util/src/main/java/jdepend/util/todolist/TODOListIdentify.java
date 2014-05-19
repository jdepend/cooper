package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.model.JDependUnit;
import jdepend.model.MetricsMgr;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;

public class TODOListIdentify {

	private transient List<TODOItem> list;

	private transient AnalysisResult result;

	private static final Float MoveJavaClassTODOItemOrder = 10000F;
	private static final Float UniteComponentTODOItemOrder = 1000F;
	private static final Float SplitComponentTODOItemOrder = 100F;
	private static final Float MoveRelationTODOItemOrder = 50F;
	private static final Float AdjustAbstractTODOItemOrder = 10F;

	public List<TODOItem> identify(AnalysisResult result) throws JDependException {
		list = new ArrayList<TODOItem>();
		this.result = result;
		if (this.result != null) {
			this.identifyMoveJavaClass();
			this.identifyUniteComponent();
			this.identifySplitCompoent();
			this.identifyAdjustAbstract();
		}
		// 按Order排序
		Collections.sort(list);
		return list;
	}

	private void identifyMoveJavaClass() {
		Collection<Relation> relations = this.result.getRelations();
		Float attentionLevel = null;
		MoveRelationTODOItem item = null;
		Relation cycleDepend = null;
		for (Relation relation : relations) {
			try {
				if (relation.isAttention()) {
					if (relation.getAttentionType() == Relation.MutualDependAttentionType) {
						attentionLevel = relation.getAttentionLevel();
						// 寻找循环依赖中“细”的那条依赖关系
						if (attentionLevel - Relation.MutualDependAttentionType > 0.8) {
							item = new MoveRelationForMutualDependTODOItem(relation);
							if (item.isMove()) {
								item.setOrder(MoveJavaClassTODOItemOrder + attentionLevel);
								this.list.add(item);
							}
						}
					} else if (relation.getAttentionType() == Relation.CycleDependAttentionType) {
						// 记录循环依赖链上关系最弱的
						if (cycleDepend == null || cycleDepend.getIntensity() > relation.getIntensity()) {
							cycleDepend = relation;
						}

					} else if (relation.getAttentionType() == Relation.ComponentLayerAttentionType) {
						item = new MoveRelationForChangeDirTODOItem(relation);
						if (item.isMove()) {
							item.setOrder(MoveJavaClassTODOItemOrder + relation.getAttentionLevel());
							this.list.add(item);
						}
					} else if (relation.getAttentionType() == Relation.SDPAttentionType) {
						attentionLevel = relation.getAttentionLevel();
						if (attentionLevel - Relation.SDPAttentionType > 0.4) {
							item = new MoveRelationForChangeDirTODOItem(relation);
							if (item.isMove()) {
								item.setOrder(MoveJavaClassTODOItemOrder + attentionLevel);
								this.list.add(item);
							}
						}
					}
				} else {
					item = new MoveRelationForReduceCouplingTODOItem(relation);
					if (item.isMove()) {
						Float order = MoveRelationTODOItemOrder;
						if (!item.isChangeDir()) {
							order = order + 10F;
						}
						item.setOrder(order);
						this.list.add(item);
					}
				}
			} catch (JDependException e) {
				LogUtil.getInstance(TODOListIdentify.class).systemError(e.getMessage());
				e.printStackTrace();

			}
		}
		// 处理循环依赖链
		if (cycleDepend != null) {
			try {
				item = new MoveRelationForChangeDirTODOItem(cycleDepend);
				if (item.isMove()) {
					item.setOrder(MoveJavaClassTODOItemOrder);
					this.list.add(item);
				}
			} catch (JDependException e) {
				LogUtil.getInstance(TODOListIdentify.class).systemError(e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private void identifyUniteComponent() {
		this.identifyUniteComponentWithRelation();
		this.identifyUniteComponentWithButterflyObject();
	}

	/**
	 * 根据关系强度识别需要合并的组件
	 */
	private void identifyUniteComponentWithRelation() {
		Collection<Relation> relations = this.result.getRelations();
		Float attentionLevel = null;
		TODOItem item = null;
		for (Relation relation : relations) {
			if (relation.isAttention()) {
				if (relation.getAttentionType() == Relation.MutualDependAttentionType) {
					attentionLevel = relation.getAttentionLevel() - Relation.MutualDependAttentionType;
					// 循环依赖的双方依赖线不是一“粗”一“细”
					if (attentionLevel < 0.8 && attentionLevel >= 0.2) {
						item = new ComponentUniteTODOItem(relation);
						item.setContent("合并组件[" + relation.getCurrent().getName() + "]和["
								+ relation.getDepend().getName() + "]");
						StringBuilder according = new StringBuilder("两个组件存在彼此依赖");
						int orderOffset = 0;
						// 依赖强度相似
						if (attentionLevel < 0.6 && attentionLevel > 0.4) {
							according.append("，依赖强度相似");
							orderOffset += 10;
						}
						// 耦合值大于内聚值
						if (relation.getBalance() < 0) {
							according.append("，并且耦合值大于内聚值");
							orderOffset -= relation.getBalance();
						}
						item.setAccording(according.toString());
						item.setOrder(UniteComponentTODOItemOrder + orderOffset);
						this.list.add(item);
					}
				}
			} else {
				// 耦合值大于内聚值和值100
				if (relation.getBalance() < -100) {
					item = new ComponentUniteTODOItem(relation);
					item.setContent("合并组件[" + relation.getCurrent().getName() + "]和[" + relation.getDepend().getName()
							+ "]");
					item.setAccording("两个组件的耦合值远大于内聚值");
					item.setOrder(UniteComponentTODOItemOrder - relation.getBalance());
					this.list.add(item);
				}
			}
		}
	}

	/**
	 * 根据蝶形对象识别需要合并的组件
	 */
	private void identifyUniteComponentWithButterflyObject() {
		List<JDependUnit> units = new ArrayList<JDependUnit>(result.getComponents());

		Collections.sort(units, new JDependUnitByMetricsComparator(MetricsMgr.Ca, false));
		int Ca;
		int Ce;
		TODOItem item = null;
		Relation relation;
		for (JDependUnit unit : units) {
			Ca = unit.getAfferentCoupling();
			Ce = unit.getEfferentCoupling();
			if (Ce > 0 && Ce < 2 && Ca > Ce) {
				for (JDependUnit ceUnit : unit.getEfferents()) {
					relation = result.getTheRelation(unit.getName(), ceUnit.getName());
					item = new ComponentUniteTODOItem(relation);
					item.setContent("合并组件[" + relation.getCurrent().getName() + "]和[" + relation.getDepend().getName()
							+ "]");
					item.setAccording("组件[" + relation.getCurrent().getName() + "]作为蝶形对象不应该在依赖其他组件");
					item.setOrder(UniteComponentTODOItemOrder);
					this.list.add(item);
				}
				break;
			}
		}
	}

	private void identifySplitCompoent() {
		this.identifySplitCompoentWithBalance();
		this.identifySplitCompoentWithFissileObject();
	}

	/**
	 * 根据内聚性指数拆分
	 */
	private void identifySplitCompoentWithBalance() {
		SplitCompoentTODOItem item = null;
		for (JDependUnit unit : result.getComponents()) {
			if (unit.getBalance() < 0.2F) {
				item = new SplitCompoentTODOItem(unit);
				if (item.isSplit()) {
					item.setContent("组件[" + unit.getName() + "]内聚性差，需要拆分");
					item.setAccording("组件内的Class应该关系更紧密，而与其他组件中的Class关系疏远");
					item.setOrder(SplitComponentTODOItemOrder - unit.getBalance());

					this.list.add(item);
				}
			}
		}
	}

	/**
	 * 根据易分对象识别需要拆分的组件
	 */
	private void identifySplitCompoentWithFissileObject() {

		List<JDependUnit> units = new ArrayList<JDependUnit>(result.getComponents());
		Collections.sort(units, new JDependUnitByMetricsComparator(MetricsMgr.Ce, false));
		int Ca;
		int Ce;
		SplitCompoentTODOItem item = null;
		for (JDependUnit unit : units) {
			Ca = unit.getAfferentCoupling();
			Ce = unit.getEfferentCoupling();
			if (Ca > 0 && Ca < 2 && Ca < Ce) {
				item = new SplitCompoentTODOItem(unit);
				if (item.isSplit()) {
					item.setContent("组件[" + unit.getName() + "]作为易分对象又被其他组件依赖，需要拆分");
					item.setAccording("作为易分对象又被其他组件依赖，需要拆分");
					item.setOrder(SplitComponentTODOItemOrder - unit.getBalance());

					this.list.add(item);
					break;
				}
			}
		}
	}

	private void identifyAdjustAbstract() {
		TODOItem item = null;
		for (JDependUnit unit : result.getComponents()) {
			if (unit.getDistance() > 0.8F) {
				item = new AdjustAbstractTODOItem(unit);
				if (unit.getStability() < 0.5) {
					item.setContent("组件[" + unit.getName() + "]的抽象程度不够");
				} else {
					item.setContent("组件[" + unit.getName() + "]的抽象程度过大");
				}

				item.setAccording("稳定抽象等价原则");
				item.setOrder(AdjustAbstractTODOItemOrder + unit.getDistance());

				this.list.add(item);
			}
		}
	}
}
