package jdepend.client.ui.result.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.ui.panel.ImagePanel;

public class MMDialog extends CooperDialog {

	public MMDialog(float score) {

		super("你的分数对应的美女是");

		ImagePanel imagePanel = new ImagePanel(getImageName(score));
		imagePanel.setStretch(false);
		
		this.add(BorderLayout.CENTER, imagePanel);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createCloseButton());

		this.add(BorderLayout.SOUTH, buttonBar);

	}

	private String getImageName(float score) {

		if (score < 50) {
			return "mm/01.jpg";
		} else if (score >= 50 && score < 60) {
			return "mm/02.jpg";
		} else if (score >= 60 && score < 65) {
			return "mm/03.jpg";
		} else if (score >= 65 && score < 70) {
			return "mm/04.jpg";
		} else if (score >= 70 && score < 75) {
			return "mm/05.jpg";
		} else if (score >= 75 && score < 80) {
			return "mm/06.jpg";
		} else if (score >= 80 && score < 85) {
			return "mm/07.jpg";
		} else if (score >= 85 && score < 90) {
			return "mm/08.jpg";
		} else if (score >= 90 && score < 95) {
			return "mm/09.jpg";
		} else if (score >= 95 && score < 100) {
			return "mm/10.jpg";
		} else {
			return null;
		}
	}
}
