package jdepend.client.ui.profile.settingpanel;

import javax.swing.JPanel;

import jdepend.client.ui.profile.ProfileValidateException;

public abstract class ModelProfileSettingPanel extends JPanel {

	public abstract void validateData() throws ProfileValidateException;

	public abstract void save();

}
