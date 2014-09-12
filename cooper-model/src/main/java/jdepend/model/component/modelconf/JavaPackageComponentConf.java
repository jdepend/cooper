package jdepend.model.component.modelconf;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jdepend.model.JavaClass;

public final class JavaPackageComponentConf extends ComponentConf {

	private static final long serialVersionUID = 7000463243306131779L;

	private Collection<String> packages = new HashSet<String>();

	public JavaPackageComponentConf(String name) {
		super(name);
	}

	public JavaPackageComponentConf(String name, int layer) {
		super(name, layer);
	}

	public JavaPackageComponentConf(String name, List<String> packages) {
		super(name);
		this.packages = packages;
	}

	public Collection<String> getPackages() {
		return packages;
	}

	public void addPackages(Collection<String> joinPackages) {
		for (String packageName : joinPackages) {
			this.addPackage(packageName);
		}
	}

	public void addPackage(String joinPackage) {
		if (!packages.contains(joinPackage)) {
			packages.add(joinPackage);
		}
	}

	public void deletePackages(Collection<String> deletePackages) {
		Iterator<String> iterator = this.packages.iterator();
		while (iterator.hasNext()) {
			if (deletePackages.contains(iterator.next())) {
				iterator.remove();
			}
		}
	}

	public void deletePackage(String deletePackage) {
		this.packages.remove(deletePackage);
	}

	@Override
	public boolean isMember(JavaClass javaClass) {
		return packages.contains(javaClass.getJavaPackage().getName());
	}

	@Override
	public JavaPackageComponentConf clone() throws CloneNotSupportedException {
		JavaPackageComponentConf conf = new JavaPackageComponentConf(this.getName(), this.getLayer());
		conf.packages = new HashSet<String>();
		for (String packageName : this.packages) {
			conf.packages.add(packageName);
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

		for (String packageName : this.packages) {
			content.append(packageName);
			content.append("、");
		}
		content.delete(content.length() - 1, content.length());
		content.append("\n");

		return content.toString();
	}
}
