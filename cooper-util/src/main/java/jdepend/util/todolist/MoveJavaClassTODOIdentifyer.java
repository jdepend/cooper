package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.model.Relation;

public class MoveJavaClassTODOIdentifyer implements TODOIdentifyer {

	private static final Float MoveJavaClassTODOItemOrder = 10000F;
	private static final Float MoveRelationTODOItemOrder = 50F;

	@Override
	public List<TODOItem> identify(TODOIdentifyInfo info) throws JDependException {

		List<TODOItem> list = new ArrayList<TODOItem>();

		Float attentionLevel = null;
		MoveRelationTODOItem item = null;
		TODORelationData cycleDepend = null;
		Relation relation;
		for (TODORelationData relationData : info.getRelationDatas()) {
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
								list.add(item);
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
							list.add(item);
						}
					} else if (relation.getAttentionType() == Relation.SDPAttentionType) {
						attentionLevel = relation.getAttentionLevel();
						if (attentionLevel - Relation.SDPAttentionType > 0.4) {
							item = new MoveRelationForChangeDirTODOItem(relationData);
							if (item.isMove()) {
								relationData.setTodo(true);
								item.setOrder(MoveJavaClassTODOItemOrder + attentionLevel);
								list.add(item);
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
						list.add(item);
					}
				}
			} catch (JDependException e) {
				LogUtil.getInstance(TODOListIdentifyerFacade.class).systemError(e.getMessage());
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
					list.add(item);
				}
			} catch (JDependException e) {
				LogUtil.getInstance(TODOListIdentifyerFacade.class).systemError(e.getMessage());
				e.printStackTrace();
			}
		}

		return list;
	}

}
