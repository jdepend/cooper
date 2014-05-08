package jdepend.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.util.FileType;
import jdepend.framework.util.TargetFileManager;
import jdepend.model.Component;
import jdepend.model.JavaClassRelationType;
import jdepend.model.Metrics;

/**
 * 分析数据
 * 
 * @author wangdg
 * 
 */
public class AnalyseDataDTO implements Serializable {

	private List<String> directories = new ArrayList<String>();

	private List<byte[]> classes;// class文件信息

	private List<byte[]> configs;// 配置文件信息（如：ibats、spring、hibernate的xml信息）

	private Component component;

	private List<String> filteredPackages;

	private Map<String, Metrics> metricses = new HashMap<String, Metrics>();

	private Map<String, JavaClassRelationType> javaClassRelationTypes = new HashMap<String, JavaClassRelationType>();

	private Map<String, Collection<String>> targetFiles;

	public List<String> getDirectories() {
		return directories;
	}

	public void addDirectory(String directory) {
		if (!this.directories.contains(directory)) {
			this.directories.add(directory);
		}

	}

	public void calTargets() throws IOException {
		if (this.classes == null || this.configs == null) {
			TargetFileManager fileManager = new TargetFileManager();
			for (String dir : directories) {
				fileManager.addDirectory(dir);
			}
			Map<FileType, List<byte[]>> fileDatas = fileManager.getFileData();
			this.classes = fileDatas.get(FileType.classType);
			this.configs = fileDatas.get(FileType.xmlType);
			this.targetFiles = fileManager.getTargetFileGroupInfo();
		}
	}

	public Map<String, Collection<String>> getTargetFiles() {
		return targetFiles;
	}

	public void setTargetFiles(Map<String, Collection<String>> targetFiles) {
		this.targetFiles = targetFiles;
	}

	public String getPath() {
		if (this.directories == null || this.directories.size() == 0) {
			return null;
		}
		StringBuilder path = new StringBuilder();
		for (String dir : this.directories) {
			path.append(dir);
			path.append(";");
		}
		return path.toString();
	}

	public List<byte[]> getClasses() {
		return classes;
	}

	public List<byte[]> getConfigs() {
		return configs;
	}

	public void setClasses(List<byte[]> classes) {
		this.classes = classes;
	}

	public void setConfigs(List<byte[]> configs) {
		this.configs = configs;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public List<String> getFilteredPackages() {
		return filteredPackages;
	}

	public void setFilteredPackages(List<String> filteredPackages) {
		this.filteredPackages = filteredPackages;
	}

	public Map<String, Metrics> getMetricses() {
		return metricses;
	}

	public void setMetricses(Map<String, Metrics> metricses) {
		this.metricses = metricses;
	}

	public Map<String, JavaClassRelationType> getJavaClassRelationTypes() {
		return javaClassRelationTypes;
	}

	public void setJavaClassRelationTypes(
			Map<String, JavaClassRelationType> javaClassRelationTypes) {
		this.javaClassRelationTypes = javaClassRelationTypes;
	}

	@Override
	public String toString() {
		return "AnalyseData [classes=" + classes + ", component=" + component
				+ ", configs=" + configs + ", directories=" + directories
				+ ", filteredPackages=" + filteredPackages
				+ ", javaClassRelationTypes=" + javaClassRelationTypes
				+ ", metricses=" + metricses + "]";
	}
}
