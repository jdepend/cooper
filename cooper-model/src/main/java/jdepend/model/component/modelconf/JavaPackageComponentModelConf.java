package jdepend.model.component.modelconf;

import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.metadata.Candidate;
import jdepend.metadata.JavaPackage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class JavaPackageComponentModelConf extends ComponentModelConf {

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
	@Override
	public void addComponentConf(String name, int layer, List<String> packageNames) throws JDependException {
		if (name == null || name.length() == 0)
			throw new JDependException("没有给定组件名！");

		if (this.contains(name))
			throw new JDependException("组件名重复！");

		for (ComponentConf componentConf : this.getComponentConfs()) {
			for (String packageName : packageNames) {
				if (componentConf.getItemIds().contains(packageName)) {
					throw new JDependException("该组件名选择的包[" + packageName + "]已经在组件[" + componentConf.getName()
							+ "]中包含！");
				}
			}
		}

		JavaPackageComponentConf componentConf = new JavaPackageComponentConf(name, packageNames);
		componentConf.setLayer(layer);
		this.addComponentConf(componentConf);
	}

	@Override
	public JavaPackageComponentModelConf clone() throws CloneNotSupportedException {
		JavaPackageComponentModelConf conf = new JavaPackageComponentModelConf(this.getName());
		for (String ignoreItem : this.getIgnoreItems()) {
			conf.addIgnoreItem(ignoreItem);
		}
		for (ComponentConf componentConf : this.getComponentConfs()) {
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

	@Override
	public Collection<? extends Candidate> getCandidates(Collection<JavaPackage> packages) {
		return packages;
	}
}
