package jdepend.report.filter;

import jdepend.model.Relation;

public class RelationFilter extends ReportFilter {

	public boolean isRelationDetail() {
		if (switchInfo.size() == 0)
			return false;

		return "true".equals(switchInfo.get("RelationDetail")) || "1".equals(switchInfo.get("RelationDetail"))
				|| "Y".equalsIgnoreCase((String) switchInfo.get("RelationDetail"));
	}

	public boolean isTheRelationDetail(Relation r) {
		if (switchInfo.size() == 0)
			return true;

		String theRelation = (String) switchInfo.get("TheRelation");
		if (theRelation == null || "".equals(theRelation))
			return true;

		String[] relations = theRelation.split(";");
		String[] elements = null;
		for (int i = 0; i < relations.length; i++) {
			elements = relations[i].trim().split(",");
			if (elements == null || elements.length != 2)
				return true;

			if (r.getCurrent().getName().equals(elements[0].trim())
					&& r.getDepend().getName().equals(elements[1].trim())) {
				return true;
			}
		}
		return false;

	}

	public boolean isRelationIntensity() {
		if (switchInfo.size() == 0)
			return false;

		return "true".equals(switchInfo.get("RelationIntensity")) || "1".equals(switchInfo.get("RelationIntensity"))
				|| "Y".equalsIgnoreCase((String) switchInfo.get("RelationIntensity"));
	}

	public boolean isElementIntensity() {
		if (switchInfo.size() == 0)
			return false;

		return "true".equals(switchInfo.get("ElementIntensity")) || "1".equals(switchInfo.get("ElementIntensity"))
				|| "Y".equalsIgnoreCase((String) switchInfo.get("ElementIntensity"));
	}

	public boolean isMoveCenter() {
		if (switchInfo.size() == 0)
			return false;

		return "true".equals(switchInfo.get("MoveCenter")) || "1".equals(switchInfo.get("MoveCenter"))
				|| "Y".equalsIgnoreCase((String) switchInfo.get("MoveCenter"));
	}

}
