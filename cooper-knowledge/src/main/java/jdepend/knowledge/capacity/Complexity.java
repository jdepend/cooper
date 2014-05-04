package jdepend.knowledge.capacity;

import jdepend.framework.util.MetricsFormat;
import jdepend.model.result.AnalysisResult;

public class Complexity {

	private int relations;

	private int components;

	private int classes;

	public Complexity(AnalysisResult result) {
		this.relations = result.getRelations().size();
		this.components = result.getComponents().size();
		this.classes = result.getClasses().size();
	}

	public float getValue() {
		return MetricsFormat.toFormattedScore(this.relations * 0.75F + this.components * 0.2F + this.classes * 0.05F);
	}

	public int getRelations() {
		return relations;
	}

	public int getComponents() {
		return components;
	}

	public int getClasses() {
		return classes;
	}

	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("关系数量：");
		info.append(this.relations);
		info.append("\n");

		info.append("组件数量：");
		info.append(this.components);
		info.append("\n");

		info.append("类数量：");
		info.append(this.classes);
		info.append("\n");

		info.append("复杂度：");
		info.append(this.getValue());
		info.append("\n");

		return info.toString();
	}

}
