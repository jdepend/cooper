package jdepend.framework.ui;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JDialog;

import com.sun.awt.AWTUtilities;

public abstract class WelcomeDialog extends JDialog {

	protected Image backgroundImage = JDependUIUtil.getImage("Welcome.png");

	public WelcomeDialog() {
		setSize(600, 450);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示
		this.setLayout(new BorderLayout());
		this.setUndecorated(true);

		// 设置透明度
		AWTUtilities.setWindowOpacity(this, 0.8f);
	}
}
