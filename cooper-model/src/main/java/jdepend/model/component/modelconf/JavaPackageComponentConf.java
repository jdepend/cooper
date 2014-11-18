package jdepend.model.component.modelconf;

import java.util.List;

import jdepend.model.JavaClass;

public final class JavaPackageComponentConf extends ComponentConf {

	private static final long serialVersionUID = 7000463243306131779L;

	public JavaPackageComponentConf(String name) {
		super(name);
	}

	public JavaPackageComponentConf(String name, int layer) {
		super(name, layer);
	}

	public JavaPackageComponentConf(String name, List<String> packages) {
		super(name, packages);
	}

	@Override
	public boolean isMember(JavaClass javaClass) {
		return this.getItemIds().contains(javaClass.getJavaPackage().getId());
	}

	@Override
	public JavaPackageComponentConf clone() throws CloneNotSupportedException {
		JavaPackageComponentConf conf = new JavaPackageComponentConf(this.getName(), this.getLayer());
		for (String packageName : this.getItemIds()) {
			conf.addItemId(packageName);
		}
		return conf;
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append("组件名称：");
		content.append(this.getName());
		content.append("\n");
		content.append("包含的包：");

		for (String packageName : this.getItemIds()) {
			content.append(packageName);
			content.append("、");
		}
		content.delete(content.length() - 1, content.length());
		content.append("\n");

		return content.toString();
	}
}
