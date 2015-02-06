package jdepend.service.remote;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.file.AnalyzeData;
import jdepend.framework.file.TargetFileManager;
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

	private AnalyzeData data;

	private Component component;

	private List<String> filteredPackages;

	public List<String> getDirectories() {
		return directories;
	}

	public void addDirectory(String directory) {
		if (!this.directories.contains(directory)) {
			this.directories.add(directory);
		}

	}

	public AnalyzeData getAnalyzeData() {
		return data;
	}

	public void setAnalyzeData(AnalyzeData fileDatas) {
		this.data = fileDatas;
	}

	public void calAnalyzeData() throws IOException {
		if (this.data == null) {
			TargetFileManager fileManager = new TargetFileManager();
			for (String dir : directories) {
				fileManager.addDirectory(dir);
			}
			this.data = fileManager.getAnalyzeData();
		}
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

	@Override
	public String toString() {
		return "AnalyseData [classes=" + data.getClasses().size() + ", component=" + component + ", configs="
				+ data.getConfigs().size() + ", directories=" + directories + ", filteredPackages=" + filteredPackages
				+ "]";
	}
}
