package jdepend.model.profile.model.defaultvalue;

import jdepend.model.profile.model.JavaClassUnitProfile;

public class DefaultJavaClassUnitProfile extends JavaClassUnitProfile {

	public DefaultJavaClassUnitProfile() {

		this.setAbstractClassRules(getAllAbstractClassRules());
		this.setStableRules(getAllStableRules());
	}
}
