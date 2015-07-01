package jdepend.client.ui.profile;

import jdepend.client.ui.JDependCooper;
import jdepend.service.profile.scope.ProfileScopeFacade;

public class WorkspaceProfileSettingDialog extends ProfileSettingDialog {

	public WorkspaceProfileSettingDialog(JDependCooper frame) {
		super(frame, ProfileScopeFacade.getInstance().getWorkspaceProfileScope().getProfileFacade());
	}

}
