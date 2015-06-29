package jdepend.service.profile.scope;

import jdepend.model.profile.model.defaultvalue.DefaultAnalysisResultProfile;


public class DefaultWorkspaceProfileScope extends WorkspaceProfileScope {

	private static final long serialVersionUID = 7041687926833059961L;

	public DefaultWorkspaceProfileScope() {
		super();
		
		this.setAnalysisResultProfile(new DefaultAnalysisResultProfile());
	}
}
