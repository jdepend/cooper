package jdepend.model.profile.scope;

public class WorkspaceProfileScope extends AbstractProfileScope {

	private static final long serialVersionUID = -3370964430282085060L;

	public WorkspaceProfileScope() {
		super();
	}

	@Override
	protected boolean isSelf(String group, String command) {
		return true;
	}

}
