package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.dialog.ServiceProxySettingDialog;

public class ServiceSettingAction extends AbstractAction {

	private JDependCooper frame;

	public ServiceSettingAction(JDependCooper frame) {
		super("服务设置");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		ServiceProxySettingDialog d = new ServiceProxySettingDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}