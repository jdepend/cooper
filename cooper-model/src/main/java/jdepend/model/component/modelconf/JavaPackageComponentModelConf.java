package jdepend.model.component.modelconf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaPackage;

public class JavaPackageComponentModelConf extends ComponentModelConf<JavaPackageComponentConf> {

	private static final long serialVersionUID = 5051041678062375195L;

	private transient JavaPackageComponentModelConfRepo repo = new JavaPackageComponentModelConfRepo();

	public JavaPackageComponentModelConf() {

	}

	public JavaPackageComponentModelConf(String name) {
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
	public void addComponentConf(String name, int layer, List<String> packageNames) throws JDependException {
		if (name == null || name.length() == 0)
			throw new JDependException("没有给定组件名！");

		if (this.contains(name))
			throw new JDependException("组件名重复！");

		for (JavaPackageComponentConf componentConf : this.getComponentConfs()) {
			for (String packageName : packageNames) {
				if (componentConf.getItemNames().contains(packageName)) {
					throw new JDependException("该组件名选择的包[" + packageName + "]已经在组件[" + componentConf.getName()
							+ "]中包含！");
				}
			}
		}

		JavaPackageComponentConf componentConf = new JavaPackageComponentConf(name, packageNames);
		componentConf.setLayer(layer);
		this.addComponentConf(componentConf);
	}

	/**
	 * 得到没有包含在组件模型中的javaPackages
	 * 
	 * @param packages
	 * 
	 * @return
	 */
	public List<String> calIgnorePackages(List<JavaPackage> packages) {

		Collection<String> containPackages = this.getContainItems();

		List<String> ignorePackages = new ArrayList<String>();
		for (JavaPackage javaPackage : packages) {
			if (!containPackages.contains(javaPackage.getName()) && javaPackage.isInner()) {
				ignorePackages.add(javaPackage.getName());
			}
		}
		return ignorePackages;
	}

	@Override
	public JavaPackageComponentModelConf clone() throws CloneNotSupportedException {
		JavaPackageComponentModelConf conf = new JavaPackageComponentModelConf(this.getName());
		for (String ignoreItem : this.getIgnoreItems()) {
			conf.addIgnoreItem(ignoreItem);
		}
		for (JavaPackageComponentConf componentConf : this.getComponentConfs()) {
			conf.addComponentConf(componentConf.clone());
		}
		return conf;
	}

	@Override
	public Element save(Document document) {
		return this.repo.save(document, this);
	}

	@Override
	public JavaPackageComponentModelConf load(Node componentModel) throws JDependException {
		return this.repo.load(componentModel);
	}
}
