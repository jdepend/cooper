package jdepend.framework.file;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeData implements Serializable {

	private static final long serialVersionUID = -2336923223962120301L;

	private Map<String, List<TargetFileInfo>> files = new HashMap<String, List<TargetFileInfo>>();

	private transient Map<String, List<TargetFileInfo>> configs;

	private transient Map<String, List<TargetFileInfo>> classes;

	private transient Integer placePos;

	public AnalyzeData() {
		super();
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
		if (this.configs == null) {
			this.classifyFiles();
		}
		return configs;
	}

	public Map<String, List<TargetFileInfo>> getClasses() {
		if (this.classes == null) {
			this.classifyFiles();
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

	private void classifyFiles() {
		configs = new HashMap<String, List<TargetFileInfo>>();
		classes = new HashMap<String, List<TargetFileInfo>>();
		for (String place : files.keySet()) {
			String smallPlace = getSmallPlace(place);
			configs.put(smallPlace, new ArrayList<TargetFileInfo>());
			classes.put(smallPlace, new ArrayList<TargetFileInfo>());
			for (TargetFileInfo targetFileInfo : files.get(place)) {
				if (targetFileInfo.getType().equals(TargetFileInfo.TYPE_XML)) {
					configs.get(smallPlace).add(targetFileInfo);
				} else if (targetFileInfo.getType().equals(TargetFileInfo.TYPE_CLASS)) {
					classes.get(smallPlace).add(targetFileInfo);
				}
			}
		}
	}

	private String getSmallPlace(String place) {
		if (this.placePos == null) {
			Collection<String> places = new ArrayList<String>(files.keySet());
			Collection<String[]> placeSegments = new ArrayList<String[]>();
			for (String place1 : places) {
				placeSegments.add(place1.split("\\\\"));
			}
			int count = 0;
			int pos = 0;
			L: for (String[] placeSegment1 : placeSegments) {
				for (String segment1 : placeSegment1) {
					for (String[] placeSegment2 : placeSegments) {
						if (placeSegment2.length < count) {
							break L;
						}
						String segment2 = placeSegment2[count];
						if (!segment1.equals(segment2)) {
							break L;
						}
					}
					count++;
					pos += segment1.length() + 1;
				}
			}
			this.placePos = pos;
		}
		return place.substring(this.placePos);

	}

}
