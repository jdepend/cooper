package jdepend.model.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;
import jdepend.model.result.AnalysisRunningContextMgr;

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
		Map<String, Collection<String>> targetFiles = AnalysisRunningContextMgr.getContext().getTargetFiles();
		if (targetFiles == null || targetFiles.isEmpty()) {
			throw new JDependException("没有目标文件分组信息");
		}
		List<Component> components = new ArrayList<Component>();
		JarComponent component;
		Collection<String> classNames;

		for (String name : targetFiles.keySet()) {
			component = new JarComponent(name);
			classNames = targetFiles.get(name);
			for (JavaPackage javaPackage : javaPackages) {
				for (JavaClass javaClass : javaPackage.getClasses()) {
					if (classNames.contains(javaClass.getName())) {
						component.addJavaClass(javaClass);
					}
				}
			}
			components.add(component);
		}

		return components;
	}

}
