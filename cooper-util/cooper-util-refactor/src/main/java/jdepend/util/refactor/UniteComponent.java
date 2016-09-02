package jdepend.util.refactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.ComponentException;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClassUnit;

public final class UniteComponent extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2809762258520777271L;

	private Collection<String> subComponents = new ArrayList<String>();

	public UniteComponent() {
		super();
	}

	public UniteComponent(String name) {
		super(name);
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws ComponentException {
		throw new ComponentException("合并组件不进行组件的识别");
	}

	public Collection<String> getSubComponents() {
		return subComponents;
	}

	public void setSubComponents(Collection<String> subComponents) {
		this.subComponents = subComponents;
	}

	public void unite() {
		Component component;
		for (String componentName : subComponents) {
			component = JDependUnitMgr.getInstance().getResult().getTheComponent(componentName);
			for (JavaClassUnit javaClass : component.getClasses()) {
				this.addJavaClass(javaClass);
			}
		}
	}

	@Override
	public Component clone(Map<String, JavaClassUnit> javaClasses) throws ComponentException {
		UniteComponent obj = (UniteComponent) super.clone(javaClasses);
		obj.setSubComponents(this.subComponents);

		return obj;
	}

}
