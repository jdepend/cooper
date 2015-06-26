package jdepend.model.profile.scope;

import java.io.Serializable;

import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;

public interface ProfileScope extends Serializable {

	public AnalysisResultProfile getAnalysisResultProfile(String group, String command);

	public AreaComponentProfile getAreaComponentProfile(String group, String command);

	public ComponentProfile getComponentProfile(String group, String command);

	public JavaClassUnitProfile getJavaClassUnitProfile(String group, String command);

}
