package jdepend.model.component.modelconf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 组件模型下一个组件的配置信息
 * 
 * @author wangdg
 * 
 */
public final class ComponentConf implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7000463243306131779L;

	private String name;

	private int layer;

	private List<String> packages = new ArrayList<String>();

	public ComponentConf(String name) {
		super();
		this.name = name;
	}

	public ComponentConf(String name, List<String> packages) {
		super();
		this.name = name;
		this.packages = packages;
	}

	public List<String> getPackages() {
		return packages;
	}

	public void addPackages(List<String> joinPackages) {
		for (String packageName : joinPackages) {
			this.addPackage(packageName);
		}
	}

	public void addPackage(String joinPackage) {
		if (!packages.contains(joinPackage)) {
			packages.add(joinPackage);
		}
	}

	public void deletePackages(List<String> deletePackages) {
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

	public String getName() {
		return name;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	@Override
	public ComponentConf clone() throws CloneNotSupportedException {
		ComponentConf conf = new ComponentConf(this.name);
		conf.layer = this.layer;
		conf.packages = new ArrayList<String>();
		for (String packageName : this.packages) {
			conf.packages.add(packageName);
		}
		return conf;
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append("组件名称：");
		content.append(this.name);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ComponentConf other = (ComponentConf) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
