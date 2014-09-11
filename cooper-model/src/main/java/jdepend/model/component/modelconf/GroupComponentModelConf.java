package jdepend.model.component.modelconf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * 一个命令组配置的组件模型信息（一个组可以配置多个组件模型）
 * 
 * @author wangdg
 * 
 */
public final class GroupComponentModelConf {

	private String group;

	private Map<String, ComponentModelConf<ComponentConf>> componentModelConfs;

	public static final String DEFAULT_PROPERTY_DIR = "conf\\componentconf";

	public GroupComponentModelConf(String group) throws JDependException {
		this.group = group;
		this.componentModelConfs = this.loadComponentModelConfs();
	}

	public GroupComponentModelConf(String group, Map<String, ComponentModelConf<ComponentConf>> componentModelConfs) {
		super();
		this.group = group;
		this.componentModelConfs = componentModelConfs;
	}

	private Map<String, ComponentModelConf<ComponentConf>> loadComponentModelConfs() throws JDependException {
		Map<String, ComponentModelConf<ComponentConf>> componentModelConfs = new LinkedHashMap<String, ComponentModelConf<ComponentConf>>();
		JavaPackageComponentModelConf componentModelConf;

		if (!(new File(getComponentFilePath(group))).exists())
			return componentModelConfs;

		try {
			DocumentBuilder domBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputStream input = new FileInputStream(getComponentFilePath(group));
			Document doc = domBuilder.parse(input);
			Element root = doc.getDocumentElement();
			NodeList componentModels = root.getChildNodes();
			if (componentModels != null) {
				for (int i = 0; i < componentModels.getLength(); i++) {
					Node componentModel = componentModels.item(i);
					if (componentModel.getNodeType() == Node.ELEMENT_NODE) {
						String componentModelName = componentModel.getAttributes().getNamedItem("name").getNodeValue();
						componentModelConf = new JavaPackageComponentModelConf(componentModelName);

						for (Node node = componentModel.getFirstChild(); node != null; node = node.getNextSibling()) {
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								if (node.getNodeName().equals("component")) {
									String componentName = node.getAttributes().getNamedItem("name").getNodeValue();
									int layer = Integer.parseInt(node.getAttributes().getNamedItem("layer")
											.getNodeValue());
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
									componentModelConf.setIgnorePackages(ignorePackages);
								}
							}
						}
						componentModelConfs.put(componentModelConf.getName(), (ComponentModelConf) componentModelConf);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return componentModelConfs;
	}

	public void save() throws JDependException {

		if (componentModelConfs != null && componentModelConfs.size() > 0) {
			try {
				Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Element root = document.createElement("componentModels");
				root.setAttribute("group", group);// 添加group属性

				for (String componentModelName : componentModelConfs.keySet()) {
					Element nelement = document.createElement("componentModel");// 组件模型节点
					nelement.setAttribute("name", componentModelName);// 添加name属性
					// 添加组件信息
					for (ComponentConf componentConf : componentModelConfs.get(componentModelName).getComponentConfs()) {
						Element selement = document.createElement("component");// 组件节点
						selement.setAttribute("name", componentConf.getName());
						selement.setAttribute("layer", String.valueOf(componentConf.getLayer()));
						for (String packageName : ((JavaPackageComponentConf) componentConf).getPackages()) {
							Element eelement = document.createElement("package");
							eelement.setTextContent(packageName);
							selement.appendChild(eelement);
						}
						nelement.appendChild(selement);
					}
					// 添加未包含的packages
					ComponentModelConf componentModelConf = componentModelConfs.get(componentModelName);
					List<String> ignorePackages = ((JavaPackageComponentModelConf) componentModelConf)
							.getIgnorePackages();
					if (ignorePackages != null && ignorePackages.size() > 0) {
						Element ielements = document.createElement("ignorePackages");
						for (String ignorePackage : ignorePackages) {
							Element ielement = document.createElement("package");
							ielement.setTextContent(ignorePackage);
							ielements.appendChild(ielement);
						}
						nelement.appendChild(ielements);
					}
					root.appendChild(nelement);
				}
				output(root, getComponentFilePath(group));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			FileUtil.deleteFile(getComponentFilePath(group));
		}

	}

	private static void output(Node node, String filename) {
		try {
			PrintWriter pWriter = new PrintWriter(new java.io.FileOutputStream(filename));
			XMLSerializer serl = new XMLSerializer(pWriter, new OutputFormat("xml", "UTF-8", true));// 这里的参数（TRUE）表示格式化为自动换行
			serl.serialize(node);

			pWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getComponentFilePath(String group) {
		return JDependContext.getWorkspacePath() + "\\" + GroupComponentModelConf.DEFAULT_PROPERTY_DIR + "\\component_"
				+ group + ".xml";
	}

	public String getGroup() {
		return group;
	}

	public Map<String, ComponentModelConf<ComponentConf>> getComponentModelConfs() {
		return componentModelConfs;
	}

	public void setComponentModelConfs(Map<String, ComponentModelConf<ComponentConf>> componentModelConfs) {
		this.componentModelConfs = componentModelConfs;
	}

	public void addComponentModelConf(ComponentModelConf<ComponentConf> componentModelConf) throws JDependException {
		if (this.componentModelConfs.containsKey(componentModelConf.getName())) {
			throw new JDependException("组件模型[" + componentModelConf.getName() + "]重复");
		}
		this.componentModelConfs.put(componentModelConf.getName(), componentModelConf);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
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
		final GroupComponentModelConf other = (GroupComponentModelConf) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		return true;
	}

}
