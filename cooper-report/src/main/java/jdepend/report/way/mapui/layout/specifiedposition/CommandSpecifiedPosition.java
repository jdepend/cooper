package jdepend.report.way.mapui.layout.specifiedposition;

import java.io.Serializable;
import java.util.List;

public class CommandSpecifiedPosition implements Serializable {

	private static final long serialVersionUID = 1110859050242265542L;

	private String group;

	private String command;

	private List<SpecifiedNodePosition> nodePositions;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<SpecifiedNodePosition> getNodePositions() {
		return nodePositions;
	}

	public void setNodePositions(List<SpecifiedNodePosition> nodePositions) {
		this.nodePositions = nodePositions;
	}

}
