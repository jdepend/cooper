package jdepend.model.profile.scope;

public class WorkspaceProfileScope extends AbstractProfileScope {

	private static final long serialVersionUID = -3370964430282085060L;
	
	private String workspacePath;

	public WorkspaceProfileScope() {
		super();
	}

	public WorkspaceProfileScope(String workspacePath) {
		super();
		this.workspacePath = workspacePath;
	}

	@Override
	protected boolean isSelf(String group, String command) {
		return true;
	}

}
