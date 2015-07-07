package jdepend.model.profile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelationProfile implements Serializable {

	private static final long serialVersionUID = 6577779252907654777L;

	private Map<String, Integer> problemRelations;

	public static final String CycleDependAttentionType = "循环依赖权值";
	public static final String SDPAttentionType = "违反稳定依赖原则权值";
	public static final String ComponentLayerAttentionType = "下层组件依赖了上层组件权值";
	public static final String MutualDependAttentionType = "彼此依赖权值";

	public static List<String> getAllAttentionTypes() {
		List<String> allAttentionTypes = new ArrayList<String>();

		allAttentionTypes.add(CycleDependAttentionType);
		allAttentionTypes.add(SDPAttentionType);
		allAttentionTypes.add(ComponentLayerAttentionType);
		allAttentionTypes.add(MutualDependAttentionType);

		return allAttentionTypes;
	}

	public Map<String, Integer> getProblemRelations() {
		return problemRelations;
	}

	public void setProblemRelations(Map<String, Integer> problemRelations) {
		this.problemRelations = problemRelations;
	}
	
	public String getExplain() {
		StringBuilder info = new StringBuilder();
		info.append("在计算关系合理性指标的过程中需要给出不同问题关系类型的权值。权值越大表示这类问题关系越严重。\n\n");
		return info.toString();
	}
}
