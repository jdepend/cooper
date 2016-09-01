package jdepend.model.component.modelconf;

import java.util.Collection;
import java.util.List;

import jdepend.metadata.Candidate;
import jdepend.metadata.JavaPackage;
import jdepend.metadata.util.JavaClassUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class JavaClassComponentModelConf extends ComponentModelConf {

	private static final long serialVersionUID = 1785215322819969052L;

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
	 * @throws ComponentConfException
	 */
	@Override
	public void addComponentConf(String name, int layer, List<String> classNames) throws ComponentConfException {
		if (name == null || name.length() == 0)
			throw new ComponentConfException("没有给定组件名！");

		if (this.contains(name))
			throw new ComponentConfException("组件名重复！");

		for (ComponentConf componentConf : this.getComponentConfs()) {
			for (String className : classNames) {
				if (componentConf.getItemIds().contains(className)) {
					throw new ComponentConfException("该组件名选择的类[" + className + "]已经在组件[" + componentConf.getName()
							+ "]中包含！");
				}
			}
		}

		JavaClassComponentConf componentConf = new JavaClassComponentConf(name, classNames);
		componentConf.setLayer(layer);
		this.addComponentConf(componentConf);
	}

	@Override
	public JavaClassComponentModelConf clone() throws CloneNotSupportedException {
		JavaClassComponentModelConf conf = new JavaClassComponentModelConf(this.getName());
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
	public JavaClassComponentModelConf load(Node componentModel) throws ComponentConfException {
		return this.repo.load(componentModel);
	}

	@Override
	public Collection<? extends Candidate> getCandidates(Collection<JavaPackage> packages) {
		return JavaClassUtil.getClassesForJavaPackages(packages);
	}
}
