package jdepend.framework.ui;

import java.awt.BorderLayout;

import javax.swing.JDialog;

public abstract class CooperDialog extends JDialog {

	protected static final int ResultPopDialogWidth = 750;
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
}
