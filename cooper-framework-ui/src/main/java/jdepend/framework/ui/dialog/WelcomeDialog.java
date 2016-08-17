package jdepend.framework.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jdepend.framework.ui.util.JDependUIUtil;

import com.sun.awt.AWTUtilities;

public class WelcomeDialog extends JDialog {

	protected Image backgroundImage = JDependUIUtil.getImage("Welcome.png");

	public WelcomeDialog() {
		setSize(600, 450);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示
		this.setLayout(new BorderLayout());
		this.setUndecorated(true);

		// 设置透明度
		AWTUtilities.setWindowOpacity(this, 0.8f);
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	public void showError(String message) {
		JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel msg = new JLabel(message);
		errorPanel.add(msg);
		
		this.add(BorderLayout.SOUTH, errorPanel);
		this.setVisible(true);
		
	}
}
