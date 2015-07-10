package jdepend.service.profile;

import jdepend.metadata.profile.JavaClassRelationItemProfile;
import jdepend.model.profile.ProfileFacade;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.model.profile.model.RelationProfile;
import jdepend.service.profile.scope.ProfileScopeFacade;

public class ClientProfileFacadeImpl implements ProfileFacade {

	private ProfileFacade profileFacade;

	public ClientProfileFacadeImpl(String group, String command) {
		super();
		this.profileFacade = ProfileScopeFacade.getInstance().getProfileFacade(group, command);
	}

	@Override
	public AnalysisResultProfile getAnalysisResultProfile() {
		return this.profileFacade.getAnalysisResultProfile();
	}

	@Override
	public AreaComponentProfile getAreaComponentProfile() {
		return this.profileFacade.getAreaComponentProfile();
	}

	@Override
	public ComponentProfile getComponentProfile() {
		return this.profileFacade.getComponentProfile();
	}

	@Override
	public RelationProfile getRelationProfile() {
		return this.profileFacade.getRelationProfile();
	}

	@Override
	public JavaClassUnitProfile getJavaClassUnitProfile() {
		return this.profileFacade.getJavaClassUnitProfile();
	}

	@Override
	public JavaClassRelationItemProfile getJavaClassRelationItemProfile() {
		return this.profileFacade.getJavaClassRelationItemProfile();
	}

}
