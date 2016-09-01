package jdepend.server.service.session;

import java.io.Serializable;

/**
 * request
 * 
 * @author wangdg
 * 
 */
public class JDependRequest implements Serializable {

	private long sessionId;

	private String groupName;

	private String commandName;

	public JDependRequest(long sessionId, String groupName, String commandName) {
		this.sessionId = sessionId;
		this.groupName = groupName;
		this.commandName = commandName;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public long getSessionId() {
		return sessionId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return commandName;
	}

}
