package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.ui.JDependCooper;
import jdepend.ui.LoginDialog;

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