package jdepend.service.remote;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Session
 * 
 * @author wangdg
 * 
 */
public class JDependSession {

	private long id;

	private String client;

	private Date createTime;

	private int analyzeSchedule;

	private String userName;

	public JDependSession() {
		this.createTime = Calendar.getInstance().getTime();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public synchronized int getAnalyzeSchedule() {
		return analyzeSchedule;
	}

	public synchronized void appendAnalyzeSchedule(int increment) {
		this.analyzeSchedule += increment;
	}

	public synchronized void clearAnalyzeSchedule() {
		this.analyzeSchedule = 0;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		final JDependSession other = (JDependSession) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder(100);
		content.append(this.id);
		content.append("    IP地址：");
		content.append(this.client);
		content.append("    登陆用户：");
		content.append(this.userName);
		content.append("    创建时间：");
		content.append((new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(this.createTime));

		return content.toString();
	}
}
