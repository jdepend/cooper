package jdepend.knowledge.pattern.impl2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.model.JavaClass;

public class IdentifyerImpl implements Identifyer {

	private String name;

	private List<String> featureNames = new ArrayList<String>();

	private Map<JavaClass, List<String>> javaClasses = new LinkedHashMap<JavaClass, List<String>>();

	private Map<JavaClass, String> patternInfos = new HashMap<JavaClass, String>();

	public IdentifyerImpl(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void accumulate(Feature feature) {
		// 增加符合特性的类
		if (!this.javaClasses.containsKey(feature.getCurrent())) {
			this.javaClasses.put(feature.getCurrent(), new ArrayList<String>());
		}
		this.javaClasses.get(feature.getCurrent()).add(feature.getName());
		// 设置最终输出结果
		if (feature.getPatternInfo() != null) {
			this.patternInfos.put(feature.getCurrent(), feature.getPatternInfo());
		}
	}

	public void registFeature(String featureName) {
		if (!this.featureNames.contains(featureName)) {
			this.featureNames.add(featureName);
		}
	}

	public Map<JavaClass, String> getResult() {
		Map<JavaClass, String> results = new LinkedHashMap<JavaClass, String>();
		for (JavaClass javaClass : this.javaClasses.keySet()) {
			if (this.javaClasses.get(javaClass).size() == featureNames.size()) {
				results.put(javaClass, this.patternInfos.get(javaClass));
			}
		}
		return results;
	}

	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>" + this.name + "</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;");
		for (String featureName : this.featureNames) {
			explain.append(featureName);
			explain.append("  ");
		}
		explain.append("<br><br>");
		return explain.toString();
	}

	@Override
	public void clear() {
		this.javaClasses = new LinkedHashMap<JavaClass, List<String>>();
		this.patternInfos = new HashMap<JavaClass, String>();
	}
}
