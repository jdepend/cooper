package jdepend.service.profile.scope;

import java.io.Serializable;

import jdepend.model.profile.ProfileFacade;

public interface ProfileScope extends Serializable{

	public ProfileFacade getProfileFacade(String group, String command);


}
