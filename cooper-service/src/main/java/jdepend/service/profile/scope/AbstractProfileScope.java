package jdepend.service.profile.scope;

import jdepend.model.profile.ProfileFacade;

public abstract class AbstractProfileScope implements ProfileScope {

	private ProfileFacade profileFacade;

	public abstract boolean isSelf(String group, String command);

	public ProfileFacade getProfileFacade() {
		return profileFacade;
	}

	public void setProfileFacade(ProfileFacade profileFacade) {
		this.profileFacade = profileFacade;
	}
}
