package jdepend.service.profile.scope;

import jdepend.model.profile.model.defaultvalue.DefaultAnalysisResultProfile;
import jdepend.model.result.ProfileFacadeImpl;

public class DefaultWorkspaceProfileScope extends WorkspaceProfileScope {

	private static final long serialVersionUID = 7041687926833059961L;

	public DefaultWorkspaceProfileScope() {
		super();

		ProfileFacadeImpl profileFacade = new ProfileFacadeImpl();
		profileFacade.setAnalysisResultProfile(new DefaultAnalysisResultProfile());

		this.setProfileFacade(profileFacade);
	}
}
