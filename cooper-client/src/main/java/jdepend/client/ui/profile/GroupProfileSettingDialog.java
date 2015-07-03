package jdepend.client.ui.profile;

import jdepend.client.ui.JDependCooper;
import jdepend.model.profile.ProfileFacade;
import jdepend.service.profile.scope.ProfileScopeFacade;
import jdepend.service.profile.scope.WorkspaceProfileScope;

public class GroupProfileSettingDialog extends ProfileSettingDialog {

	private String group;

	public GroupProfileSettingDialog(JDependCooper frame, String group) {
		super(frame, ProfileScopeFacade.getInstance().getGroupProfileScope(group).getProfileFacade());

		this.group = group;
	}

	@Override
	protected void updateScope(ProfileFacade profileFacade) {
		WorkspaceProfileScope workspaceProfileScope = new WorkspaceProfileScope();
		workspaceProfileScope.setProfileFacade(profileFacade);
		ProfileScopeFacade.getInstance().setWorkspaceProfileScope(workspaceProfileScope);
	}
}
