package jdepend.core.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.LogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.util.FileUtil;
import jdepend.model.component.modelconf.ComponentConf;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.component.modelconf.ComponentModelConfMgr;
import jdepend.model.component.modelconf.GroupComponentModelConf;

/**
 * 命令组对象
 * 
 * 管理命令组及其涉及的命令配置信息
 * 
 * @author <b>Abner</b>
 * 
 */
public class GroupConf implements Cloneable {

	private static List<GroupConf> groupCommands;

	private String name;

	private String path;

	private String srcPath;

	private List<String> filteredPackages = new ArrayList<String>();

	private String attribute;

	private boolean visible = true;

	private List<CommandConf> commandInfos = new ArrayList<CommandConf>();

	private GroupComponentModelConf groupComponentModelConf;

	GroupConf(String name) {
		super();
		this.name = name;
		this.groupComponentModelConf = new GroupComponentModelConf(this.name,
				new HashMap<String, ComponentModelConf<ComponentConf>>());
	}

	GroupConf(String name, String path, String srcPath, List<String> filteredPackages, String attribute) {
		super();
		this.name = name;
		this.path = path;
		this.srcPath = srcPath;
		this.filteredPackages = filteredPackages;
		this.attribute = attribute;

		this.groupComponentModelConf = new GroupComponentModelConf(this.name,
				new HashMap<String, ComponentModelConf<ComponentConf>>());
	}

	/**
	 * 初始化所有命令组
	 * 
	 * @return
	 * @throws JDependException
	 */
	static List<GroupConf> init() throws CommandConfException {

		groupCommands = new ArrayList<GroupConf>();

		GroupConfRepository conf = new GroupConfRepository();

		ComponentModelConfMgr.getInstance().init();

		for (GroupConf group : conf.getGroupsConfigurator()) {
			try {
				// load commandInfo
				group.commandInfos = (new CommandConfRepository(group)).getCommandConfigurator();
				// 创建组件模型配置信息
				group.groupComponentModelConf = new GroupComponentModelConf(group.getName());
				// 增加组件模型配置信息
				ComponentModelConfMgr.getInstance().addComponentModelConf(group.groupComponentModelConf);

				groupCommands.add(group);
			} catch (JDependException e) {
				e.printStackTrace();
			}
		}

		return groupCommands;
	}

	void insert() throws CommandConfException {

		GroupConfRepository conf = new GroupConfRepository();
		conf.insert(this);
	}

	void insertAll() throws JDependException {

		insert();

		insertCommands();

		insertComponentGroups();

	}

	void update() throws CommandConfException {

		GroupConfRepository conf = new GroupConfRepository();
		conf.update(this);
	}

	void delete() throws CommandConfException {
		// 删除组件模型
		try {
			ComponentModelConfMgr.getInstance().deleteGroupComponentModelConf(name);
		} catch (JDependException e) {
			throw new CommandConfException(e);
		}
		// 删除命令组下的命令
		CommandConfRepository cconf = new CommandConfRepository(this);

		for (CommandConf command : commandInfos) {
			cconf.delete(command.label);
		}
		// 删除命令组
		GroupConfRepository conf = new GroupConfRepository();
		conf.delete(this);
	}

	void insertCommands() throws CommandConfException {

		CommandConfRepository cconf = new CommandConfRepository(this);

		for (CommandConf command : commandInfos) {
			cconf.insert(command);
		}
	}

	public void insertCommand(CommandConf command) throws CommandConfException {

		if (this.commandInfos.contains(command))
			return;

		if (command.order == 0) {
			int maxOrder = 0;
			for (CommandConf info : this.commandInfos) {
				if (info.order > maxOrder) {
					maxOrder = info.order;
				}
			}
			command.order = maxOrder + 1;
		}
		(new CommandConfRepository(this)).insert(command);
		this.commandInfos.add(command);
	}

	public void updateCommand(String oldlabel, CommandConf info) throws CommandConfException {
		(new CommandConfRepository(this)).update(oldlabel, info);

		for (CommandConf command : commandInfos) {
			if (command.label.equals(oldlabel)) {
				commandInfos.remove(info);
				commandInfos.add(info);
				break;
			}
		}
	}

