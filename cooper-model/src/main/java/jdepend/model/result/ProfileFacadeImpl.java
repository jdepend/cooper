package jdepend.model.result;

import java.io.Serializable;

import jdepend.model.profile.ProfileFacade;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassRelationItemProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.model.profile.model.RelationProfile;

public class ProfileFacadeImpl implements ProfileFacade, Serializable {

	private static final long serialVersionUID = 3545005678622982882L;

	private AnalysisResultProfile analysisResultProfile;

	private AreaComponentProfile areaComponentProfile;

	private ComponentProfile componentProfile;

	private RelationProfile relationProfile;

	private JavaClassUnitProfile javaClassUnitProfile;

	private JavaClassRelationItemProfile javaClassRelationItemProfile;

	public ProfileFacadeImpl() {
	}

	public ProfileFacadeImpl(ProfileFacade profileFacade) {
		this.analysisResultProfile = profileFacade.getAnalysisResultProfile();
		this.areaComponentProfile = profileFacade.getAreaComponentProfile();
		this.componentProfile = profileFacade.getComponentProfile();
		this.relationProfile = profileFacade.getRelationProfile();
		this.javaClassUnitProfile = profileFacade.getJavaClassUnitProfile();
		this.javaClassRelationItemProfile = profileFacade.getJavaClassRelationItemProfile();
	}

	@Override
	public AnalysisResultProfile getAnalysisResultProfile() {
		return analysisResultProfile;
	}

	@Override
	public AreaComponentProfile getAreaComponentProfile() {
		return areaComponentProfile;
	}

	@Override
	public ComponentProfile getComponentProfile() {
		return componentProfile;
	}

	@Override
	public RelationProfile getRelationProfile() {
		return relationProfile;
	}

	@Override
	public JavaClassUnitProfile getJavaClassUnitProfile() {
		return javaClassUnitProfile;
	}

	@Override
	public JavaClassRelationItemProfile getJavaClassRelationItemProfile() {
		return javaClassRelationItemProfile;
	}

	public void setAnalysisResultProfile(AnalysisResultProfile analysisResultProfile) {
		this.analysisResultProfile = analysisResultProfile;
	}

	public void setAreaComponentProfile(AreaComponentProfile areaComponentProfile) {
		this.areaComponentProfile = areaComponentProfile;
	}

	public void setComponentProfile(ComponentProfile componentProfile) {
		this.componentProfile = componentProfile;
	}

	public void setRelationProfile(RelationProfile relationProfile) {
		this.relationProfile = relationProfile;
	}

	public void setJavaClassUnitProfile(JavaClassUnitProfile javaClassUnitProfile) {
		this.javaClassUnitProfile = javaClassUnitProfile;
	}

	public void setJavaClassRelationItemProfile(JavaClassRelationItemProfile javaClassRelationItemProfile) {
		this.javaClassRelationItemProfile = javaClassRelationItemProfile;
	}
}
