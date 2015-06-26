package jdepend.model.profile.scope;

import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;

public abstract class AbstractProfileScope implements ProfileScope {

	private AnalysisResultProfile analysisResultProfile;

	private AreaComponentProfile areaComponentProfile;

	private ComponentProfile componentProfile;

	private JavaClassUnitProfile javaClassUnitProfile;

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

	protected abstract boolean isSelf(String group, String command);

}
