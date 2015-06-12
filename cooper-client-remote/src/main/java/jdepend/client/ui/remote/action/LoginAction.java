package jdepend.client.ui.remote.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.remote.LoginDialog;

public class LoginAction extends AbstractAction {

	private JDependCooper frame;

	public LoginAction(JDependCooper frame) {
		super("登陆");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		LoginDialog d = new LoginDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}