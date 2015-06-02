package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdepend.model.Component;
import jdepend.model.MetricsMgr;
import jdepend.model.Relation;
import jdepend.model.util.JDependUnitByMetricsComparator;

public class UniteComponentTODOIdentifyer implements TODOIdentifyer {

	private List<TODOItem> list;

	private static final Float UniteComponentTODOItemOrder = 1000F;

	@Override
	public List<TODOItem> identify(TODOIdentifyInfo info) throws TODOListException {
		list = new ArrayList<TODOItem>();

		this.identifyUniteComponentWithRelation(info);
		this.identifyUniteComponentWithButterflyObject(info);

		return list;

	}

	/**
	 * 根据关系强度识别需要合并的组件
	 */
	private void identifyUniteComponentWithRelation(TODOIdentifyInfo info) {
		Float attentionLevel = null;
		TODOItem item = null;
		Relation relation;
		for (TODORelationData relationData : info.getRelationDatas()) {
			if (!relationData.isTodo()) {
				relation = relationData.getRelation();
				if (relation.isAttention()) {
					if (relation.getAttentionType() == Relation.MutualDependAttentionType) {
						attentionLevel = relation.getAttentionLevel() - Relation.MutualDependAttentionType;
						// 循环依赖的双方依赖线不是一“粗”一“细”
						if (attentionLevel < 0.8 && attentionLevel >= 0.2) {
							item = new UniteComponentTODOItem(relation);
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
						item = new UniteComponentTODOItem(relation);
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
	private void identifyUniteComponentWithButterflyObject(TODOIdentifyInfo info) {
		List<Component> components = info.getResult().getComponents();

		Collections.sort(components, new JDependUnitByMetricsComparator(MetricsMgr.Ca, false));
		int Ca;
		int Ce;
		TODOItem item = null;
		Relation relation;
		L: for (Component component : components) {
			Ca = component.getAfferentCoupling();
			Ce = component.getEfferentCoupling();
			if (Ce == 1 && Ca > Ce) {
				relation = component.getCeTheRelation(component.getEfferents().iterator().next());
				TODORelationData relationData = info.getRelationData(relation);
				if (!relationData.isTodo()) {
					item = new UniteComponentTODOItem(relation);
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

}
