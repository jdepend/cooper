package jdepend.util.todolist;

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
import jdepend.util.refactor.RefactorToolFactory;

public abstract class MoveRelationTODOItem extends TODOItem {

	protected Relation relation;

	protected transient Collection<JavaClass> moveClasses;

	protected transient Component targetComponent;

	protected transient boolean isChangeDir;

	private static ThreadLocal<Map<Relation, RelationData>> relationDatas = new ThreadLocal<Map<Relation, RelationData>>();

	public MoveRelationTODOItem(Relation relation) {
		super();
		this.relation = relation;
	}

	@Override
	public StringBuilder execute() throws JDependException {

		if (moveClasses != null && targetComponent != null) {
			// 将依赖的Class移入到被依赖的Class所在的组件中
			RefactorToolFactory.createTool().moveClass(moveClasses, targetComponent);
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
		return isChangeDir;
	}

	@Override
	public StringBuilder getInfo() {
		if (this.moveClasses != null && this.moveClasses.size() > 0) {
			StringBuilder info = new StringBuilder();

			for (JavaClass javaClass : this.moveClasses) {
				info.append(javaClass.getName());
				info.append(" 将从 ");
				info.append(javaClass.getComponent().getName());
				info.append(" 移动到 ");
				info.append(this.targetComponent.getName());
				info.append("\n");
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

		public RelationData(Relation relation) {
			this.current = new VirtualComponent("current");
			this.depend = new VirtualComponent("depend");

			VirtualComponent currentOther = new VirtualComponent("currentOther");
			VirtualComponent dependOther = new VirtualComponent("dependOther");

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
			this.currentCeIntensity = current.calCeCoupling(currentOther);
			this.currentCaIntensity = currentOther.calCeCoupling(current);

			this.dependCeIntensity = depend.calCeCoupling(dependOther);
			this.dependCaIntensity = dependOther.calCeCoupling(depend);
		}
	}
}
