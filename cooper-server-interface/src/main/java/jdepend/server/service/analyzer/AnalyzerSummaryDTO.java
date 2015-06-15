package jdepend.server.service.analyzer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnalyzerSummaryDTO implements Serializable {

	private static final long serialVersionUID = -5774152893628995546L;

	private String className;

	private String name;

	private String tip;

	private String bigTip;

	private String type;

	private String client;

	private String userName;

	private Date createDate;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getBigTip() {
		return bigTip;
	}

	public void setBigTip(String bigTip) {
		this.bigTip = bigTip;
	}

	public Date getCreateDate() {
		return new Date(this.createDate.getTime()) {
			public String toString() {
				return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(this);
			}
		};

	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
