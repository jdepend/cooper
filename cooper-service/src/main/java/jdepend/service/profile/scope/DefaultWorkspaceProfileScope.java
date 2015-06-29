package jdepend.service.profile.scope;

import jdepend.model.profile.model.AnalysisResultProfile;


public class DefaultWorkspaceProfileScope extends WorkspaceProfileScope {

	private static final long serialVersionUID = 7041687926833059961L;

	public DefaultWorkspaceProfileScope() {
		super();
		
		AnalysisResultProfile analysisResultProfile = new AnalysisResultProfile(); 
		
		analysisResultProfile.setBalance(25F);
		analysisResultProfile.setDistance(25F);
		analysisResultProfile.setEncapsulation(25F);
		analysisResultProfile.setRelationRationality(25F);
		
		this.setAnalysisResultProfile(analysisResultProfile);
		
		
		
	}
}
