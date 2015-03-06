package jdepend.core.local.analyzer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnalyzerSummaryInfo {

	private String className;

	private String name;

	private String tip;

	private String userName;

	private Date createDate;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

}
