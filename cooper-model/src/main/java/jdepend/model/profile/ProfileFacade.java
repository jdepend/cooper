package jdepend.model.profile;

import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassRelationItemProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;

public interface ProfileFacade {

	public AnalysisResultProfile getAnalysisResultProfile();

	public AreaComponentProfile getAreaComponentProfile();

	public ComponentProfile getComponentProfile();

	public JavaClassUnitProfile getJavaClassUnitProfile();

	public JavaClassRelationItemProfile getJavaClassRelationItemProfile();

}
