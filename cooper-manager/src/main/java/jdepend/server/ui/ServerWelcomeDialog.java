package jdepend.server.ui;

import java.awt.Graphics;

import jdepend.framework.ui.WelcomeDialog;

public class ServerWelcomeDialog extends WelcomeDialog {

	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
