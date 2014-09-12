package test.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileUtil;
import jdepend.model.Component;
import jdepend.model.component.modelconf.ComponentConf;
import jdepend.model.component.modelconf.JavaPackageComponentConf;
import jdepend.model.component.modelconf.JavaPackageComponentModelConf;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ComponentModelsFormatConvert {

	/**
	 * @param args
	 * @throws JDependException
	 */
	public static void main(String[] args) throws JDependException {
		String dirName = "C:/Users/user/ks/cooper/workspace/conf/componentconf/";
		File dir = new File(dirName);

		for (String directoryFile : dir.list()) {
			if (directoryFile.endsWith(".xml")) {
				save(loadComponentModelConfs(dirName + directoryFile), dirName + directoryFile);
			}
		}

	}

	private static Map<String, JavaPackageComponentModelConf> loadComponentModelConfs(String fileName) throws JDependException {

		Map<String, JavaPackageComponentModelConf> componentModelConfs = new LinkedHashMap<String, JavaPackageComponentModelConf>();
		JavaPackageComponentModelConf componentModelConf;

		Properties componentsInfo = new Properties();
		FileUtil.readFileContentXML(fileName, componentsInfo);

		String[] packageNames;
		ArrayList<String> packageInfo;
		String keyInfo;
		String componentModelName;
		String componentName;
		int layer;

		int componentModelNamePos;
		int componentNamePos;

		for (Object key : componentsInfo.keySet()) {

			keyInfo = (String) key;
			componentModelNamePos = keyInfo.indexOf(',');
			componentModelName = keyInfo.substring(0, componentModelNamePos);
			componentNamePos = keyInfo.indexOf(',', componentModelNamePos + 1);
			if (componentNamePos != -1) {
				componentName = keyInfo.substring(componentModelNamePos + 1, componentNamePos);
				layer = Integer.parseInt(keyInfo.substring(componentNamePos + 1));
			} else {
				componentName = keyInfo.substring(componentModelNamePos + 1);
				layer = Component.UndefinedComponentLevel;
			}

			packageNames = ((String) componentsInfo.get(key)).split(",");
			packageInfo = new ArrayList<String>(packageNames.length);
			for (int i = 0; i < packageNames.length; i++) {
				packageInfo.add(packageNames[i]);
			}

			if (!componentModelConfs.containsKey(componentModelName)) {
				componentModelConf = new JavaPackageComponentModelConf(componentModelName);
				componentModelConf.addComponentConf(componentName, layer, packageInfo);
				componentModelConfs.put(componentModelName, componentModelConf);
			} else {
				componentModelConfs.get(componentModelName).addComponentConf(componentName, layer, packageInfo);
			}
		}
		return componentModelConfs;
	}

	public static void save(Map<String, JavaPackageComponentModelConf> componentModelConfs, String fileName)
			throws JDependException {

		if (componentModelConfs != null && componentModelConfs.size() > 0) {
			String group = fileName.substring(fileName.indexOf('_') + 1, fileName.indexOf('.'));
			try {
				// Document-->Node
				Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Element root = document.createElement("componentModels");
				root.setAttribute("group", group);// 添加group属性

				for (String componentModelName : componentModelConfs.keySet()) {
					Element nelement = document.createElement("componentModel");// 元素节点
					nelement.setAttribute("name", componentModelName);// 添加group属性
					// 添加组件信息
					for (ComponentConf componentConf : componentModelConfs.get(componentModelName).getComponentConfs()) {
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
					List<String> ignorePackages = componentModelConfs.get(componentModelName).getIgnoreItems();
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
				output(root, fileName);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			FileUtil.deleteFile(fileName);
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

}
