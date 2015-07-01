package jdepend.service.profile;

import jdepend.model.profile.ProfileFacade;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassRelationItemProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.model.profile.model.RelationProfile;
import jdepend.service.profile.scope.ProfileScopeFacade;

public class ClientProfileFacadeImpl implements ProfileFacade {

	private String group;

	private String command;

	public ClientProfileFacadeImpl(String group, String command) {
		super();
		this.group = group;
		this.command = command;
	}

	@Override
	public AnalysisResultProfile getAnalysisResultProfile() {
		return ProfileScopeFacade.getInstance().getProfileFacade(group, command).getAnalysisResultProfile();
	}

	@Override
	public AreaComponentProfile getAreaComponentProfile() {
		return ProfileScopeFacade.getInstance().getProfileFacade(group, command).getAreaComponentProfile();
	}

	@Override
	public ComponentProfile getComponentProfile() {
		return ProfileScopeFacade.getInstance().getProfileFacade(group, command).getComponentProfile();
	}

	@Override
	public RelationProfile getRelationProfile() {
		return ProfileScopeFacade.getInstance().getProfileFacade(group, command).getRelationProfile();
	}

	@Override
	public JavaClassUnitProfile getJavaClassUnitProfile() {
		return ProfileScopeFacade.getInstance().getProfileFacade(group, command).getJavaClassUnitProfile();
	}

	@Override
	public JavaClassRelationItemProfile getJavaClassRelationItemProfile() {
		return ProfileScopeFacade.getInstance().getProfileFacade(group, command).getJavaClassRelationItemProfile();
	}

}
