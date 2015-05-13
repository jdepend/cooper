package jdepend.model.component.modelconf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.metadata.Candidate;
import jdepend.metadata.JavaPackage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 组件模型配置信息类
 * 
 * @author <b>Abner</b>
 * 
 */
public abstract class ComponentModelConf implements Serializable, Cloneable {

	private String name;

	private List<ComponentConf> componentConfs = new ArrayList<ComponentConf>();

	private List<String> ignoreItems = new ArrayList<String>();

	public final static String ComponentModelType_Package = "package";
	public final static String ComponentModelType_Class = "class";

	public final static String ADD = "ADD";
	public final static String DELETE = "DELETE";

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

	public abstract void addComponentConf(String name, int layer, List<String> packageNames) throws JDependException;

	protected void addComponentConf(ComponentConf componentConf) {
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
		ComponentConf componentConf = this.getTheComponentConf(name);
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
	 * 得到在创建该组件模型时未被包含的javaPackages
	 * 
	 * @return
	 */
	public List<String> getIgnoreItems() {
		return ignoreItems;
	}

	public void setIgnoreItems(List<String> ignoreItems) {
		this.ignoreItems = ignoreItems;
	}

	public void addIgnoreItem(String ignoreItem) {
		if (!this.ignoreItems.contains(ignoreItem)) {
			this.ignoreItems.add(ignoreItem);
		}
	}

	/**
	 * 得到该组件模型包含的javaPackages
	 * 
	 * @return
	 */
	public Collection<String> getContainItems() {
		Collection<String> containItems = new ArrayList<String>();
		for (ComponentConf componentConf : this.getComponentConfs()) {
			for (String itemName : componentConf.getItemIds()) {
				containItems.add(itemName);
			}
		}
		return containItems;
	}

	public void validateData() throws JDependException {
		if (this.size() == 0) {
			throw new JDependException("您没有设置组件!");
		}
		if (this.getName() == null || this.getName().length() == 0) {
			throw new JDependException("未指定组件组名称!");
		}
	}

	public abstract ComponentModelConf clone() throws CloneNotSupportedException;

	public abstract Element save(Document document);

	public abstract ComponentModelConf load(Node componentModel) throws JDependException;

	public abstract Collection<? extends Candidate> getCandidates(Collection<JavaPackage> packages);

	public Map<String, String> calDiffElements(Collection<JavaPackage> packages) {

		Map<String, String> diffElements = new LinkedHashMap<String, String>();

		Collection<String> containItems = this.getContainItems();
		Collection<String> ignoreItems = this.getIgnoreItems();
		Collection<String> runItems = new ArrayList<String>();

		Map<String, Candidate> candidateForId = new HashMap<String, Candidate>();
		for (Candidate candidate : this.getCandidates(packages)) {
			runItems.add(candidate.getId());
			candidateForId.put(candidate.getId(), candidate);
		}
		for (String runItem : runItems) {
			if (!containItems.contains(runItem) && !ignoreItems.contains(runItem)) {
				if (candidateForId.get(runItem).isInner()) {
					diffElements.put(runItem, ADD);
				}
			}
		}
		for (String containItem : containItems) {
			if (!runItems.contains(containItem)) {
				diffElements.put(containItem, DELETE);
			}
		}

		return diffElements;
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
