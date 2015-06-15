package jdepend.service.local.impl;

/**
 * 服务上下文
 * 
 * @author wangdg
 * 
 */
public final class AnalyseContext {

	private boolean isLocalRunning;
	private String client;
	private String userName;
	private long executeStartTime;
	private long executeEndTime;

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
