package jdepend.ui.circle.domain;

/**
 * 
 * @author ce
 */
public class UsersVo {

	private String userName; // 用户名
	private String alias; // 别名
	private String groupName; // 工作组名
	private String ip; // IP地址
	private String hostName; // 主机名

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAlias() {
		if (alias == null || "".equals(alias))
			return userName;
		else
			return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * 讲数据包转换为用户列表VO
	 * 
	 * @param dp
	 * @return
	 */
	public static UsersVo changeDataPacket(DataPacket dp) {
		UsersVo uv = new UsersVo();
		uv.setUserName(dp.getSenderName());
		uv.setHostName(dp.getSenderHost());
		String[] buff = dp.getAdditional().split("\0");
		if (buff.length >= 2) {
			uv.setAlias(buff[0]);
			uv.setGroupName(buff[1]);
		}
		uv.setIp(dp.getIp());
		return uv;
	}

	public String[] toArray() {
		return new String[] { getAlias(), groupName, hostName, ip };
	}

}
