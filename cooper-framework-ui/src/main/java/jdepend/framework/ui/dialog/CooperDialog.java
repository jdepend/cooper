package jdepend.framework.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import jdepend.framework.util.BundleUtil;

public abstract class CooperDialog extends JDialog {

	protected static final int ResultPopDialogWidth = 950;
	protected static final int ResultPopDialogHeight = 550;

	public CooperDialog() {
		this(null);
	}

	public CooperDialog(String title) {
		if (title != null) {
			this.setTitle(title);
		}
		getContentPane().setLayout(new BorderLayout());
		setSize(ResultPopDialogWidth, ResultPopDialogHeight);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示
	}

	protected JButton createCloseButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		return button;
	}
}
