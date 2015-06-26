package jdepend.model.profile.model;

import java.io.Serializable;
import java.util.Map;

public class RelationProfile implements Serializable {

	private static final long serialVersionUID = 6577779252907654777L;
	
	private Map<String, Float> problemRelations;

	public Map<String, Float> getProblemRelations() {
		return problemRelations;
	}

	public void setProblemRelations(Map<String, Float> problemRelations) {
		this.problemRelations = problemRelations;
	}
}
