package jdepend.model.profile.scope;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.domain.PersistentBean;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;

public class ProfileScopeFacade extends PersistentBean implements ProfileScope {

	private static final long serialVersionUID = -3898728864369810803L;

	private static ProfileScopeFacade facade;

	private List<ProfileScope> scopes = new ArrayList<ProfileScope>();

	private ProfileScopeFacade() {
		super("ProfileScopeFacade", "ProfileScopeFacade", PropertyConfigurator.DEFAULT_PROPERTY_DIR);

		if (scopes == null) {
			this.initDefaultScopes();
		}
	}

	private void initDefaultScopes() {
		scopes = new ArrayList<ProfileScope>();
		scopes.add(new DefaultWorkspaceProfileScope());
	}

	public static ProfileScopeFacade getInstance() {
		if (facade == null) {
			facade = new ProfileScopeFacade();
		}
		return facade;
	}

	@Override
	public AnalysisResultProfile getAnalysisResultProfile(String group, String command) {

		AnalysisResultProfile analysisResultProfile;

		for (ProfileScope scope : scopes) {
			analysisResultProfile = scope.getAnalysisResultProfile(group, command);
			if (analysisResultProfile != null) {
				return analysisResultProfile;
			}
		}

		return null;
	}

	@Override
	public AreaComponentProfile getAreaComponentProfile(String group, String command) {
		AreaComponentProfile areaComponentProfile;

		for (ProfileScope scope : scopes) {
			areaComponentProfile = scope.getAreaComponentProfile(group, command);
			if (areaComponentProfile != null) {
				return areaComponentProfile;
			}
		}

		return null;
	}

	@Override
	public ComponentProfile getComponentProfile(String group, String command) {
		ComponentProfile analysisResultProfile;

		for (ProfileScope scope : scopes) {
			analysisResultProfile = scope.getComponentProfile(group, command);
			if (analysisResultProfile != null) {
				return analysisResultProfile;
			}
		}

		return null;
	}

	@Override
	public JavaClassUnitProfile getJavaClassUnitProfile(String group, String command) {
		JavaClassUnitProfile javaClassUnitProfile;

		for (ProfileScope scope : scopes) {
			javaClassUnitProfile = scope.getJavaClassUnitProfile(group, command);
			if (javaClassUnitProfile != null) {
				return javaClassUnitProfile;
			}
		}

		return null;
	}

}