	public void deleteCommand(String command) throws CommandConfException {

		CommandConf theInfo = new CommandConf();
		theInfo.group = name;
		theInfo.label = command;

		(new CommandConfRepository(this)).delete(theInfo);

		for (CommandConf info : commandInfos) {
			if (info.equals(theInfo)) {
				commandInfos.remove(info);
				break;
			}
		}
	}

	public CommandConf getCommandInfo(String label) {

		for (CommandConf command : commandInfos) {
			if (command.label.equals(label)) {
				return command;
			}
		}
		return null;
	}

	public CommandConf getCommandInfoByComponentGroup(String componentGroup) {

		for (CommandConf command : commandInfos) {
			if (command.args != null) {
				for (String arg : command.args) {
					if (arg.equals(componentGroup)) {
						return command;
					}
				}
			}
		}
		return null;
	}

	public StringBuilder getSrcContent(String className) throws CommandConfException {

		if (this.srcPath == null || this.srcPath.length() == 0) {
			return new StringBuilder().append("该命令组没有配置源文件路径！");
		}
		for (String src : this.srcPath.split(";")) {
			File file = new File(src);
			if (FileUtil.acceptCompressFile(file)) {
				try {
					String key = className.replace('.', '/');
					key = key.concat(".java");
					return FileUtil.getJarFileContent(new JarFile(file), key);
				} catch (IOException e) {
					LogUtil.getInstance(GroupConf.class).systemLog(src + "没有找到源文件.");
				}
			} else {
				String path = src + "//" + className.replace(".", "//") + ".java";
				String encode = (new PropertyConfigurator()).getJavaClassEncode();
				try {
					if (encode != null) {
						return FileUtil.readFileContent(path, encode);
					} else {
						return FileUtil.readFileContent(path, "GBK");
					}

				} catch (JDependException e) {
					LogUtil.getInstance(GroupConf.class).systemLog(src + "没有找到源文件.");
				}
			}
		}
		throw new CommandConfException(name, null, "源文件读取失败！");
	}

	public void insertComponentGroups() throws JDependException {
		this.groupComponentModelConf.save();
	}

	public ComponentModelConf<ComponentConf> getTheComponentModelConf(String name) {
		return this.groupComponentModelConf.getComponentModelConfs().get(name);
	}

	public Collection<String> getComponentModelConfNames() {
		return this.groupComponentModelConf.getComponentModelConfs().keySet();
	}

	public void setComponentModelConfs(Map<String, ComponentModelConf<ComponentConf>> components) {
		this.groupComponentModelConf.setComponentModelConfs(components);
	}

	public void addComponentModel(ComponentModelConf<ComponentConf> componentGroup) throws JDependException {
		this.groupComponentModelConf.addComponentModelConf(componentGroup);
		insertComponentGroups();
		BusiLogUtil.getInstance().businessLog(Operation.createComponentModel);
	}

	public void deleteComponentModel(String name) throws JDependException {
		this.groupComponentModelConf.getComponentModelConfs().remove(name);
		insertComponentGroups();
		BusiLogUtil.getInstance().businessLog(Operation.deleteComponentModel);
	}

	public GroupComponentModelConf getGroupComponentModelConf() {
		return groupComponentModelConf;
	}

	public String getPath() {
		return this.path;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CommandConf> getCommands() {
		return commandInfos;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setCommands(List<CommandConf> commands) {
		this.commandInfos = commands;
	}

	public List<String> getFilteredPackages() {
		return filteredPackages;
	}

	public void setFilteredPackages(List<String> filteredPackages) {
		this.filteredPackages = filteredPackages;
	}

	public void addFilteredPackage(String filteredPackage) {
		if (!this.filteredPackages.contains(filteredPackage)) {
			this.filteredPackages.add(filteredPackage);
		}
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		GroupConf groupCommand = new GroupConf(this.name);
		groupCommand.visible = this.visible;
		groupCommand.path = this.path;
		groupCommand.srcPath = this.srcPath;
		groupCommand.filteredPackages = this.filteredPackages;
		groupCommand.commandInfos = this.commandInfos;
		groupCommand.groupComponentModelConf = this.groupComponentModelConf;

		return groupCommand;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		final GroupConf other = (GroupConf) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
