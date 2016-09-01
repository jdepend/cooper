package jdepend.framework.file.gather;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TargetFiles {

	private Map<String, List<File>> files = new HashMap<String, List<File>>();

	public TargetFiles() {
		super();
	}
	
	public void addPlace(String place) {
		this.files.put(place, new ArrayList<File>());
	}

	public void addFile(String place, File file) {
		this.files.get(place).add(file);
	}

	public Map<String, List<File>> getFiles() {
		return files;
	}

	public void setFiles(Map<String, List<File>> files) {
		this.files = files;
	}

	public List<File> getAllFiles() {
		List<File> allFiles = new ArrayList<File>();
		for (String place : files.keySet()) {
			for (File file : files.get(place)) {
				allFiles.add(file);
			}
		}
		return allFiles;
	}

}
