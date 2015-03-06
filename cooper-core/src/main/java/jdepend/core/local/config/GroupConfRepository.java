package jdepend.core.local.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The <code>GroupConfRepository</code> class contains configuration information
 * contained in the <code>group.properties</code> file, if such a file exists
 * either in the user's home directory or somewhere in the classpath.
 * 
 * @author <b>Abner</b>
 */

public class GroupConfRepository {

	private Collection<GroupConf> groups;

	public transient static final String DEFAULT_PROPERTY_FILE = "groups.xml";

	public transient static final String DEFAULT_PROPERTY_DIR = "conf\\groupconf";

	private String filePath;

	/**
	 * Constructs a <code>GroupConfRepository</code> instance containing the
	 * properties specified in the file <code>group.properties</code>, if it
	 * exists.
	 * 
	 * @throws JDependException
	 */
	public GroupConfRepository() throws CommandConfException {
		reload();
	}

	private void reload() throws CommandConfException {
		this.loadGroups(getDefaultPropertyFile());
	}

	private static File getDefaultPropertyFile() {
		String home = JDependContext.getWorkspacePath() + "\\" + GroupConfRepository.DEFAULT_PROPERTY_DIR;
		return new File(home, DEFAULT_PROPERTY_FILE);
	}

	private void loadGroups(File file) throws CommandConfException {

		InputStream is = null;

		try {

			is = new FileInputStream(file);
			filePath = file.getParent() + "\\" + DEFAULT_PROPERTY_FILE;

		} catch (Exception e) {
			is = GroupConfRepository.class.getResourceAsStream("/" + DEFAULT_PROPERTY_FILE);
			if (is == null) {
				is = GroupConfRepository.class.getResourceAsStream(DEFAULT_PROPERTY_FILE);
				filePath = JDependContext.getRunningPath() + "\\classes\\"
						+ GroupConfRepository.class.getPackage().getName().replace('.', '\\') + "\\"
						+ DEFAULT_PROPERTY_FILE;
			} else {
				filePath = JDependContext.getRunningPath() + "\\classes\\" + DEFAULT_PROPERTY_FILE;
			}
		}

		try {
			if (is != null) {
				this.groups = new ArrayList<GroupConf>();
				GroupConf group;
				String name;
				boolean visible;
				Node path;
				Node srcPath;
				Node filteredPackages;
				Node attribute;

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(is);
				NodeList nl = doc.getElementsByTagName("group");
				for (int i = 0; i < nl.getLength(); i++) {
					try {
						name = doc.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
						group = new GroupConf(name);

						visible = Boolean.parseBoolean(doc.getElementsByTagName("visible").item(i).getFirstChild()
								.getNodeValue());
						group.setVisible(visible);

						path = doc.getElementsByTagName("path").item(i).getFirstChild();
						if (path != null)
							group.setPath(path.getNodeValue());

						srcPath = doc.getElementsByTagName("srcPath").item(i).getFirstChild();
						if (srcPath != null)
							group.setSrcPath(srcPath.getNodeValue());

						filteredPackages = doc.getElementsByTagName("filteredPackages").item(i).getFirstChild();
						if (filteredPackages != null) {
							for (String filteredPackage : filteredPackages.getNodeValue().split(";")) {
								group.addFilteredPackage(filteredPackage);
							}
						}

						if (doc.getElementsByTagName("attribute").item(i) != null) {
							attribute = doc.getElementsByTagName("attribute").item(i).getFirstChild();
							if (attribute != null)
								group.setAttribute(attribute.getNodeValue());
						}
						this.groups.add(group);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} else {
				throw new CommandConfException("读取Group配置文件出错。");
			}
		} catch (CommandConfException e) {
			throw e;
		} catch (ParserConfigurationException e) {
			throw new CommandConfException(e);
		} catch (SAXException e) {
			throw new CommandConfException(e);
		} catch (IOException e) {
			throw new CommandConfException(e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException ignore) {
			}
		}
	}

	void delete(GroupConf group) throws CommandConfException {

		CommandConfRepository conf = new CommandConfRepository(group);

		if (conf.getCommandConfigurator().size() > 0) {
			throw new CommandConfException(group.getName(), null, "组下有命令，不能够删除。");
		}

		try {
			// 删除命令文件
			FileUtil.deleteFile(conf.getFilePath());
			// 删除组件文件
			FileUtil.deleteFile(getComponentFilePath(group.getName()));

			groups.remove(group);
			this.save();
		} catch (JDependException e) {
			throw new CommandConfException(group.getName(), null, e);
		}

	}

	void insert(GroupConf group) throws CommandConfException {

		if (!groups.contains(group)) {
			try {
				FileUtil.createFile(getCommandFilePath(group.getName()), new Properties());
				groups.add(group);
				this.save();
			} catch (JDependException e) {
				throw new CommandConfException(group.getName(), null, e);
			}
		} else {
			throw new CommandConfException(group.getName(), null, "组名重复。");
		}
	}

	void update(GroupConf group) throws CommandConfException {
		if (!groups.contains(group)) {
			throw new CommandConfException(group.getName(), null, "该组不存在。");
		} else {
			for (GroupConf obj : groups) {
				if (obj.equals(group)) {
					obj.setVisible(group.isVisible());
					obj.setPath(group.getPath());
					obj.setSrcPath(group.getSrcPath());
					obj.setFilteredPackages(group.getFilteredPackages());
					obj.setAttribute(group.getAttribute());
				}
			}
			this.save();
		}
	}

	private String getCommandFilePath(String group) {
		return this.filePath.substring(0, this.filePath.lastIndexOf("\\") + 1)
				+ CommandConfRepository.getDefaultPropertyFileName(group);
	}

	private String getComponentFilePath(String group) {
		return this.filePath.substring(0, this.filePath.lastIndexOf("\\") + 1) + "component_" + group + ".xml";
	}

	private void save() throws CommandConfException {

		FileOutputStream out = null;

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document.setXmlVersion("1.0");

			Element root = document.createElement("groups"); // 创建根节点
			document.appendChild(root); // 将根节点添加到Document对象中

			StringBuilder filteredPackages = null;

			for (GroupConf group : this.groups) {

				root.appendChild(document.createTextNode("\n	"));

				Element groupElement = document.createElement("group");

				Element nameElement = document.createElement("name");
				nameElement.setTextContent(group.getName());

				groupElement.appendChild(document.createTextNode("\n		"));
				groupElement.appendChild(nameElement);

				Element visibleElement = document.createElement("visible");
				visibleElement.setTextContent(Boolean.toString(group.isVisible()));

				groupElement.appendChild(document.createTextNode("\n		"));
				groupElement.appendChild(visibleElement);

				Element pathElement = document.createElement("path");
				pathElement.setTextContent(group.getPath());

				groupElement.appendChild(document.createTextNode("\n		"));
				groupElement.appendChild(pathElement);

				Element srcPathElement = document.createElement("srcPath");
				srcPathElement.setTextContent(group.getSrcPath());

				groupElement.appendChild(document.createTextNode("\n		"));
				groupElement.appendChild(srcPathElement);

				Element filteredPackagesElement = document.createElement("filteredPackages");
				filteredPackages = new StringBuilder();
				for (String filteredPackage : group.getFilteredPackages()) {
					filteredPackages.append(filteredPackage);
					filteredPackages.append(";");
				}
				if (filteredPackages.length() > 0) {
					filteredPackages.delete(filteredPackages.length() - 1, filteredPackages.length());
				}
				filteredPackagesElement.setTextContent(filteredPackages.toString());

				groupElement.appendChild(document.createTextNode("\n		"));
				groupElement.appendChild(filteredPackagesElement);

				Element attributeElement = document.createElement("attribute");
				attributeElement.setTextContent(group.getAttribute());

				groupElement.appendChild(document.createTextNode("\n		"));
				groupElement.appendChild(attributeElement);

				groupElement.appendChild(document.createTextNode("\n	"));
				root.appendChild(groupElement);
			}

			TransformerFactory transFactory = TransformerFactory.newInstance(); // 开始把Document映射到文件
			Transformer transFormer = transFactory.newTransformer();
			transFormer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource domSource = new DOMSource(document); // 设置输出结果

			File file = new File(this.filePath); // 生成xml文件

			if (!file.exists()) {
				file.createNewFile();
			}

			out = new FileOutputStream(file); // 文件输出流
			StreamResult xmlResult = new StreamResult(out); // 设置输入源

			transFormer.transform(domSource, xmlResult); // 输出xml文件

		} catch (Exception e) {
			throw new CommandConfException("命令组保存失败。", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	Collection<GroupConf> getGroupsConfigurator() {
		return groups;
	}

	void setGroupsConfigurator(Collection<GroupConf> groups) {
		this.groups = groups;
	}
}
