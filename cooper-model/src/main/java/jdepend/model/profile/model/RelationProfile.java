package jdepend.model.profile.model;

import java.io.Serializable;
import java.util.Map;

public class RelationProfile implements Serializable {

	private static final long serialVersionUID = 6577779252907654777L;

	private Map<String, Integer> problemRelations;

	public Map<String, Integer> getProblemRelations() {
		return problemRelations;
	}

	public void setProblemRelations(Map<String, Integer> problemRelations) {
		this.problemRelations = problemRelations;
	}

	public String getExplain() {
		StringBuilder info = new StringBuilder();
		info.append("在计算关系合理性指标的过程中需要给出不同问题关系类型的权值。权值越大表示这类问题关系越严重。\n\n");
		info.append("循环依赖的含义是：该关系处于循环依赖链上，并且强度最弱。\n\n");
		return info.toString();
	}
}
