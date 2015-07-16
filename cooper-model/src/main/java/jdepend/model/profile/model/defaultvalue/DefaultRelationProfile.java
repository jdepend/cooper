package jdepend.model.profile.model.defaultvalue;

import java.util.HashMap;
import java.util.Map;

import jdepend.model.Relation;
import jdepend.model.profile.model.RelationProfile;

public class DefaultRelationProfile extends RelationProfile {

	public DefaultRelationProfile() {

		Map<String, Integer> problemRelations = new HashMap<String, Integer>();

		problemRelations.put(Relation.CycleDependAttentionType, 1);
		problemRelations.put(Relation.SDPAttentionType, 2);
		problemRelations.put(Relation.ComponentLayerAttentionType, 3);
		problemRelations.put(Relation.MutualDependAttentionType, 4);

		this.setProblemRelations(problemRelations);
		
		this.setSDPDifference(0.1F);

	}
}
