package jdepend.service.profile.scope;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.domain.PersistentBean;
import jdepend.model.profile.ProfileFacade;
import jdepend.model.profile.model.defaultvalue.DefaultProfileFacadeImpl;

public class ProfileScopeFacade extends PersistentBean {

	private static final long serialVersionUID = -3898728864369810803L;

	private static ProfileScopeFacade facade;

	private WorkspaceProfileScope workspaceProfileScope;

	private List<GroupProfileScope> groupProfileScopes;

	private List<CommandProfileScope> commandProfileScopes;

	private ProfileScopeFacade() {
		super("ProfileScopeFacade", "ProfileScopeFacade", PropertyConfigurator.DEFAULT_PROPERTY_DIR);

		if (!this.containSetting()) {
			this.initDefaultScopes();
		}
		if (groupProfileScopes == null) {
			groupProfileScopes = new ArrayList<GroupProfileScope>();
		}

		if (commandProfileScopes == null) {
			commandProfileScopes = new ArrayList<CommandProfileScope>();
		}
	}

	private void initDefaultScopes() {
		workspaceProfileScope = new WorkspaceProfileScope();
		workspaceProfileScope.setProfileFacade(new DefaultProfileFacadeImpl());
		groupProfileScopes = new ArrayList<GroupProfileScope>();
		commandProfileScopes = new ArrayList<CommandProfileScope>();
	}

	private boolean containSetting() {
		return workspaceProfileScope != null || groupProfileScopes != null && groupProfileScopes.size() > 0
				|| commandProfileScopes != null && commandProfileScopes.size() > 0;
	}

	public static ProfileScopeFacade getInstance() {
		if (facade == null) {
			facade = new ProfileScopeFacade();
		}
		return facade;
	}

	public void refresh() {
		facade = new ProfileScopeFacade();
	}

	public ProfileFacade getProfileFacade(String group, String command) {

		for (AbstractProfileScope scope : this.getProfileScope()) {
			if (scope.isSelf(group, command)) {
				return scope.getProfileFacade();
			}
		}

		return null;
	}

	public ProfileFacade getDefaultProfileFacade() {
		return new DefaultProfileFacadeImpl();
	}

	private List<AbstractProfileScope> getProfileScope() {

		List<AbstractProfileScope> scopes = new ArrayList<AbstractProfileScope>();
		scopes.addAll(commandProfileScopes);
		scopes.addAll(groupProfileScopes);
		scopes.add(workspaceProfileScope);

		return scopes;
	}

	public WorkspaceProfileScope getWorkspaceProfileScope() {
		return workspaceProfileScope;
	}

	public void setWorkspaceProfileScope(WorkspaceProfileScope workspaceProfileScope) {
		this.workspaceProfileScope = workspaceProfileScope;
	}

	public List<GroupProfileScope> getGroupProfileScopes() {
		return groupProfileScopes;
	}

	public void setGroupProfileScopes(List<GroupProfileScope> groupProfileScopes) {
		this.groupProfileScopes = groupProfileScopes;
	}

	public List<CommandProfileScope> getCommandProfileScopes() {
		return commandProfileScopes;
	}

	public void setCommandProfileScopes(List<CommandProfileScope> commandProfileScopes) {
		this.commandProfileScopes = commandProfileScopes;
	}

	public ProfileScope getGroupProfileScope(String group) {
		for (GroupProfileScope groupProfileScope : this.groupProfileScopes) {
			if (groupProfileScope.getGroup().equals(group)) {
				return groupProfileScope;
			}
		}
		return this.workspaceProfileScope;
	}

	public void setGroupProfileScope(GroupProfileScope groupProfileScope) {

		if (this.groupProfileScopes.contains(groupProfileScope)) {
			this.groupProfileScopes.remove(groupProfileScope);
		}
		this.groupProfileScopes.add(groupProfileScope);
	}

	public ProfileScope getCommandProfileScope(String group, String command) {
		for (CommandProfileScope commandProfileScope : this.commandProfileScopes) {
			if (commandProfileScope.getGroup().equals(group) && commandProfileScope.getCommand().equals(command)) {
				return commandProfileScope;
			}
		}
		return this.getGroupProfileScope(group);
	}

	public void setCommandProfileScope(CommandProfileScope commandProfileScope) {

		if (this.commandProfileScopes.contains(commandProfileScope)) {
			this.commandProfileScopes.remove(commandProfileScope);
		}
		this.commandProfileScopes.add(commandProfileScope);
	}
}
