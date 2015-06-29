package jdepend.service.profile;

import jdepend.model.profile.ProfileFacade;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassRelationItemProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.service.profile.scope.ProfileScopeFacade;

public class ProfileFacadeImpl implements ProfileFacade {

	private String group;

	private String command;

	public ProfileFacadeImpl(String group, String command) {
		super();
		this.group = group;
		this.command = command;
	}

	@Override
	public AnalysisResultProfile getAnalysisResultProfile() {
		return ProfileScopeFacade.getInstance().getAnalysisResultProfile(group, command);
	}

	@Override
	public AreaComponentProfile getAreaComponentProfile() {
		return ProfileScopeFacade.getInstance().getAreaComponentProfile(group, command);
	}

	@Override
	public ComponentProfile getComponentProfile() {
		return ProfileScopeFacade.getInstance().getComponentProfile(group, command);
	}

	@Override
	public JavaClassUnitProfile getJavaClassUnitProfile() {
		return ProfileScopeFacade.getInstance().getJavaClassUnitProfile(group, command);
	}

	@Override
	public JavaClassRelationItemProfile getJavaClassRelationItemProfile() {
		return ProfileScopeFacade.getInstance().getJavaClassRelationItemProfile(group, command);
	}

}
