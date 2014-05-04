package jdepend.ui.start;

import jdepend.framework.domain.PersistentBean;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileUtil;

public final class WorkspaceSetting extends PersistentBean {

	private static final long serialVersionUID = -5421008827282420116L;

	private static final String PROTOTYPE_FILE = "/prototype.zip";

	private String workspacePath;

	public WorkspaceSetting() {
		super("workspacePath", "workspacePath", null);
	}

	@Override
	protected void initPath(String path) {
		this.setPath(path);
	}

	public boolean Inited() {
		return this.workspacePath != null;
	}

	public void initWorkspace(String text) throws JDependException {
		workspacePath = text;

		FileUtil.unZipFile(workspacePath, WorkspaceSetting.class.getResourceAsStream(PROTOTYPE_FILE));

	}

	public String getWorkspacePath() {
		return workspacePath;
	}

	public void setWorkspacePath(String workspacePath) {
		this.workspacePath = workspacePath;
	}
}
