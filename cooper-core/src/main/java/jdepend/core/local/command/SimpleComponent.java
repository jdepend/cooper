package jdepend.core.local.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;

/**
 * 由args设置的Component
 * 
 * @author <b>Abner</b>
 * 
 */
public final class SimpleComponent extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3612255931574478103L;

	private List<String> componentNames;

	public SimpleComponent() {
		super();
	}

	public SimpleComponent(String name) {
		super(name);
	}

	public void setComponentNames(List<String> componentNames) {
		this.componentNames = componentNames;
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws JDependException {

		List<Component> components = new ArrayList<Component>();
		SimpleComponent component;

		for (String componentName : componentNames) {
			component = new SimpleComponent(componentName);
			component.setTitle(componentName);
			for (JavaPackage javaPackage : javaPackages) {
				if (javaPackage.getName().startsWith(componentName + ".")
						|| javaPackage.getName().startsWith(componentName)) {
					for (JavaClass javaClass : javaPackage.getClasses()) {
						component.addJavaClass(javaClass);
					}
				}
			}
			components.add(component);

		}
		return components;
	}

	@Override
	public Component clone(Map<String, JavaClass> javaClasses) throws JDependException {

		SimpleComponent obj = (SimpleComponent) super.clone(javaClasses);
		obj.setComponentNames(this.componentNames);

		return obj;
	}

	/**
	 * Sets the list of components.
	 * 
	 * @param components
	 *            Comma-separated list of components.
	 */
	public static SimpleComponent calSimpleComponent(String components, String split) {
		List<String> componentLists = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(components, split);
		while (st.hasMoreTokens()) {
			String component = st.nextToken();
			componentLists.add(component);
		}
		if (componentLists.size() == 0) {
			return null;
		} else {
			SimpleComponent sc = new SimpleComponent();
			sc.setComponentNames(componentLists);
			return sc;
		}
	}

}
