package jdepend.service.profile.scope;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.domain.PersistentBean;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassRelationItemProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.model.profile.model.RelationProfile;

public class ProfileScopeFacade extends PersistentBean implements ProfileScope, ProfileScopeMgr {

	private static final long serialVersionUID = -3898728864369810803L;

	private static ProfileScopeFacade facade;

	private WorkspaceProfileScope workspaceScope;

	private List<GroupProfileScope> groupScopes = new ArrayList<GroupProfileScope>();

	private List<CommandProfileScope> commandScopes = new ArrayList<CommandProfileScope>();

	private ProfileScopeFacade() {
		super("ProfileScopeFacade", "ProfileScopeFacade", PropertyConfigurator.DEFAULT_PROPERTY_DIR);

		if (!this.containSetting()) {
			this.initDefaultScopes();
		}
	}

	private void initDefaultScopes() {
		workspaceScope = new DefaultWorkspaceProfileScope();
	}

	private boolean containSetting() {
		return workspaceScope != null || groupScopes != null && groupScopes.size() > 0 || commandScopes != null
				&& commandScopes.size() > 0;
	}

	public static ProfileScopeFacade getInstance() {
		if (facade == null) {
			facade = new ProfileScopeFacade();
		}
		return facade;
	}

	@Override
	public AnalysisResultProfile getAnalysisResultProfile(String group, String command) {

		AnalysisResultProfile profile;

		for (ProfileScope scope : this.getProfileScope()) {
			profile = scope.getAnalysisResultProfile(group, command);
			if (profile != null) {
				return profile;
			}
		}

		return null;
	}

	@Override
	public AreaComponentProfile getAreaComponentProfile(String group, String command) {
		AreaComponentProfile profile;

		for (ProfileScope scope : this.getProfileScope()) {
			profile = scope.getAreaComponentProfile(group, command);
			if (profile != null) {
				return profile;
			}
		}

		return null;
	}

	@Override
	public ComponentProfile getComponentProfile(String group, String command) {
		ComponentProfile profile;

		for (ProfileScope scope : this.getProfileScope()) {
			profile = scope.getComponentProfile(group, command);
			if (profile != null) {
				return profile;
			}
		}

		return null;
	}

	@Override
	public RelationProfile getRelationProfile(String group, String command) {
		RelationProfile profile;

		for (ProfileScope scope : this.getProfileScope()) {
			profile = scope.getRelationProfile(group, command);
			if (profile != null) {
				return profile;
			}
		}

		return null;
	}

	@Override
	public JavaClassUnitProfile getJavaClassUnitProfile(String group, String command) {
		JavaClassUnitProfile profile;

		for (ProfileScope scope : this.getProfileScope()) {
			profile = scope.getJavaClassUnitProfile(group, command);
			if (profile != null) {
				return profile;
			}
		}

		return null;
	}

	@Override
	public JavaClassRelationItemProfile getJavaClassRelationItemProfile(String group, String command) {
		JavaClassRelationItemProfile profile;

		for (ProfileScope scope : this.getProfileScope()) {
			profile = scope.getJavaClassRelationItemProfile(group, command);
			if (profile != null) {
				return profile;
			}
		}

		return null;
	}

	private List<ProfileScope> getProfileScope() {

		List<ProfileScope> scopes = new ArrayList<ProfileScope>();
		scopes.addAll(commandScopes);
		scopes.addAll(groupScopes);
		scopes.add(workspaceScope);

		return scopes;
	}

	@Override
	public WorkspaceProfileScope getWorkspaceProfileScope() {
		return workspaceScope;
	}

	@Override
	public void setWorkspaceProfileScope(WorkspaceProfileScope workspaceProfileScope) {
		this.workspaceScope = workspaceProfileScope;
	}

	@Override
	public GroupProfileScope getGroupProfileScope(String group) {
		for (GroupProfileScope groupProfileScope : this.groupScopes) {
			if (groupProfileScope.getGroup().equals(group)) {
				return groupProfileScope;
			}
		}
		return null;
	}

	@Override
	public void setGroupProfileScope(GroupProfileScope groupProfileScope) {

		if (this.groupScopes.contains(groupProfileScope)) {
			this.groupScopes.remove(groupProfileScope);
		}
		this.groupScopes.add(groupProfileScope);
	}

	@Override
	public CommandProfileScope getCommandProfileScope(String group, String command) {
		for (CommandProfileScope commandProfileScope : this.commandScopes) {
			if (commandProfileScope.getGroup().equals(group) && commandProfileScope.getCommand().equals(command)) {
				return commandProfileScope;
			}
		}
		return null;
	}

	@Override
	public void setCommandProfileScope(CommandProfileScope commandProfileScope) {

		if (this.commandScopes.contains(commandProfileScope)) {
			this.commandScopes.remove(commandProfileScope);
		}
		this.commandScopes.add(commandProfileScope);
	}
}
