package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.Relation;
import jdepend.util.refactor.RefactorToolFactory;

public final class ComponentUniteTODOItem extends TODOItem {

	private Relation relation;

	public ComponentUniteTODOItem(Relation relation) {
		super();
		this.relation = relation;
	}

	@Override
	public List<Object> execute() throws JDependException {

		List<String> components = new ArrayList<String>();
		components.add(relation.getCurrent().getName());
		components.add(relation.getDepend().getName());

		String newComponent = relation.getCurrent().getName() + "|" + relation.getDepend().getName();

		int clayer = relation.getCurrent().getComponent().getLayer();
		int dlayer = relation.getDepend().getComponent().getLayer();
		int layer = Math.min(clayer, dlayer);
		RefactorToolFactory.createTool().uniteComponent(newComponent, layer, components);
		// 记录日志
		BusiLogUtil.getInstance().businessLog(Operation.uniteComponent);

		return null;
	}

	public Relation getRelation() {
		return relation;
	}

	@Override
	public List<Object> getInfo() {
		StringBuilder info = new StringBuilder();

		info.append("组件[");
		info.append(this.relation.getCurrent().getName());
		info.append("]将与组件[");
		info.append(this.relation.getDepend().getName());
		info.append("]合并，其关系耦合值为：");
		info.append(MetricsFormat.toFormattedMetrics(this.relation.getIntensity()));
		info.append("\n");

		List<Object> infos = new ArrayList<Object>();
		infos.add(info);
		return infos;
	}

}
