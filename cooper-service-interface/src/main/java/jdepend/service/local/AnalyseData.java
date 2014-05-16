package jdepend.service.local;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.parse.impl.ParseData;

public class AnalyseData {

	private List<byte[]> classes;

	private List<byte[]> configs;

	private Map<String, Collection<String>> targetFiles;

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

	public Map<String, Collection<String>> getTargetFiles() {
		return targetFiles;
	}

	public void setTargetFiles(Map<String, Collection<String>> targetFiles) {
		this.targetFiles = targetFiles;
	}

	public ParseData toParseData() {
		ParseData parseData = new ParseData();

		parseData.setClasses(this.getClasses());
		parseData.setConfigs(this.getConfigs());

		return parseData;
	}
}
