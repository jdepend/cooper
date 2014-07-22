package jdepend.framework.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeData implements Serializable {

	private static final long serialVersionUID = -2336923223962120301L;

	private Map<String, List<TargetFileInfo>> files = new HashMap<String, List<TargetFileInfo>>();

	public AnalyzeData() {
		super();
	}

	public Map<String, Collection<String>> getFileNames() {
		Map<String, Collection<String>> fileNames = new HashMap<String, Collection<String>>();
		for (String place : files.keySet()) {
			fileNames.put(place, new ArrayList<String>());
			for (TargetFileInfo targetFileInfo : files.get(place)) {
				fileNames.get(place).add(targetFileInfo.getName());
			}
		}
		return fileNames;
	}

	public void addFileInfo(String place, TargetFileInfo targetFileInfo) {
		if (!files.containsKey(place)) {
			files.put(place, new ArrayList<TargetFileInfo>());
		}
		files.get(place).add(targetFileInfo);
	}

	public String getPath() {
		StringBuilder path = new StringBuilder();
		for (String fileName : files.keySet()) {
			path.append(fileName);
			path.append(";");
		}
		if (path.length() > 0) {
			path.delete(path.length() - 1, path.length());
		}
		return path.toString();
	}

	public Map<String, List<TargetFileInfo>> getConfigs() {
		Map<String, List<TargetFileInfo>> configs = new HashMap<String, List<TargetFileInfo>>();
		for (String place : files.keySet()) {
			configs.put(place, new ArrayList<TargetFileInfo>());
			for (TargetFileInfo targetFileInfo : files.get(place)) {
				if (targetFileInfo.getType().equals(TargetFileInfo.TYPE_XML)) {
					configs.get(place).add(targetFileInfo);
				}
			}
		}
		return configs;
	}

	public Map<String, List<TargetFileInfo>> getClasses() {
		Map<String, List<TargetFileInfo>> classes = new HashMap<String, List<TargetFileInfo>>();
		for (String place : files.keySet()) {
			classes.put(place, new ArrayList<TargetFileInfo>());
			for (TargetFileInfo targetFileInfo : files.get(place)) {
				if (targetFileInfo.getType().equals(TargetFileInfo.TYPE_CLASS)) {
					classes.get(place).add(targetFileInfo);
				}
			}
		}
		return classes;
	}

	public int getClassesCount() {
		int count = 0;
		for (String place : getClasses().keySet()) {
			count += getClasses().get(place).size();
		}
		return count;
	}

}
