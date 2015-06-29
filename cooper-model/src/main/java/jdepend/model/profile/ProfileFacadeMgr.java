package jdepend.model.profile;

public class ProfileFacadeMgr {

	private static ProfileFacadeMgr mgr = new ProfileFacadeMgr();

	private ProfileFacade profileFacade;

	private ProfileFacadeMgr() {

	}

	public static ProfileFacadeMgr getInstance() {
		return mgr;
	}

	public ProfileFacade getProfileFacade() {
		return profileFacade;
	}

	public void setProfileFacade(ProfileFacade profileFacade) {
		this.profileFacade = profileFacade;
	}

}
