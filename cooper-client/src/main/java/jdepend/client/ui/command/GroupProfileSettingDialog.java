package jdepend.client.ui.command;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.profile.ProfileSettingDialog;
import jdepend.model.profile.ProfileFacade;
import jdepend.service.profile.scope.GroupProfileScope;
import jdepend.service.profile.scope.ProfileScopeFacade;

public class GroupProfileSettingDialog extends ProfileSettingDialog {

	private String group;

	public GroupProfileSettingDialog(JDependCooper frame, String group) {
		super(frame, ProfileScopeFacade.getInstance().getGroupProfileScope(group).getProfileFacade());
		this.setName("定制" + group + "组的规则");
		this.group = group;
	}

	@Override
	protected void updateScope(ProfileFacade profileFacade) {
		GroupProfileScope groupProfileScope = new GroupProfileScope();
		groupProfileScope.setGroup(group);
		groupProfileScope.setProfileFacade(profileFacade);
		ProfileScopeFacade.getInstance().setGroupProfileScope(groupProfileScope);
	}
}
