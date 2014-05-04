package jdepend.service.remote.analyzer;

import java.io.IOException;
import java.io.Serializable;

public final class AnalyzerDTO implements Serializable {

	private static final long serialVersionUID = -3859641911175386780L;

	private String name;
	private String tip;
	private String bigTip;
	private String type;

	private byte[] defaultData;
	private String className;
	private byte[] def;

	private String client;

	private String userName;

	public AnalyzerDTO() {
	}

	public AnalyzerDTO(AnalyzerInfo info) {
		this.def = info.getDef();
		this.defaultData = info.getDefaultData();
		this.className = info.getClassName();
		this.client = info.getClient();
		this.userName = info.getUserName();
	}

	public byte[] getDefaultData() {
		return defaultData;
	}

	public void setDefaultData(byte[] defaultData) {
		this.defaultData = defaultData;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public byte[] getDef() {
		return def;
	}

	public void setDef(byte[] def) {
		this.def = def;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AnalyzerInfo toInfo() throws ClassNotFoundException, IOException {
		AnalyzerInfo info = new AnalyzerInfo();

		info.setClassName(className);
		info.setClient(client);
		info.setDef(def);
		info.setDefaultData(defaultData);
		info.setUserName(userName);
		info.setName(name);
		info.setTip(tip);
		info.setType(type);
		info.setBigTip(bigTip);

		return info;
	}
}
