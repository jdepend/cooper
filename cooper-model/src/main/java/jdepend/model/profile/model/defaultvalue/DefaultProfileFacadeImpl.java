package jdepend.model.profile.model.defaultvalue;

import jdepend.model.profile.ProfileFacade;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassRelationItemProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.model.profile.model.RelationProfile;

public class DefaultProfileFacadeImpl implements ProfileFacade {

	@Override
	public AnalysisResultProfile getAnalysisResultProfile() {
		return new DefaultAnalysisResultProfile();
	}

	@Override
	public AreaComponentProfile getAreaComponentProfile() {
		return new DefaultAreaComponentProfile();
	}

	@Override
	public ComponentProfile getComponentProfile() {
		return new DefaultComponentProfile();
	}

	@Override
	public RelationProfile getRelationProfile() {
		return new DefaultRelationProfile();
	}

	@Override
	public JavaClassUnitProfile getJavaClassUnitProfile() {
		return new DefaultJavaClassUnitProfile();
	}

	@Override
	public JavaClassRelationItemProfile getJavaClassRelationItemProfile() {
		return new DefaultJavaClassRelationItemProfile();
	}

}
