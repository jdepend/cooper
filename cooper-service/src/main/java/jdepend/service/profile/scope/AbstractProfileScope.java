package jdepend.service.profile.scope;

import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassRelationItemProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;

public abstract class AbstractProfileScope implements ProfileScope {

	private AnalysisResultProfile analysisResultProfile;

	private AreaComponentProfile areaComponentProfile;

	private ComponentProfile componentProfile;

	private JavaClassUnitProfile javaClassUnitProfile;

	private JavaClassRelationItemProfile javaClassRelationItemProfile;

	@Override
	public AnalysisResultProfile getAnalysisResultProfile(String group, String command) {
		if (this.isSelf(group, command)) {
			return analysisResultProfile;
		} else {
			return null;
		}
	}

	@Override
	public AreaComponentProfile getAreaComponentProfile(String group, String command) {

		if (this.isSelf(group, command)) {
			return areaComponentProfile;
		} else {
			return null;
		}
	}

	@Override
	public ComponentProfile getComponentProfile(String group, String command) {

		if (this.isSelf(group, command)) {
			return componentProfile;
		} else {
			return null;
		}
	}

	@Override
	public JavaClassUnitProfile getJavaClassUnitProfile(String group, String command) {

		if (this.isSelf(group, command)) {
			return javaClassUnitProfile;
		} else {
			return null;
		}
	}

	@Override
	public JavaClassRelationItemProfile getJavaClassRelationItemProfile(String group, String command) {

		if (this.isSelf(group, command)) {
			return javaClassRelationItemProfile;
		} else {
			return null;
		}
	}

	protected abstract boolean isSelf(String group, String command);

	public AnalysisResultProfile getAnalysisResultProfile() {
		return analysisResultProfile;
	}

	public void setAnalysisResultProfile(AnalysisResultProfile analysisResultProfile) {
		this.analysisResultProfile = analysisResultProfile;
	}

	public AreaComponentProfile getAreaComponentProfile() {
		return areaComponentProfile;
	}

	public void setAreaComponentProfile(AreaComponentProfile areaComponentProfile) {
		this.areaComponentProfile = areaComponentProfile;
	}

	public ComponentProfile getComponentProfile() {
		return componentProfile;
	}

	public void setComponentProfile(ComponentProfile componentProfile) {
		this.componentProfile = componentProfile;
	}

	public JavaClassUnitProfile getJavaClassUnitProfile() {
		return javaClassUnitProfile;
	}

	public void setJavaClassUnitProfile(JavaClassUnitProfile javaClassUnitProfile) {
		this.javaClassUnitProfile = javaClassUnitProfile;
	}

	public void setJavaClassRelationItemProfile(JavaClassRelationItemProfile javaClassRelationItemProfile) {
		this.javaClassRelationItemProfile = javaClassRelationItemProfile;
	}
}
