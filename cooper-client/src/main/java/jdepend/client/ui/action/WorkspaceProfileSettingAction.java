package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.profile.WorkspaceProfileSettingDialog;

public class WorkspaceProfileSettingAction extends AbstractAction {

	private JDependCooper frame;

	public WorkspaceProfileSettingAction(JDependCooper frame) {
		super("设置工作区级别的规则");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		WorkspaceProfileSettingDialog d = new WorkspaceProfileSettingDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}
