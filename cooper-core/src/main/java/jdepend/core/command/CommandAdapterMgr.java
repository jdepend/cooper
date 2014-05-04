package jdepend.core.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.core.config.CommandConf;
import jdepend.core.config.CommandConfMgr;
import jdepend.core.config.GroupConf;
import jdepend.core.config.GroupConfChangeListener;
import jdepend.framework.exception.JDependException;
import jdepend.model.util.ClassSearchUtil;

/**
 * 命令管理器
 * 
 * 管理CommandAdapter集合
 * 
 * @author <b>Abner</b>
 * 
 */
public final class CommandAdapterMgr implements GroupConfChangeListener {

	private static CommandAdapterMgr mgr;

	// 命名 group label adapter
	private Map<String, LinkedHashMap<String, CommandAdapter>> commands = new LinkedHashMap<String, LinkedHashMap<String, CommandAdapter>>();

	private static String currentGroup;

	private static String currentCommand;

	private CommandAdapterMgr() throws JDependException {
		this.refresh();
	}

	public void refresh() throws JDependException {

		CommandAdapter adapter;
		LinkedHashMap<String, CommandAdapter> commandAdapters;

		commands = new LinkedHashMap<String, LinkedHashMap<String, CommandAdapter>>();

		for (GroupConf group : CommandConfMgr.getInstance().getGroups()) {

			Collection<CommandConf> commandInfos = group.getCommands();

			for (CommandConf info : commandInfos) {
				try {
					// 构建adapter
					adapter = new CommandAdapter(info);
					// 缓存adapter
					commandAdapters = commands.get(info.group);
					if (commandAdapters == null) {
						commandAdapters = new LinkedHashMap<String, CommandAdapter>();
						commands.put(info.group, commandAdapters);
					}
					commandAdapters.put(adapter.getLabel(), adapter);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}

	}

	public CommandAdapter getTheCommandAdapter(String group, String command) {
		return this.commands.get(group).get(command);
	}

	public static CommandAdapterMgr getInstance() throws JDependException {
		if (mgr == null) {
			mgr = new CommandAdapterMgr();
		}
		return mgr;
	}

	public static String getCurrentGroup() {
		return currentGroup;
	}

	public static void setCurrentGroup(String group) {
		currentGroup = group;
	}

	public static String getCurrentCommand() {
		return currentCommand;
	}

	public static void setCurrentCommand(String command) {
		currentCommand = command;
	}

	public Map<String, CommandAdapter> getCommands(String group) {
		return commands.get(group);
	}

	@Override
	public void onCreate(String group) throws JDependException {
	}

	@Override
	public void onDelete(String group) throws JDependException {
	}

	@Override
	public void onUpdate(String group) throws JDependException {
	}

	@Override
	public void onRefresh() throws JDependException {
		this.refresh();
	}
}
