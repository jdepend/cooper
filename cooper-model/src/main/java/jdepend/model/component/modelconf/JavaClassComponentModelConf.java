package jdepend.model.component.modelconf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;

public class JavaClassComponentModelConf extends ComponentModelConf<JavaClassComponentConf> {

	private static final long serialVersionUID = 1785215322819969052L;

	private List<String> ignoreClasses = new ArrayList<String>();

	private transient JavaClassComponentModelConfRepo repo = new JavaClassComponentModelConfRepo();

	public JavaClassComponentModelConf() {

	}

	public JavaClassComponentModelConf(String name) {
		super(name);
	}

	/**
	 * 增加一个组件配置
	 * 
	 * @param name
	 * @param layer
	 * @param packages
	 * @throws JDependException
	 */
	public void addComponentConf(String name, int layer, List<String> classNames) throws JDependException {
		if (name == null || name.length() == 0)
			throw new JDependException("没有给定组件名！");

		if (this.contains(name))
			throw new JDependException("组件名重复！");

		for (JavaClassComponentConf componentConf : this.getComponentConfs()) {
			for (String className : classNames) {
				if (componentConf.getClassNames().contains(className)) {
					throw new JDependException("该组件名选择的类[" + className + "]已经在组件[" + componentConf.getName() + "]中包含！");
				}
			}
		}

		JavaClassComponentConf componentConf = new JavaClassComponentConf(name, classNames);
		componentConf.setLayer(layer);
		this.addComponentConf(componentConf);
	}

	/**
	 * 得到没有包含在组件模型中的javaClasses
	 * 
	 * @param classes
	 * 
	 * @return
	 */
	public List<String> calIgnoreClasses(List<JavaClass> classes) {

		Collection<String> containClasses = this.getContainClasses();

		List<String> ignoreClasses = new ArrayList<String>();
		for (JavaClass javaClass : classes) {
			if (!containClasses.contains(javaClass.getName()) && javaClass.isInner()) {
				ignoreClasses.add(javaClass.getName());
			}
		}
		return ignoreClasses;
	}

	/**
	 * 得到在创建该组件模型时未被包含的javaClasses
	 * 
	 * @return
	 */
	public List<String> getIgnoreClasses() {
		return ignoreClasses;
	}

	public void setIgnoreClasses(List<String> ignoreClasses) {
		this.ignoreClasses = ignoreClasses;
	}

	/**
	 * 得到该组件模型包含的javaClasses
	 * 
	 * @return
	 */
	public Collection<String> getContainClasses() {
		Collection<String> containClasses = new HashSet<String>();
		for (JavaClassComponentConf componentConf : this.getComponentConfs()) {
			for (String className : componentConf.getClassNames()) {
				containClasses.add(className);
			}
		}
		return containClasses;
	}

	@Override
	public JavaClassComponentModelConf clone() throws CloneNotSupportedException {
		JavaClassComponentModelConf conf = new JavaClassComponentModelConf(this.getName());
		conf.ignoreClasses = new ArrayList<String>();
		for (String ignorePackage : this.ignoreClasses) {
			conf.ignoreClasses.add(ignorePackage);
		}
		for (JavaClassComponentConf componentConf : this.getComponentConfs()) {
			conf.addComponentConf(componentConf.clone());
		}
		return conf;
	}

	@Override
	public Element save(Document document) {
		return this.repo.save(document, this);
	}

	@Override
	public JavaClassComponentModelConf load(Node componentModel) throws JDependException {
		return this.repo.load(componentModel);
	}
}
