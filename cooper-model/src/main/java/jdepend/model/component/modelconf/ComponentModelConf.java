package jdepend.model.component.modelconf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;

/**
 * 组件模型配置信息类
 * 
 * @author <b>Abner</b>
 * 
 */
public abstract class ComponentModelConf<T extends ComponentConf> implements Serializable, Cloneable {

	private String name;

	private List<T> componentConfs = new ArrayList<T>();

	public ComponentModelConf() {

	}

	public ComponentModelConf(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected void addComponentConf(T componentConf) {
		if (!componentConfs.contains(componentConf)) {
			componentConfs.add(componentConf);
		}
	}

	/**
	 * 删除一个组件配置
	 * 
	 * @param name
	 */
	public void deleteComponentConf(String name) {
		T componentConf = this.getTheComponentConf(name);
		if (componentConf != null) {
			componentConfs.remove(componentConf);
		}
	}

	public boolean contains(String name) {
		return this.getTheComponentConf(name) == null ? false : true;
	}

	public int size() {
		return componentConfs.size();
	}

	/**
	 * 得到组件列表名称
	 * 
	 * @return
	 */
	public List<String> getComponentConfNames() {

		List<String> names = new ArrayList<String>();

		for (T componentConf : componentConfs) {
			names.add(componentConf.getName());
		}

		return names;
	}

	/**
	 * 得到指定组件配置信息
	 * 
	 * @param name
	 * @return
	 */
	public T getTheComponentConf(String name) {

		for (T componentConf : componentConfs) {
			if (componentConf.getName().equals(name)) {
				return componentConf;
			}
		}
		return null;
	}

	public List<T> getComponentConfs() {
		return this.componentConfs;
	}

	public void validateData() throws JDependException {
		if (this.size() == 0) {
			throw new JDependException("您没有设置组件!");
		}
		if (this.getName() == null || this.getName().length() == 0) {
			throw new JDependException("未指定组件组名称!");
		}
	}

	public abstract ComponentModelConf<T> clone() throws CloneNotSupportedException;

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append("组件模型名称：");
		content.append(this.name);
		content.append("\n");

		for (ComponentConf conf : this.componentConfs) {
			content.append(conf);
		}

		return content.toString();
	}
}
