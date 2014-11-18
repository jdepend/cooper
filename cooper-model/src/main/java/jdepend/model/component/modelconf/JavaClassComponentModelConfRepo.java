package jdepend.model.component.modelconf;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class JavaClassComponentModelConfRepo {

	public Element save(Document document, JavaClassComponentModelConf componentModelConf) {

		Element nelement = document.createElement("componentModel");// 组件模型节点
		nelement.setAttribute("name", componentModelConf.getName());// 添加name属性
		nelement.setAttribute("type", ComponentModelConf.ComponentModelType_Class);// 添加type属性
		// 添加组件信息
		for (ComponentConf componentConf : componentModelConf.getComponentConfs()) {
			Element selement = document.createElement("component");// 组件节点
			selement.setAttribute("name", componentConf.getName());
			selement.setAttribute("layer", String.valueOf(componentConf.getLayer()));
			for (String className : componentConf.getItemIds()) {
				Element eelement = document.createElement("class");
				eelement.setTextContent(className);
				selement.appendChild(eelement);
			}
			nelement.appendChild(selement);
		}
		// 添加未包含的classes
		List<String> ignoreClasses = componentModelConf.getIgnoreItems();
		if (ignoreClasses != null && ignoreClasses.size() > 0) {
			Element ielements = document.createElement("ignoreClasses");
			for (String ignoreClass : ignoreClasses) {
				Element ielement = document.createElement("class");
				ielement.setTextContent(ignoreClass);
				ielements.appendChild(ielement);
			}
			nelement.appendChild(ielements);
		}

		return nelement;
	}

	public JavaClassComponentModelConf load(Node componentModel) throws JDependException {

		String componentModelName = componentModel.getAttributes().getNamedItem("name").getNodeValue();
		JavaClassComponentModelConf componentModelConf = new JavaClassComponentModelConf(componentModelName);

		for (Node node = componentModel.getFirstChild(); node != null; node = node.getNextSibling()) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals("component")) {
					String componentName = node.getAttributes().getNamedItem("name").getNodeValue();
					int layer = Integer.parseInt(node.getAttributes().getNamedItem("layer").getNodeValue());
					List<String> classes = new ArrayList<String>();
					for (int k = 0; k < node.getChildNodes().getLength(); k++) {
						Node Class = node.getChildNodes().item(k);
						if (Class.getNodeType() == Node.ELEMENT_NODE) {
							classes.add(Class.getTextContent());
						}
					}
					componentModelConf.addComponentConf(componentName, layer, classes);
				} else if (node.getNodeName().equals("ignoreClasses")) {
					List<String> ignoreClasses = new ArrayList<String>();
					for (int k = 0; k < node.getChildNodes().getLength(); k++) {
						Node Class = node.getChildNodes().item(k);
						if (Class.getNodeType() == Node.ELEMENT_NODE) {
							ignoreClasses.add(Class.getTextContent());
						}
					}
					componentModelConf.setIgnoreItems(ignoreClasses);
				}
			}
		}

		return componentModelConf;
	}

}
