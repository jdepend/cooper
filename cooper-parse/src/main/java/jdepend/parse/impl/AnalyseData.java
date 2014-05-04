package jdepend.parse.impl;

import java.util.List;

public class AnalyseData {

	private List<byte[]> classes;

	private List<byte[]> configs;

	public List<byte[]> getClasses() {
		return classes;
	}

	public void setClasses(List<byte[]> classes) {
		this.classes = classes;
	}

	public List<byte[]> getConfigs() {
		return configs;
	}

	public void setConfigs(List<byte[]> configs) {
		this.configs = configs;
	}

}
