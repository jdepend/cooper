package jdepend.report.way.mapui.layout.specifiedposition;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.domain.PersistentBean;

public class SpecifiedPositionMgr extends PersistentBean {

	private List<CommandSpecifiedPosition> commandSpecifiedPositions;

	private final static SpecifiedPositionMgr mgr = new SpecifiedPositionMgr();

	private SpecifiedPositionMgr() {
		super("关系位置管理器", "关系位置管理器", PropertyConfigurator.DEFAULT_PROPERTY_DIR);

		if (commandSpecifiedPositions == null) {
			commandSpecifiedPositions = new ArrayList<CommandSpecifiedPosition>();
		}
	}

	public static SpecifiedPositionMgr getInstance() {
		return mgr;
	}

	public List<CommandSpecifiedPosition> getCommandSpecifiedPositions() {
		return commandSpecifiedPositions;
	}

	public void setCommandSpecifiedPositions(List<CommandSpecifiedPosition> commandSpecifiedPositions) {
		this.commandSpecifiedPositions = commandSpecifiedPositions;
	}

	public CommandSpecifiedPosition getTheCommandSpecifiedPosition(String group, String command) {
		for (CommandSpecifiedPosition commandSpecifiedPosition : commandSpecifiedPositions) {
			if (commandSpecifiedPosition.getGroup().equals(group)
					&& commandSpecifiedPosition.getCommand().equals(command)) {
				return commandSpecifiedPosition;
			}
		}
		return null;
	}

	public boolean isHaveCommandSpecifiedPosition(String group, String command) {
		return this.getTheCommandSpecifiedPosition(group, command) != null;
	}
}
