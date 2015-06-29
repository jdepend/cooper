package jdepend.service.profile.scope;

import java.io.Serializable;

import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassRelationItemProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.model.profile.model.RelationProfile;

public interface ProfileScope extends Serializable {

	public AnalysisResultProfile getAnalysisResultProfile(String group, String command);

	public AreaComponentProfile getAreaComponentProfile(String group, String command);

	public ComponentProfile getComponentProfile(String group, String command);
	
	public RelationProfile getRelationProfile(String group, String command);

	public JavaClassUnitProfile getJavaClassUnitProfile(String group, String command);
	
	public JavaClassRelationItemProfile getJavaClassRelationItemProfile(String group, String command);

}
