package jdepend.framework.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

class ThreadSecurityTargetFiles {

	private Map<String, List<File>> files;

	public ThreadSecurityTargetFiles() {
		super();
		this.files = new Hashtable<String, List<File>>();
	}

	public void addPlace(String place) {
		this.files.put(place, new Vector<File>());
	}

	public void addFile(String place, File file) {
		this.files.get(place).add(file);
	}

	public TargetFiles toTargetFiles() {

		TargetFiles targetFiles = new TargetFiles();

		Map<String, List<File>> tempFiles = new HashMap<String, List<File>>();
		List<File> fileInfos;
		for (String place : files.keySet()) {
			fileInfos = new ArrayList<File>(files.get(place));
			tempFiles.put(place, fileInfos);
		}
		targetFiles.setFiles(tempFiles);

		return targetFiles;
	}
}
