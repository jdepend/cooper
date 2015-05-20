package jdepend.model.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.JavaClassUnit;

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
			String place = javaPackage.getPlace();
			if (!place.equals(JavaClass.Extend_PlaceName)) {
				component = components.get(place);
				if (component == null) {
					component = new JarComponent(place);
					components.put(place, component);
				}
				for (JavaClass javaClass : javaPackage.getClasses()) {
					component.addJavaClass(new JavaClassUnit(javaClass));
				}
			}
		}

		return new ArrayList<Component>(components.values());
	}

}
