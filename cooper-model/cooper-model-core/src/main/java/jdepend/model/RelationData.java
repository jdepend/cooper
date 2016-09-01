package jdepend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.metadata.JavaClassRelationItem;
import jdepend.model.component.VirtualComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.RelationCreator;

/**
 * 用于进一步分析relation的data
 * 
 * @author user
 * 
 */
public class RelationData {

	private Relation relation;

	public float currentCeIntensity;
	public float currentCaIntensity;

	public float dependCeIntensity;
	public float dependCaIntensity;

	public VirtualComponent current;
	public VirtualComponent depend;

	public VirtualComponent currentOther;
	public VirtualComponent dependOther;

	private Collection<Relation> relations;

	public RelationData(Relation relation) {
		this.relation = relation;
	}

	public void init() {

		Component currentComponent = relation.getCurrent().getComponent();
		Component dependComponent = relation.getDepend().getComponent();

		AnalysisResult result = currentComponent.getResult();

		this.current = new VirtualComponent(relation.getCurrent().getName() + "_current", result);
		this.depend = new VirtualComponent(relation.getDepend().getName() + "_depend", result);

		currentOther = new VirtualComponent(relation.getCurrent().getName() + "_currentOther", result);
		dependOther = new VirtualComponent(relation.getDepend().getName() + "_dependOther", result);

		// 计算需要分析的组件
		for (JavaClassRelationItem item : relation.getItems()) {
			current.joinJavaClass(result.getTheClass(item.getSource().getId()));
			depend.joinJavaClass(result.getTheClass(item.getTarget().getId()));
		}

		for (JavaClassUnit javaClass : currentComponent.getClasses()) {
			if (!current.containsClass(javaClass)) {
				currentOther.joinJavaClass(javaClass);
			}
		}

		for (JavaClassUnit javaClass : dependComponent.getClasses()) {
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

		RelationCreator relationCreator = new RelationCreator();
		relations = relationCreator.create(components);

		// 获得关系强度
		this.currentCeIntensity = current.ceCouplingDetail(currentOther).getIntensity();
		this.currentCaIntensity = current.caCouplingDetail(currentOther).getIntensity();

		this.dependCeIntensity = depend.ceCouplingDetail(dependOther).getIntensity();
		this.dependCaIntensity = depend.caCouplingDetail(dependOther).getIntensity();
	}

	/**
	 * 创建本关系与其他组件之间的关系
	 */
	public void appendRelations(List<Component> components) {
		Collection<Component> selfComponents = new ArrayList<Component>();
		selfComponents.add(current);
		selfComponents.add(depend);

		Collection<Component> caComponents = new ArrayList<Component>();
		Collection<Component> ceComponents = new ArrayList<Component>();
		Component otherComponent;
		for (Component component : components) {
			if (!component.equals(relation.getCurrent().getComponent())
					&& !component.equals(relation.getDepend().getComponent())) {
				if (relation.getCurrent().getComponent().getEfferents().contains(component)
						|| relation.getDepend().getComponent().getEfferents().contains(component)) {
					otherComponent = new VirtualComponent(component);
					ceComponents.add(otherComponent);
				} else if (relation.getCurrent().getComponent().getAfferents().contains(component)
						|| relation.getDepend().getComponent().getAfferents().contains(component)) {
					otherComponent = new VirtualComponent(component);
					caComponents.add(otherComponent);
				}
			}
		}

		RelationCreator relationCreator = new RelationCreator();
		relations.addAll(relationCreator.create(selfComponents, ceComponents));
		relations.addAll(relationCreator.create(caComponents, selfComponents));
	}

	public Relation getRelation() {
		return relation;
	}

	public Collection<Relation> getRelations() {
		return relations;
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

	public Component getCurrentOther() {
		return currentOther;
	}

	public Component getDependOther() {
		return dependOther;
	}
}
