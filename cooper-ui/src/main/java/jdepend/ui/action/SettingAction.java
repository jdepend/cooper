package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.ui.CooperSettingDialog;
import jdepend.ui.JDependCooper;

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