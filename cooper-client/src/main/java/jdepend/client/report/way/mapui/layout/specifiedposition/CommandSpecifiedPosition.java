package jdepend.client.report.way.mapui.layout.specifiedposition;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
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
		CommandSpecifiedPosition other = (CommandSpecifiedPosition) obj;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		return true;
	}
}
