package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.start.WorkspaceSetting;
import jdepend.client.ui.start.WorkspaceSettingDialog;

public  class SettingWorkspaceAction extends AbstractAction {

	private JDependCooper frame;

	public SettingWorkspaceAction(JDependCooper frame) {
		super("设置工作区");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		WorkspaceSettingDialog d = new WorkspaceSettingDialog(new WorkspaceSetting(), frame);
		d.setModal(true);
		d.setVisible(true);
	}
}