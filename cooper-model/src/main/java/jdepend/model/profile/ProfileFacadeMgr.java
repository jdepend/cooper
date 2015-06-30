package jdepend.model.profile;

import jdepend.model.profile.model.defaultvalue.DefaultProfileFacadeImpl;

public class ProfileFacadeMgr {

	private static ProfileFacadeMgr mgr = new ProfileFacadeMgr();

	private ProfileFacade profileFacade;

	private ProfileFacadeMgr() {
		profileFacade = new DefaultProfileFacadeImpl();
	}

	private static ProfileFacadeMgr getInstance() {
		return mgr;
	}

	public ProfileFacade getProfileFacade() {
		return profileFacade;
	}

	public void setProfileFacade(ProfileFacade profileFacade) {
		this.profileFacade = profileFacade;
	}

}
