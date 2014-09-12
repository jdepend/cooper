package jdepend.model.component.modelconf;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class JavaPackageComponentModelConfRepo {

	public Element save(Document document, JavaPackageComponentModelConf componentModelConf) {

		Element nelement = document.createElement("componentModel");// 组件模型节点
		nelement.setAttribute("name", componentModelConf.getName());// 添加name属性
		nelement.setAttribute("type", ComponentModelConf.ComponentModelType_Package);// 添加type属性
		// 添加组件信息
		for (JavaPackageComponentConf componentConf : componentModelConf.getComponentConfs()) {
			Element selement = document.createElement("component");// 组件节点
			selement.setAttribute("name", componentConf.getName());
			selement.setAttribute("layer", String.valueOf(componentConf.getLayer()));
			for (String packageName : componentConf.getItemNames()) {
				Element eelement = document.createElement("package");
				eelement.setTextContent(packageName);
				selement.appendChild(eelement);
			}
			nelement.appendChild(selement);
		}
		// 添加未包含的packages
		List<String> ignorePackages = componentModelConf.getIgnoreItems();
		if (ignorePackages != null && ignorePackages.size() > 0) {
			Element ielements = document.createElement("ignorePackages");
			for (String ignorePackage : ignorePackages) {
				Element ielement = document.createElement("package");
				ielement.setTextContent(ignorePackage);
				ielements.appendChild(ielement);
			}
			nelement.appendChild(ielements);
		}

		return nelement;
	}

	public JavaPackageComponentModelConf load(Node componentModel) throws JDependException {

		String componentModelName = componentModel.getAttributes().getNamedItem("name").getNodeValue();
		JavaPackageComponentModelConf componentModelConf = new JavaPackageComponentModelConf(componentModelName);

		for (Node node = componentModel.getFirstChild(); node != null; node = node.getNextSibling()) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals("component")) {
					String componentName = node.getAttributes().getNamedItem("name").getNodeValue();
					int layer = Integer.parseInt(node.getAttributes().getNamedItem("layer").getNodeValue());
					List<String> packages = new ArrayList<String>();
					for (int k = 0; k < node.getChildNodes().getLength(); k++) {
						Node Package = node.getChildNodes().item(k);
						if (Package.getNodeType() == Node.ELEMENT_NODE) {
							packages.add(Package.getTextContent());
						}
					}
					componentModelConf.addComponentConf(componentName, layer, packages);
				} else if (node.getNodeName().equals("ignorePackages")) {
					List<String> ignorePackages = new ArrayList<String>();
					for (int k = 0; k < node.getChildNodes().getLength(); k++) {
						Node Package = node.getChildNodes().item(k);
						if (Package.getNodeType() == Node.ELEMENT_NODE) {
							ignorePackages.add(Package.getTextContent());
						}
					}
					componentModelConf.setIgnoreItems(ignorePackages);
				}
			}
		}

		return componentModelConf;
	}

}
