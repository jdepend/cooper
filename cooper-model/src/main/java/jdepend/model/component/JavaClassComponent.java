package jdepend.model.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;

/**
 * 将基本JavaClass作为组件
 * 
 * @author <b>Abner</b>
 * 
 */
public final class JavaClassComponent extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5348956077056506434L;

	public JavaClassComponent() {
	}

	public JavaClassComponent(String name) {
		super(name);
	}

	@Override
	public float objectOriented() {
		return this.getClasses().iterator().next().objectOriented();
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws JDependException {

		List<Component> components = new ArrayList<Component>();
		JavaClassComponent component;
		for (JavaPackage javaPackage : javaPackages) {
			for (JavaClass javaClass : javaPackage.getClasses()) {
				component = new JavaClassComponent(javaClass.getName());
				component.addJavaClass(javaClass);
				components.add(component);
			}
		}
		return components;
	}
}
