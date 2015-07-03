package jdepend.model.profile;

import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassRelationItemProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.model.profile.model.RelationProfile;

public interface MaintainProfileFacade extends ProfileFacade {

	public void setAnalysisResultProfile(AnalysisResultProfile analysisResultProfile);

	public void setAreaComponentProfile(AreaComponentProfile areaComponentProfile);

	public void setComponentProfile(ComponentProfile componentProfile);

	public void setRelationProfile(RelationProfile relationProfile);

	public void setJavaClassUnitProfile(JavaClassUnitProfile javaClassUnitProfile);

	public void setJavaClassRelationItemProfile(JavaClassRelationItemProfile javaClassRelationItemProfile);

}
