package jdepend.model.profile.model.defaultvalue;

import jdepend.model.profile.model.ComponentProfile;

public class DefaultComponentProfile extends ComponentProfile {

	public DefaultComponentProfile() {
		this.setBalance(ComponentProfile.balanceFromPackage);
		this.setStabilityWithCountScale(0.5F);
	}
}
