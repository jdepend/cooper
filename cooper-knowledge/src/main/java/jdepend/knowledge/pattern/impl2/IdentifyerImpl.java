package jdepend.knowledge.pattern.impl2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.model.JavaClassUnit;

public class IdentifyerImpl implements Identifyer {

	private String name;

	private List<String> featureNames = new ArrayList<String>();

	private Map<JavaClassUnit, List<String>> javaClasses = new LinkedHashMap<JavaClassUnit, List<String>>();

	private Map<JavaClassUnit, String> patternInfos = new HashMap<JavaClassUnit, String>();

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

	public Map<JavaClassUnit, String> getResult() {
		Map<JavaClassUnit, String> results = new LinkedHashMap<JavaClassUnit, String>();
		for (JavaClassUnit javaClass : this.javaClasses.keySet()) {
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
		this.javaClasses = new LinkedHashMap<JavaClassUnit, List<String>>();
		this.patternInfos = new HashMap<JavaClassUnit, String>();
	}
}
