package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import jdepend.core.remote.session.RemoteSessionProxy;
import jdepend.framework.context.ClientContext;
import jdepend.framework.ui.panel.StatusField;
import jdepend.ui.JDependCooper;
import jdepend.ui.LoginDialog;

public class LogoutAction extends AbstractAction {

	private JDependCooper frame;

	public LogoutAction(JDependCooper frame) {
		super("注销");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		if (RemoteSessionProxy.getInstance().getSessionId() == null) {
			JOptionPane.showMessageDialog(frame, "你还没有登陆", "提示", JOptionPane.CLOSED_OPTION);
			return;
		}
		if (JOptionPane.showConfirmDialog(frame, "您是否确认注销？", "提示", JOptionPane.YES_NO_OPTION) == 0) {
			try {
				RemoteSessionProxy.getInstance().logout();
				ClientContext.setUser(null);
				frame.getStatusField().setText(LoginDialog.Logout, StatusField.Right);
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(frame, e1.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
			}
		}
	}
}
