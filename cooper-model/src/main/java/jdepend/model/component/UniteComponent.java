package jdepend.model.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;

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
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws JDependException {
		throw new JDependException("合并组件不进行组件的识别");
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
			for (JavaClass javaClass : component.getClasses()) {
				this.addJavaClass(javaClass);
			}
		}
	}

	@Override
	public Component clone(Map<String, JavaClass> javaClasses) throws JDependException {
		UniteComponent obj = (UniteComponent) super.clone(javaClasses);
		obj.setSubComponents(this.subComponents);

		return obj;
	}

}
