package jdepend.client.ui.command;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.profile.ProfileSettingDialog;
import jdepend.model.profile.ProfileFacade;
import jdepend.service.profile.scope.CommandProfileScope;
import jdepend.service.profile.scope.ProfileScopeFacade;

public class CommandProfileSettingDialog extends ProfileSettingDialog {

	private String group;

	private String command;

	public CommandProfileSettingDialog(JDependCooper frame, String group, String command) {
		super(frame, ProfileScopeFacade.getInstance().getCommandProfileScope(group, command).getProfileFacade());
		this.setName("定制" + group + "." + command + "命令的规则");
		this.group = group;
		this.command = command;
	}

	@Override
	protected void updateScope(ProfileFacade profileFacade) {
		CommandProfileScope commandProfileScope = new CommandProfileScope();
		commandProfileScope.setGroup(group);
		commandProfileScope.setCommand(command);
		commandProfileScope.setProfileFacade(profileFacade);
		ProfileScopeFacade.getInstance().setCommandProfileScope(commandProfileScope);
	}
}
