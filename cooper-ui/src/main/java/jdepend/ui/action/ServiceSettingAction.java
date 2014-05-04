package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.ui.JDependCooper;
import jdepend.ui.ServiceProxySettingDialog;

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