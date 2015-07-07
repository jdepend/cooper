package jdepend.model.profile.model.defaultvalue;

import java.util.HashMap;
import java.util.Map;

import jdepend.model.profile.model.RelationProfile;

public class DefaultRelationProfile extends RelationProfile {

	public DefaultRelationProfile() {

		Map<String, Integer> problemRelations = new HashMap<String, Integer>();

		problemRelations.put(CycleDependAttentionType, 1);
		problemRelations.put(SDPAttentionType, 2);
		problemRelations.put(ComponentLayerAttentionType, 3);
		problemRelations.put(MutualDependAttentionType, 4);

		this.setProblemRelations(problemRelations);

	}
}
