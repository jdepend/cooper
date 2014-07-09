package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Relation;
import jdepend.model.component.VirtualComponent;
import jdepend.model.util.RelationCreator;
import jdepend.util.refactor.RefactorToolFactory;

public abstract class MoveRelationTODOItem extends TODOItem {

	protected Relation relation;

	protected transient MoveRelationInfo moveRelationInfo;

	private static ThreadLocal<Map<Relation, RelationData>> relationDatas = new ThreadLocal<Map<Relation, RelationData>>();

	public MoveRelationTODOItem(Relation relation) {
		super();
		this.relation = relation;
	}

	@Override
	public StringBuilder execute() throws JDependException {

		if (moveRelationInfo.getMoveClasses() != null && moveRelationInfo.getTargetComponent() != null) {
			// 将依赖的Class移入到被依赖的Class所在的组件中
			RefactorToolFactory.createTool().moveClass(moveRelationInfo.getMoveClasses(),
					moveRelationInfo.getTargetComponent());
			// 记录日志
			BusiLogUtil.getInstance().businessLog(Operation.moveToClass);
		}
		return null;
	}

	private void collect() {
		Map<Relation, RelationData> datas = relationDatas.get();
		if (datas == null) {
			datas = new HashMap<Relation, RelationData>();
			relationDatas.set(datas);
		}
		if (datas.get(this.relation) == null) {
			datas.put(relation, new RelationData(relation));
		}
	}

	protected RelationData getCollectData() {
		return relationDatas.get().get(this.relation);
	}

	public final boolean isMove() throws JDependException {
		this.collect();
		return this.decision();
	}

	protected abstract boolean decision() throws JDependException;

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public boolean isChangeDir() {
		return moveRelationInfo.isChangeDir();
	}

	@Override
	public StringBuilder getInfo() {
		if (moveRelationInfo.getMoveClasses() != null && moveRelationInfo.getMoveClasses().size() > 0) {
			StringBuilder info = new StringBuilder();

			for (JavaClass javaClass : moveRelationInfo.getMoveClasses()) {
				info.append(javaClass.getName());
				info.append(" 将从 ");
				info.append(javaClass.getComponent().getName());
				info.append(" 移动到 ");
				info.append(moveRelationInfo.getTargetComponent().getName());
				info.append("\n");
			}

			Collection<JavaClassRelationItem> items = this.moveRelationInfo.getFromClassRelations();
			if (!items.isEmpty()) {
				info.append("这些类与源组件的关系明细为：\n");
				for (JavaClassRelationItem item : items) {
					info.append(item);
					info.append("\n");
				}
			}
			return info;
		} else {
			return null;
		}
	}

	class RelationData {

		float currentCeIntensity;
		float currentCaIntensity;

		float dependCeIntensity;
		float dependCaIntensity;

		Component current;
		Component depend;

		VirtualComponent currentOther;
		VirtualComponent dependOther;

		public RelationData(Relation relation) {
			this.current = new VirtualComponent("current");
			this.depend = new VirtualComponent("depend");

			currentOther = new VirtualComponent("currentOther");
			dependOther = new VirtualComponent("dependOther");

			Component currentComponent = relation.getCurrent().getComponent();
			Component dependComponent = relation.getDepend().getComponent();
			// 计算需要分析的组件
			for (JavaClassRelationItem item : relation.getItems()) {
				current.addJavaClass(item.getCurrent());
				depend.addJavaClass(item.getDepend());
			}

			for (JavaClass javaClass : currentComponent.getClasses()) {
				if (!current.containsClass(javaClass)) {
					currentOther.addJavaClass(javaClass);
				}
			}

			for (JavaClass javaClass : dependComponent.getClasses()) {
				if (!depend.containsClass(javaClass)) {
					dependOther.addJavaClass(javaClass);
				}
			}
			// 计算组件间的耦合值
			Collection<Component> components = new ArrayList<Component>();
			components.add(current);
			components.add(depend);
			components.add(currentOther);
			components.add(dependOther);
			
			new RelationCreator().create(components);
			
			this.currentCeIntensity = current.ceCoupling(currentOther);
			this.currentCaIntensity = current.caCoupling(currentOther);

			this.dependCeIntensity = depend.ceCoupling(dependOther);
			this.dependCaIntensity = depend.caCoupling(dependOther);
		}
	}

	class MoveRelationInfo {

		private Component targetComponent;

		private boolean isChangeDir;

		private Component from;

		private Component fromOther;

		public MoveRelationInfo(Component from, Component fromOther) {
			this.from = from;
			this.fromOther = fromOther;
		}

		public Component getTargetComponent() {
			return targetComponent;
		}

		public void setTargetComponent(Component targetComponent) {
			this.targetComponent = targetComponent;
		}

		public boolean isChangeDir() {
			return isChangeDir;
		}

		public void setChangeDir(boolean isChangeDir) {
			this.isChangeDir = isChangeDir;
		}

		public Collection<JavaClass> getMoveClasses() {
			return this.from.getClasses();
		}

		public Collection<JavaClassRelationItem> getFromClassRelations() {
			Collection<JavaClassRelationItem> items = new ArrayList<JavaClassRelationItem>();

			for (JavaClassRelationItem item : this.from.caCouplingDetail(this.fromOther)) {
				items.add(item);
			}
			for (JavaClassRelationItem item : this.from.ceCouplingDetail(this.fromOther)) {
				items.add(item);
			}
			return items;
		}
	}
}
