package jdepend.knowledge.couplingtype;

import java.util.LinkedHashMap;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;

public class CouplingTypeIdentifyer {

	public Map<JavaClassRelationItem, CouplingType> identify(AnalysisResult result) throws JDependException {

		Map<JavaClassRelationItem, CouplingType> types = new LinkedHashMap<JavaClassRelationItem, CouplingType>();

		for (Relation relation : result.getRelations()) {
			for (JavaClassRelationItem item : relation.getItems()) {
				if (!JavaClassUnitUtil.isInner(item)) {

				}
			}
		}

		return types;

	}

}
