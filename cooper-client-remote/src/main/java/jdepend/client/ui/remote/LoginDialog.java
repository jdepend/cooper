package jdepend.client.ui.remote;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import jdepend.client.core.remote.session.RemoteSessionProxy;
import jdepend.framework.context.ClientContext;
import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.ui.panel.StatusField;
import jdepend.framework.util.BundleUtil;
import jdepend.client.ui.framework.ExceptionUtil;

public final class LoginDialog extends JDialog {

	private JDependFrame frame;

	private JTextField userName;
	private JTextField passWord;

	public static final String Login = BundleUtil.getString(BundleUtil.State_Logined);
	public static final String Logout = BundleUtil.getString(BundleUtil.State_unLogin);

	public LoginDialog(JDependFrame frame) {

		this.frame = frame;

		setTitle("登录");

		setResizable(false);

		getContentPane().setLayout(new FlowLayout());

		setSize(300, 140);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		this.setLayout(new BorderLayout());

		try {
			if (RemoteSessionProxy.getInstance().isValid()) {
				getContentPane().add(getLogoutPanel());
			} else {
				getContentPane().add(getLoginPanel());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			getContentPane().add(getLoginPanel());
		}
	}

	private JPanel getLogoutPanel() {
		JPanel logoutPanel = new JPanel(new BorderLayout());

		JPanel content = new JPanel(new FlowLayout());

		content.add(new JLabel("您已登录，登录用户名：" + RemoteSessionProxy.getInstance().getUserName()));

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createCloseButton());

		logoutPanel.add(BorderLayout.CENTER, content);
		logoutPanel.add(BorderLayout.SOUTH, buttonBar);

		return logoutPanel;
	}

	private JPanel getLoginPanel() {

		JPanel loginPanel = new JPanel(new BorderLayout());

		JPanel content = new JPanel(new BorderLayout());

		JPanel left = new JPanel(new GridLayout(2, 1));
		JPanel right = new JPanel(new GridLayout(2, 1));

		left.add(new JLabel("用户名："));
		left.add(new JLabel("口令："));

		userName = new JTextField();
		passWord = new JPasswordField();

		userName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
		passWord.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});

		right.add(userName);
		right.add(passWord);

		content.add(BorderLayout.WEST, left);
		content.add(BorderLayout.CENTER, right);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createLoginButton());
		buttonBar.add(createCancelButton());

		loginPanel.add(BorderLayout.CENTER, content);
		loginPanel.add(BorderLayout.SOUTH, buttonBar);

		return loginPanel;
	}

	private JButton createLoginButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Login));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		return button;
	}

	private JButton createCloseButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		return button;
	}

	private JButton createCancelButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Cancel));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		return button;
	}

	private void login() {
		try {
			doLogin();
			dispose();
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(frame, ExceptionUtil.getMessage(e1), "错误", JOptionPane.CLOSED_OPTION);
		}
	}

	private void doLogin() throws JDependException, RemoteException {
		String username = userName.getText();
		String password = passWord.getText();
		if (username == null || username.length() == 0) {
			throw new JDependException("用户名不能为空");
		}
		RemoteSessionProxy.getInstance().login(username, password);
		ClientContext.setUser(username);
		frame.getStatusField().setText(Login, StatusField.Right);

		BusiLogUtil.getInstance().businessLog(Operation.login);
	}
}
