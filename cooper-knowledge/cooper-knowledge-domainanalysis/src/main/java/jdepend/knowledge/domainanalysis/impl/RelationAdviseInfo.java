package jdepend.knowledge.domainanalysis.impl;

import jdepend.knowledge.domainanalysis.AdviseInfo;
import jdepend.model.Relation;

public class RelationAdviseInfo extends AdviseInfo {

	private String current;
	
	private String jdepend;

	public RelationAdviseInfo(Relation relation){
		this.current = relation.getCurrent().getName();
		this.jdepend = relation.getDepend().getName();
	}

	@Override
	protected String calInfo() {
		StringBuilder info = new StringBuilder();
		info.append(this.getDesc());
		info.append(this.getRelationInfo());
		return info.toString();
	}
	
	private String getRelationInfo(){
		return "(" + this.current + "->" + this.jdepend + ")";
	}

}
