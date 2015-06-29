package jdepend.service.profile.scope;

public interface ProfileScopeMgr {

	public WorkspaceProfileScope getWorkspaceProfileScope();

	public void setWorkspaceProfileScope(WorkspaceProfileScope workspaceProfileScope);

	public GroupProfileScope getGroupProfileScope(String group);

	public void setGroupProfileScope(GroupProfileScope groupProfileScope);

	public CommandProfileScope getCommandProfileScope(String group, String command);

	public void setCommandProfileScope(CommandProfileScope commandProfileScope);

}
