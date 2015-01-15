package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.MetricsMgr;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;

public class TODOListIdentify {

	private List<TODOItem> list;

	private AnalysisResult result;

	private List<RelationData> relationDatas;

	private Collection<Component> splitComponents;

	private static final Float MoveJavaClassTODOItemOrder = 10000F;
	private static final Float UniteComponentTODOItemOrder = 1000F;
	private static final Float SplitComponentTODOItemOrder = 100F;
	private static final Float MoveRelationTODOItemOrder = 50F;
	private static final Float AdjustAbstractTODOItemOrder = 10F;

	public List<TODOItem> identify(AnalysisResult result) throws JDependException {
		this.init(result);
		long start = System.currentTimeMillis();
		this.identifyMoveJavaClass();
		LogUtil.getInstance(TODOListIdentify.class).systemLog(
				"identifyMoveJavaClass [" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();
		this.identifyUniteComponent();
		LogUtil.getInstance(TODOListIdentify.class).systemLog(
				"identifyUniteComponent [" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();
		this.identifySplitCompoent();
		LogUtil.getInstance(TODOListIdentify.class).systemLog(
				"identifySplitCompoent [" + (System.currentTimeMillis() - start) + "]");
		start = System.currentTimeMillis();
		this.identifyAdjustAbstract();
		LogUtil.getInstance(TODOListIdentify.class).systemLog(
				"identifyAdjustAbstract [" + (System.currentTimeMillis() - start) + "]");
		// 按Order排序
		Collections.sort(list);
		return list;
	}

	private void init(AnalysisResult result) {
		list = new ArrayList<TODOItem>();
		this.result = result;

		relationDatas = new ArrayList<RelationData>();
		for (Relation relation : this.result.getRelations()) {
			relationDatas.add(new RelationData(relation));
		}

		splitComponents = new HashSet<Component>();
	}

	private RelationData getRelationData(Relation relation) {
		for (RelationData relationData : relationDatas) {
			if (relationData.getRelation().equals(relation)) {
				return relationData;
			}
		}
		return null;
	}

	private void identifyMoveJavaClass() {
		Float attentionLevel = null;
		MoveRelationTODOItem item = null;
		RelationData cycleDepend = null;
		Relation relation;
		for (RelationData relationData : relationDatas) {
			relation = relationData.getRelation();
			try {
				if (relation.isAttention()) {
					if (relation.getAttentionType() == Relation.MutualDependAttentionType) {
						attentionLevel = relation.getAttentionLevel();
						// 寻找循环依赖中“细”的那条依赖关系
						if (attentionLevel - Relation.MutualDependAttentionType > 0.8) {
							item = new MoveRelationForMutualDependTODOItem(relationData);
							if (item.isMove()) {
								relationData.setTodo(true);
								item.setOrder(MoveJavaClassTODOItemOrder + attentionLevel);
								this.list.add(item);
							}
						}
					} else if (relation.getAttentionType() == Relation.CycleDependAttentionType) {
						// 记录循环依赖链上关系最弱的
						if (cycleDepend == null || cycleDepend.getRelation().getIntensity() > relation.getIntensity()) {
							cycleDepend = relationData;
						}

					} else if (relation.getAttentionType() == Relation.ComponentLayerAttentionType) {
						item = new MoveRelationForChangeDirTODOItem(relationData);
						if (item.isMove()) {
							relationData.setTodo(true);
							item.setOrder(MoveJavaClassTODOItemOrder + relation.getAttentionLevel());
							this.list.add(item);
						}
					} else if (relation.getAttentionType() == Relation.SDPAttentionType) {
						attentionLevel = relation.getAttentionLevel();
						if (attentionLevel - Relation.SDPAttentionType > 0.4) {
							item = new MoveRelationForChangeDirTODOItem(relationData);
							if (item.isMove()) {
								relationData.setTodo(true);
								item.setOrder(MoveJavaClassTODOItemOrder + attentionLevel);
								this.list.add(item);
							}
						}
					}
				} else {
					item = new MoveRelationForReduceCouplingTODOItem(relationData);
					if (item.isMove()) {
						relationData.setTodo(true);
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
		if (cycleDepend != null && !cycleDepend.isTodo()) {
			try {
				item = new MoveRelationForChangeDirTODOItem(cycleDepend);
				if (item.isMove()) {
					cycleDepend.setTodo(true);
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
		Float attentionLevel = null;
		TODOItem item = null;
		Relation relation;
		for (RelationData relationData : relationDatas) {
			if (!relationData.isTodo()) {
				relation = relationData.getRelation();
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
							relationData.setTodo(true);
							this.list.add(item);
						}
					}
				} else {
					// 耦合值大于内聚值和值100
					if (relation.getBalance() < -100) {
						item = new ComponentUniteTODOItem(relation);
						item.setContent("合并组件[" + relation.getCurrent().getName() + "]和["
								+ relation.getDepend().getName() + "]");
						item.setAccording("两个组件的耦合值远大于内聚值");
						item.setOrder(UniteComponentTODOItemOrder - relation.getBalance());
						relationData.setTodo(true);
						this.list.add(item);
					}
				}
			}
		}
	}

	/**
	 * 根据蝶形对象识别需要合并的组件
	 */
	private void identifyUniteComponentWithButterflyObject() {
		List<Component> components = new ArrayList<Component>(result.getComponents());

		Collections.sort(components, new JDependUnitByMetricsComparator(MetricsMgr.Ca, false));
		int Ca;
		int Ce;
		TODOItem item = null;
		Relation relation;
		L: for (Component component : components) {
			Ca = component.getAfferentCoupling();
			Ce = component.getEfferentCoupling();
			if (Ce == 1 && Ca > Ce) {
				relation = result.getTheRelation(component.getName(), component.getEfferents().iterator().next()
						.getName());
				RelationData relationData = this.getRelationData(relation);
				if (!relationData.isTodo()) {
					item = new ComponentUniteTODOItem(relation);
					item.setContent("合并组件[" + relation.getCurrent().getName() + "]和[" + relation.getDepend().getName()
							+ "]");
					item.setAccording("组件[" + relation.getCurrent().getName() + "]作为蝶形对象不应该在依赖其他组件");
					item.setOrder(UniteComponentTODOItemOrder);
					relationData.setTodo(true);
					this.list.add(item);
					break L;
				}
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
		for (Component component : result.getComponents()) {
			if (!this.splitComponents.contains(component) && component.getBalance() < 0.2F) {
				item = new SplitCompoentTODOItem(component);
				if (item.isSplit()) {
					item.setContent("组件[" + component.getName() + "]内聚性差，需要拆分");
					item.setAccording("组件内的Class应该关系更紧密，而与其他组件中的Class关系疏远");
					item.setOrder(SplitComponentTODOItemOrder - component.getBalance());

					this.splitComponents.add(component);
					this.list.add(item);
				}
			}
		}
	}

	/**
	 * 根据易分对象识别需要拆分的组件
	 */
	private void identifySplitCompoentWithFissileObject() {

		List<Component> components = new ArrayList<Component>(result.getComponents());
		Collections.sort(components, new JDependUnitByMetricsComparator(MetricsMgr.Ce, false));
		int Ca;
		int Ce;
		SplitCompoentTODOItem item = null;
		L: for (Component component : components) {
			if (!this.splitComponents.contains(component)) {
				Ca = component.getAfferentCoupling();
				Ce = component.getEfferentCoupling();
				if (Ca == 1 && Ca < Ce) {
					item = new SplitCompoentTODOItem(component);
					if (item.isSplit()) {
						item.setContent("组件[" + component.getName() + "]作为易分对象又被其他组件依赖，需要拆分");
						item.setAccording("作为易分对象又被其他组件依赖，需要拆分");
						item.setOrder(SplitComponentTODOItemOrder - component.getBalance());

						this.splitComponents.add(component);
						this.list.add(item);
						break L;
					}
				}
			}
		}
	}

	private void identifyAdjustAbstract() {
		TODOItem item = null;
		for (Component component : result.getComponents()) {
			if (component.getDistance() > 0.8F) {
				item = new AdjustAbstractTODOItem(component);
				if (component.getStability() < 0.5) {
					item.setContent("组件[" + component.getName() + "]的抽象程度不够");
				} else {
					item.setContent("组件[" + component.getName() + "]的抽象程度过大");
				}

				item.setAccording("稳定抽象等价原则");
				item.setOrder(AdjustAbstractTODOItemOrder + component.getDistance());

				this.list.add(item);
			}
		}
	}
}
