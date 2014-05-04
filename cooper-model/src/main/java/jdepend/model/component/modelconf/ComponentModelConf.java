package jdepend.model.component.modelconf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaPackage;

/**
 * 组件模型配置信息类
 * 
 * @author <b>Abner</b>
 * 
 */
public class ComponentModelConf implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5051041678062375195L;

	private String name;

	private List<ComponentConf> componentConfs = new ArrayList<ComponentConf>();

	private List<String> ignorePackages = new ArrayList<String>();

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

	/**
	 * 增加一个组件配置
	 * 
	 * @param name
	 * @param layer
	 * @param packages
	 * @throws JDependException
	 */
	public void addComponentConf(String name, int layer, List<String> packages) throws JDependException {
		if (name == null || name.length() == 0)
			throw new JDependException("没有给定组件名！");

		if (this.contains(name))
			throw new JDependException("组件名重复！");

		for (String componentName : this.getComponentConfNames()) {
			for (String packageName : this.getTheComponentConf(componentName).getPackages()) {
				for (String selectedUnit : packages) {
					if (selectedUnit.equals(packageName)) {
						throw new JDependException("该组件名选择的包[" + selectedUnit + "]已经在组件[" + componentName + "]中包含！");
					}
				}
			}
		}

		ComponentConf componentConf = new ComponentConf(name, packages);
		componentConf.setLayer(layer);
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
		ComponentConf componentConf = new ComponentConf(name);
		componentConfs.remove(componentConf);
	}

	public boolean contains(String name) {
		return componentConfs.contains(new ComponentConf(name));
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

		for (ComponentConf componentConf : componentConfs) {
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
	public ComponentConf getTheComponentConf(String name) {

		for (ComponentConf componentConf : componentConfs) {
			if (componentConf.getName().equals(name)) {
				return componentConf;
			}
		}
		return null;
	}

	public List<ComponentConf> getComponentConfs() {
		return this.componentConfs;
	}

	/**
	 * 得到没有包含在组件模型中的javaPackages
	 * 
	 * @param packages
	 * 
	 * @return
	 */
	public List<String> calIgnorePackages(List<JavaPackage> packages) {

		Collection<String> containPackages = this.getContainPackages();

		List<String> ignorePackages = new ArrayList<String>();
		for (JavaPackage javaPackage : packages) {
			if (!containPackages.contains(javaPackage.getName()) && javaPackage.isInner()) {
				ignorePackages.add(javaPackage.getName());
			}
		}
		return ignorePackages;
	}

	/**
	 * 得到在创建该组件模型时未被包含的javaPackages
	 * 
	 * @return
	 */
	public List<String> getIgnorePackages() {
		return ignorePackages;
	}

	public void setIgnorePackages(List<String> ignorePackages) {
		this.ignorePackages = ignorePackages;
	}

	/**
	 * 得到该组件模型包含的javaPackages
	 * 
	 * @return
	 */
	public Collection<String> getContainPackages() {
		Collection<String> containPackages = new HashSet<String>();
		for (ComponentConf componentConf : this.componentConfs) {
			for (String packageName : componentConf.getPackages()) {
				containPackages.add(packageName);
			}
		}
		return containPackages;
	}

	public void validateData() throws JDependException {
		if (this.size() == 0) {
			throw new JDependException("您没有设置组件!");
		}
		if (this.getName() == null || this.getName().length() == 0) {
			throw new JDependException("未指定组件组名称!");
		}
	}

	@Override
	public ComponentModelConf clone() throws CloneNotSupportedException {
		ComponentModelConf conf = new ComponentModelConf(this.name);
		conf.ignorePackages = new ArrayList<String>();
		for(String ignorePackage : this.ignorePackages){
			conf.ignorePackages.add(ignorePackage);
		}
		conf.componentConfs = new ArrayList<ComponentConf>();
		for(ComponentConf componentConf : this.componentConfs){
			conf.componentConfs.add(componentConf.clone());
		}
		return conf;
	}

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
