package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.dialog.CooperSettingDialog;

public class SettingAction extends AbstractAction {

	private JDependCooper frame;

	public SettingAction(JDependCooper frame) {
		super("设置");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		CooperSettingDialog d = new CooperSettingDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}