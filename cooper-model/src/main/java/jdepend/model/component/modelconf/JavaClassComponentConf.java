package jdepend.model.component.modelconf;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jdepend.model.JavaClass;

public final class JavaClassComponentConf extends ComponentConf {

	private static final long serialVersionUID = 3187194666726871490L;

	private Collection<String> classNames = new HashSet<String>();

	public JavaClassComponentConf(String name) {
		super(name);
	}

	public JavaClassComponentConf(String name, int layer) {
		super(name, layer);
	}

	public JavaClassComponentConf(String name, List<String> classNames) {
		super(name);
		this.classNames = classNames;
	}

	public Collection<String> getClassNames() {
		return classNames;
	}

	public void addClassNames(Collection<String> joinClassNames) {
		for (String className : joinClassNames) {
			this.addClassName(className);
		}
	}

	public void addClassName(String className) {
		if (!classNames.contains(className)) {
			classNames.add(className);
		}
	}

	public void deleteClassNames(Collection<String> deleteClassNames) {
		Iterator<String> iterator = this.classNames.iterator();
		while (iterator.hasNext()) {
			if (deleteClassNames.contains(iterator.next())) {
				iterator.remove();
			}
		}
	}

	public void deleteClassName(String deleteClassName) {
		this.classNames.remove(deleteClassName);
	}

	@Override
	public boolean isMember(JavaClass javaClass) {
		return classNames.contains(javaClass.getName());
	}

	@Override
	public JavaClassComponentConf clone() throws CloneNotSupportedException {
		JavaClassComponentConf conf = new JavaClassComponentConf(this.getName(), this.getLayer());
		conf.classNames = new HashSet<String>();
		for (String className : this.classNames) {
			conf.classNames.add(className);
		}
		return conf;
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append("组件名称：");
		content.append(this.getName());
		content.append("\n");
		content.append("包含的类：");

		for (String className : this.classNames) {
			content.append(className);
			content.append("、");
		}
		content.delete(content.length() - 1, content.length());
		content.append("\n");

		return content.toString();
	}
}
