package jdepend.model.profile.model;

import java.io.Serializable;
import java.util.Map;

public class RelationProfile implements Serializable {

	private static final long serialVersionUID = 6577779252907654777L;

	private Map<String, Integer> problemRelations;

	private Float SDPDifference;//判断稳定的组件依赖不稳定组件的阈值（违反稳定依赖原则的阈值）

	public Map<String, Integer> getProblemRelations() {
		return problemRelations;
	}

	public void setProblemRelations(Map<String, Integer> problemRelations) {
		this.problemRelations = problemRelations;
	}

	public Float getSDPDifference() {
		return SDPDifference;
	}

	public void setSDPDifference(Float sDPDifference) {
		SDPDifference = sDPDifference;
	}

	public String getExplain() {
		StringBuilder info = new StringBuilder();
		info.append("在计算关系合理性指标的过程中需要给出不同问题关系类型的权值。权值越大表示这类问题关系越严重。\n\n");
		info.append("循环依赖的含义是：该关系处于循环依赖链上，并且强度最弱。\n\n");
		info.append("违反稳定依赖原则的阈值是在判断稳定的组件依赖不稳定组件时的稳定性指标达到的差值临界点。该阈值为0时表示只要稳定性指标数值小于被依赖的组件就违反原则了。\n\n");
		return info.toString();
	}
}
