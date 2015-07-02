package jdepend.client.ui.profile.settingpanel;

import javax.swing.JCheckBox;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.profile.model.AreaComponentProfile;

public class AreaComponentProfileSettingPanel extends ModelProfileSettingPanel {

	private AreaComponentProfile areaComponentProfile;

	private JCheckBox createCheckBox;

	public AreaComponentProfileSettingPanel(AreaComponentProfile areaComponentProfile) {
		this.areaComponentProfile = areaComponentProfile;

		this.createCheckBox = new JCheckBox("是否计算组件区域");

		if (areaComponentProfile.isCreate()) {
			this.createCheckBox.setSelected(true);
		}
		
		this.add(this.createCheckBox);
	}

	@Override
	public void validateData() throws ProfileValidateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

}
