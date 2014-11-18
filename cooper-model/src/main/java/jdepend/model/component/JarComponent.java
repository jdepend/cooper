package jdepend.model.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;

public final class JarComponent extends Component {

	private static final long serialVersionUID = 3708104803198587661L;

	public JarComponent() {
		super();
	}

	public JarComponent(String name) {
		super(name);
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws JDependException {

		Map<String, JarComponent> components = new HashMap<String, JarComponent>();
		JarComponent component;

		for (JavaPackage javaPackage : javaPackages) {
			component = components.get(javaPackage.getPlace());
			if (component == null) {
				component = new JarComponent(javaPackage.getPlace());
				components.put(javaPackage.getPlace(), component);
			}
			for (JavaClass javaClass : javaPackage.getClasses()) {
				component.addJavaClass(javaClass);
			}
		}

		return new ArrayList<Component>(components.values());
	}

}
