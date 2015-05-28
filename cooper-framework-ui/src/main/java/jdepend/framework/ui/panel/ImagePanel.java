package jdepend.framework.ui.panel;

import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JPanel;

import jdepend.framework.ui.util.JDependUIUtil;

public class ImagePanel extends JPanel {

	private String imageName;

	private boolean stretch = true;

	public ImagePanel(String imageName) {
		this.imageName = imageName;

		this.setLayout(new GridBagLayout());
		this.setBackground(new java.awt.Color(255, 255, 255));
	}

	public void setStretch(boolean stretch) {
		this.stretch = stretch;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image backgroundImage = JDependUIUtil.getImage(this.imageName);
		int width;
		int height;
		if (stretch) {
			width = this.getWidth();
			height = this.getHeight();
			g.drawImage(backgroundImage, 0, 0, width, height, this);
		} else {
			width = backgroundImage.getWidth(this);
			height = backgroundImage.getHeight(this);
			int x = this.getWidth() / 2 - width / 2;
			int y = this.getHeight() / 2 - height / 2;
			g.drawImage(backgroundImage, x, y, width, height, this);
		}

	}
}
