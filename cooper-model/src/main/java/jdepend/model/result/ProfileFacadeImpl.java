package jdepend.model.result;

import java.io.Serializable;

import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.ProfileFacade;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassRelationItemProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.model.profile.model.RelationProfile;
import jdepend.model.profile.model.defaultvalue.DefaultAnalysisResultProfile;
import jdepend.model.profile.model.defaultvalue.DefaultAreaComponentProfile;
import jdepend.model.profile.model.defaultvalue.DefaultComponentProfile;
import jdepend.model.profile.model.defaultvalue.DefaultJavaClassRelationItemProfile;
import jdepend.model.profile.model.defaultvalue.DefaultJavaClassUnitProfile;
import jdepend.model.profile.model.defaultvalue.DefaultRelationProfile;

public class ProfileFacadeImpl implements MaintainProfileFacade, Serializable {

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
		if (this.analysisResultProfile == null) {
			this.analysisResultProfile = new DefaultAnalysisResultProfile();
		}

		this.areaComponentProfile = profileFacade.getAreaComponentProfile();
		if (this.areaComponentProfile == null) {
			this.areaComponentProfile = new DefaultAreaComponentProfile();
		}

		this.componentProfile = profileFacade.getComponentProfile();
		if (this.componentProfile == null) {
			this.componentProfile = new DefaultComponentProfile();
		}

		this.relationProfile = profileFacade.getRelationProfile();
		if (this.relationProfile == null) {
			this.relationProfile = new DefaultRelationProfile();
		}
		this.javaClassUnitProfile = profileFacade.getJavaClassUnitProfile();
		if (this.javaClassUnitProfile == null) {
			this.javaClassUnitProfile = new DefaultJavaClassUnitProfile();
		}
		this.javaClassRelationItemProfile = profileFacade.getJavaClassRelationItemProfile();
		if (this.javaClassRelationItemProfile == null) {
			this.javaClassRelationItemProfile = new DefaultJavaClassRelationItemProfile();
		}
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

	@Override
	public void setAnalysisResultProfile(AnalysisResultProfile analysisResultProfile) {
		this.analysisResultProfile = analysisResultProfile;
	}

	@Override
	public void setAreaComponentProfile(AreaComponentProfile areaComponentProfile) {
		this.areaComponentProfile = areaComponentProfile;
	}

	@Override
	public void setComponentProfile(ComponentProfile componentProfile) {
		this.componentProfile = componentProfile;
	}

	@Override
	public void setRelationProfile(RelationProfile relationProfile) {
		this.relationProfile = relationProfile;
	}

	@Override
	public void setJavaClassUnitProfile(JavaClassUnitProfile javaClassUnitProfile) {
		this.javaClassUnitProfile = javaClassUnitProfile;
	}

	@Override
	public void setJavaClassRelationItemProfile(JavaClassRelationItemProfile javaClassRelationItemProfile) {
		this.javaClassRelationItemProfile = javaClassRelationItemProfile;
	}
}
