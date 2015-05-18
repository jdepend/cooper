package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;

public class TODOIdentifyInfo {

	private AnalysisResult result;

	private List<TODORelationData> relationDatas;

	public TODOIdentifyInfo(AnalysisResult result) {
		super();
		this.result = result;

		this.relationDatas = new ArrayList<TODORelationData>();
		for (Relation relation : this.result.getRelations()) {
			this.relationDatas.add(new TODORelationData(relation));
		}
	}

	public AnalysisResult getResult() {
		return result;
	}

	public List<TODORelationData> getRelationDatas() {
		return relationDatas;
	}

	public TODORelationData getRelationData(Relation relation) {
		for (TODORelationData relationData : relationDatas) {
			if (relationData.getRelation().equals(relation)) {
				return relationData;
			}
		}
		return null;
	}
}
