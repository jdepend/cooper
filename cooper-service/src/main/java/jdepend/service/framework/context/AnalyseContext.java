package jdepend.service.framework.context;

/**
 * 服务上下文
 * 
 * @author wangdg
 * 
 */
public final class AnalyseContext {

	private String group;
	private String command;
	private boolean isLocalRunning;
	private String client;
	private String userName;
	private long executeStartTime;
	private long executeEndTime;

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

	public boolean isLocalRunning() {
		return isLocalRunning;
	}

	public void setLocalRunning(boolean isLocalRunning) {
		this.isLocalRunning = isLocalRunning;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getExecuteStartTime() {
		return executeStartTime;
	}

	public void setExecuteStartTime(long executeStartTime) {
		this.executeStartTime = executeStartTime;
	}

	public long getExecuteEndTime() {
		return executeEndTime;
	}

	public void setExecuteEndTime(long executeEndTime) {
		this.executeEndTime = executeEndTime;
	}

}
