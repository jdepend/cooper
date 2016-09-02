package jdepend.knowledge.domainanalysis;

import java.io.Serializable;

import jdepend.model.result.AnalysisResult;

public final class Structure implements Serializable {

	private StructureCategory category;

	private String name;

	private AnalysisResult data;

	public StructureCategory getCategory() {
		return category;
	}

	public void setCategory(StructureCategory category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AnalysisResult getData() {
		return data;
	}

	public void setData(AnalysisResult data) {
		this.data = data;
	}

}
