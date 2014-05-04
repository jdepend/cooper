package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import jdepend.framework.context.JDependContext;
import jdepend.ui.JDependCooper;
import jdepend.ui.LoginDialog;

public class LoginAction extends AbstractAction {

	private JDependCooper frame;

	public LoginAction(JDependCooper frame) {
		super("登陆");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		if (JDependContext.isLocalService()) {
			JOptionPane.showMessageDialog(frame, "该客户端运行在单机模式下，不能够进行登陆操作", "提示", JOptionPane.CLOSED_OPTION);
			return;
		}
		LoginDialog d = new LoginDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}