package jdepend.knowledge.couplingtype;

import java.util.LinkedHashMap;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;

public class CouplingTypeIdentifyer {

	public Map<JavaClassRelationItem, CouplingType> identify(
			AnalysisResult result) throws JDependException {

		Map<JavaClassRelationItem, CouplingType> types = new LinkedHashMap<JavaClassRelationItem, CouplingType>();

		for (Relation relation : result.getRelations()) {
			for (JavaClassRelationItem item : relation.getItems()) {
				if (!item.isInner()) {

				}
			}
		}

		return types;

	}

}
