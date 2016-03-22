package jdepend.knowledge.architectpattern.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.ComponentException;
import jdepend.model.Relation;

/**
 * 去除了干扰关系的组件
 * 
 * @author user
 * 
 */
public class IdealComponent extends Component {

	private Component component;

	public IdealComponent(Component component) {
		super(component.getName());
		this.component = component;
	}
	
	public Component getOriginalComponent() {
		return component;
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws ComponentException {
		throw new ComponentException("理想组件不进行组件的识别");
	}

	@Override
	public synchronized Collection<Component> getAfferents() {

		if (this.afferents == null) {
			this.afferents = new HashSet<Component>();
			for (Relation relation : component.getRelations()) {
				if (relation.isNormality() && relation.getDepend().getComponent().equals(component)) {
					this.afferents.add(relation.getCurrent().getComponent());
				}
			}
		}
		return this.afferents;
	}

	@Override
	public synchronized Collection<Component> getEfferents() {

		if (this.efferents == null) {
			this.efferents = new HashSet<Component>();
			for (Relation relation : component.getRelations()) {
				if (relation.isNormality() && relation.getCurrent().getComponent().equals(component)) {
					this.efferents.add(relation.getDepend().getComponent());
				}
			}
		}
		return this.efferents;
	}
	
	@Override
	public String getName() {
		return component.getName();
	}

	@Override
	public String toString() {
		return component.toString();
	}
}
