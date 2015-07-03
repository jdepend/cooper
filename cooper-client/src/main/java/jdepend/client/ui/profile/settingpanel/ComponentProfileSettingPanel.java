package jdepend.client.ui.profile.settingpanel;

import java.awt.Component;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.model.ComponentProfile;

public class ComponentProfileSettingPanel extends ModelProfileSettingPanel {
	
	private ComponentProfile componentProfile;

	public ComponentProfileSettingPanel(ComponentProfile componentProfile) {
		this.componentProfile = componentProfile;
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
		maintainProfileFacade.setComponentProfile(componentProfile);
	}

	@Override
	protected String getExplain() {
		// TODO Auto-generated method stub
		return null;
	}

}
