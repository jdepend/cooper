package jdepend.model.component.modelconf;

import java.util.List;

import jdepend.model.JavaClass;

public final class JavaClassComponentConf extends ComponentConf {

	private static final long serialVersionUID = 3187194666726871490L;

	public JavaClassComponentConf(String name) {
		super(name);
	}

	public JavaClassComponentConf(String name, int layer) {
		super(name, layer);
	}

	public JavaClassComponentConf(String name, List<String> classNames) {
		super(name, classNames);
	}

	@Override
	public boolean isMember(JavaClass javaClass) {
		return this.getItemNames().contains(javaClass.getName());
	}

	@Override
	public JavaClassComponentConf clone() throws CloneNotSupportedException {
		JavaClassComponentConf conf = new JavaClassComponentConf(this.getName(), this.getLayer());
		for (String className : this.getItemNames()) {
			conf.addItemName(className);
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

		for (String className : this.getItemNames()) {
			content.append(className);
			content.append("、");
		}
		content.delete(content.length() - 1, content.length());
		content.append("\n");

		return content.toString();
	}
}
