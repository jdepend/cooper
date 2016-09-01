package jdepend.client.ui.result.panel;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.profile.ProfileSettingDialog;
import jdepend.model.JDependUnitMgr;
import jdepend.model.profile.ProfileFacade;

public class CurrentProfileSettingDialog extends ProfileSettingDialog {

	public CurrentProfileSettingDialog(JDependCooper frame) {
		super(frame, JDependUnitMgr.getInstance().getResult().getRunningContext().getProfileFacade());
		this.setTitle("当前分析结果使用的规则");
	}

	@Override
	protected void updateScope(ProfileFacade profileFacade) {

	}
}
