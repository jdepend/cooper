package jdepend.client.ui.profile.settingpanel;

import java.awt.Component;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.model.JavaClassUnitProfile;

public class JavaClassUnitProfileSettingPanel extends ModelProfileSettingPanel {
	
	private JavaClassUnitProfile javaClassUnitProfile;

	public JavaClassUnitProfileSettingPanel(JavaClassUnitProfile javaClassUnitProfile) {
		this.javaClassUnitProfile = javaClassUnitProfile;
	}
	

	protected Component leftPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateData() throws ProfileValidateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(MaintainProfileFacade maintainProfileFacade) {
		// TODO Auto-generated method stub
		maintainProfileFacade.setJavaClassUnitProfile(javaClassUnitProfile);
	}

	@Override
	protected String getExplain() {
		// TODO Auto-generated method stub
		return null;
	}


}
