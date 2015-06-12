package jdepend.client.core.local.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.util.FileUtil;

/**
 * The <code>CommandConfRepository</code> class contains configuration
 * information contained in the <code>command.properties</code> file, if such a
 * file exists either in the user's home directory or somewhere in the
 * classpath.
 * 
 * @author <b>Abner</b>
 * 
 */

public class CommandConfRepository {

	private GroupConf group;

	private List<CommandConf> commandInfos = new ArrayList<CommandConf>();

	public static final String DEFAULT_PROPERTY_FILE = "command";

	private String filePath;

	/**
	 * Constructs a <code>CommandConfRepository</code> instance containing the
	 * properties specified in the file <code>command.properties</code>, if it
	 * exists.
	 * 
	 * @throws JDependException
	 */
	public CommandConfRepository(GroupConf group) throws CommandConfException {
		this.group = group;
		reload();
	}

	private void reload() throws CommandConfException {
		this.loadCommandInfo(getDefaultPropertyFile(group.getName()));
	}

	/**
	 * 得到所有命令信息
	 * 
	 * @return
	 */
	private List<CommandConf> getCommandConfigurator(Properties properties) {

		CommandConf info;

		String[] command;

		Enumeration e = properties.propertyNames();
		while (e.hasMoreElements()) {
			info = new CommandConf();
			info.label = (String) e.nextElement();
			try {
				command = properties.getProperty(info.label).split(",");
				info.group = group.getName();
				info.order = Integer.parseInt(command[0].trim());
				info.tip = command[1].trim();
				info.setPath(group.getPath());
				if (command.length > 2) {
					info.args = CommandConf.parseArgs(command[2].trim());
				}
				commandInfos.add(info);
			} catch (Exception ex) {
				LogUtil.getInstance(CommandConfRepository.class).systemError("Command " + info.label + " 配置有问题。");
				ex.printStackTrace();
			}
		}

		Collections.sort(commandInfos);

		return commandInfos;
	}

	public CommandConf getCommandInfo(String label) {
		if (label == null)
			return null;

		for (CommandConf info : commandInfos) {
			if (info.label.equals(label)) {
				return info;
			}
		}
		return null;
	}

	public static File getDefaultPropertyFile(String group) throws CommandConfException {

		if (group == null || group.length() == 0)
			throw new CommandConfException("组名不能为空！");

		String home = JDependContext.getWorkspacePath() + "\\" + GroupConfRepository.DEFAULT_PROPERTY_DIR;
		return new File(home, getDefaultPropertyFileName(group));
	}

	public static String getDefaultPropertyFileName(String group) {
		return DEFAULT_PROPERTY_FILE + "_" + group + ".xml";
	}

	private void loadCommandInfo(File file) throws CommandConfException {

		Properties p = new Properties();

		InputStream is = null;

		try {

			is = new FileInputStream(file);
			filePath = file.getParent() + "\\" + getDefaultPropertyFileName(group.getName());

		} catch (Exception e) {
			is = CommandConfRepository.class.getResourceAsStream("/" + getDefaultPropertyFileName(group.getName()));
			if (is == null) {
				is = CommandConfRepository.class.getResourceAsStream(getDefaultPropertyFileName(group.getName()));
				filePath = JDependContext.getRunningPath() + "\\classes\\"
						+ CommandConfRepository.class.getPackage().getName().replace('.', '\\') + "\\"
						+ getDefaultPropertyFileName(group.getName());
			} else {
				filePath = JDependContext.getRunningPath() + "\\classes\\"
						+ getDefaultPropertyFileName(group.getName());
			}
		}

		try {
			if (is != null) {
				p.loadFromXML(is);
			} else {
				throw new CommandConfException(group.getName(), null, "读取Commad配置文件出错。");
			}
		} catch (IOException ignore) {
			throw new CommandConfException(group.getName(), null, "读取Commad配置文件出错。", ignore);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException ignore) {
			}
		}

		commandInfos = getCommandConfigurator(p);
	}

	public void update(String oldlabel, CommandConf info) throws CommandConfException {

		this.validate(info);

		List<CommandConf> newcommandInfos = new ArrayList<CommandConf>();

		CommandConf oldInfo = new CommandConf();
		oldInfo.group = group.getName();
		oldInfo.label = oldlabel;

		for (CommandConf item : commandInfos) {
			if (item.equals(oldInfo)) {
				newcommandInfos.add(info);
			} else {
				newcommandInfos.add(item);
			}
		}

		List<CommandConf> oldCommandInfos = this.commandInfos;
		this.commandInfos = newcommandInfos;
		try {
			this.save();
		} catch (CommandConfException e) {
			this.commandInfos = oldCommandInfos;
			throw e;
		}
	}

	public void insert(CommandConf info) throws CommandConfException {

		this.validate(info);

		if (!commandInfos.contains(info)) {
			commandInfos.add(info);
			this.save();
		} else {
			throw new CommandConfException(info.group, info.label, "命令名称重复。");
		}
	}

	public void delete(String currentCommand) throws CommandConfException {

		CommandConf info = new CommandConf();
		info.group = group.getName();
		info.label = currentCommand;

		delete(info);

	}

	public void delete(CommandConf info) throws CommandConfException {

		commandInfos.remove(info);
		this.save();
	}

	private void save() throws CommandConfException {

		Properties content = new Properties();
		StringBuilder info;
		for (CommandConf item : commandInfos) {
			info = new StringBuilder(100);
			info.append(item.order);
			info.append(",");
			info.append(item.tip);
			if (item.args != null && item.args.length > 0) {
				info.append(",");
				info.append(item.getArgInfo());
			}
			content.put(item.label, info.toString());
		}
		try {
			FileUtil.saveFileContent(filePath, content);
		} catch (JDependException e) {
			throw new CommandConfException("命令保存失败。", e);
		}
	}

	private void validate(CommandConf info) throws CommandConfException {

		if (info.group == null || info.group.length() == 0) {
			throw new IllegalArgumentException("组名不能为空。");
		}

		if (info.label == null || info.label.length() == 0) {
			throw new IllegalArgumentException("名称不能为空。");
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public List<CommandConf> getCommandConfigurator() {
		return this.commandInfos;
	}

}
