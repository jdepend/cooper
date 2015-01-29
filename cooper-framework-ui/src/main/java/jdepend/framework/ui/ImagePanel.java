package jdepend.framework.ui;

import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JPanel;

import jdepend.framework.ui.JDependUIUtil;

public class ImagePanel extends JPanel {

	private String imageName;

	private boolean stretch = true;

	public ImagePanel(String imageName) {
		this.imageName = imageName;

		this.setLayout(new GridBagLayout());
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
		} else {
			width = backgroundImage.getWidth(this);
			height = backgroundImage.getHeight(this);
		}
		g.drawImage(backgroundImage, 0, 0, width, height, this);
	}
}
