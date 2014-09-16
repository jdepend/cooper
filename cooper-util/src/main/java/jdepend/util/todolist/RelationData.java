package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Relation;
import jdepend.model.component.VirtualComponent;
import jdepend.model.util.RelationCreator;

public class RelationData {

	private Relation relation;

	float currentCeIntensity;
	float currentCaIntensity;

	float dependCeIntensity;
	float dependCaIntensity;

	Component current;
	Component depend;

	VirtualComponent currentOther;
	VirtualComponent dependOther;

	private boolean inited = false;

	public RelationData(Relation relation) {
		this.relation = relation;
	}

	public void init() {
		if (!inited) {
			this.current = new VirtualComponent("current");
			this.depend = new VirtualComponent("depend");

			currentOther = new VirtualComponent("currentOther");
			dependOther = new VirtualComponent("dependOther");

			Component currentComponent = relation.getCurrent().getComponent();
			Component dependComponent = relation.getDepend().getComponent();
			// 计算需要分析的组件
			for (JavaClassRelationItem item : relation.getItems()) {
				current.joinJavaClass(item.getCurrent());
				depend.joinJavaClass(item.getDepend());
			}

			for (JavaClass javaClass : currentComponent.getClasses()) {
				if (!current.containsClass(javaClass)) {
					currentOther.joinJavaClass(javaClass);
				}
			}

			for (JavaClass javaClass : dependComponent.getClasses()) {
				if (!depend.containsClass(javaClass)) {
					dependOther.joinJavaClass(javaClass);
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

			this.inited = true;
		}
	}

	public Relation getRelation() {
		return relation;
	}

	public float getCurrentCeIntensity() {
		return currentCeIntensity;
	}

	public float getCurrentCaIntensity() {
		return currentCaIntensity;
	}

	public float getDependCeIntensity() {
		return dependCeIntensity;
	}

	public float getDependCaIntensity() {
		return dependCaIntensity;
	}

	public Component getCurrent() {
		return current;
	}

	public Component getDepend() {
		return depend;
	}

	public VirtualComponent getCurrentOther() {
		return currentOther;
	}

	public VirtualComponent getDependOther() {
		return dependOther;
	}
}
