package jdepend.ui.result.panel;

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
			return "mm/01_凤姐_小于50.jpg";
		} else if (score >= 50 && score < 60) {
			return "mm/02_芙蓉_50-60.jpg";
		} else if (score >= 60 && score < 65) {
			return "mm/03_龅牙珍_陈凯师_60-65.jpg";
		} else if (score >= 65 && score < 70) {
			return "mm/04_吴君如_65-70.jpg";
		} else if (score >= 70 && score < 75) {
			return "mm/05_莫文蔚_素颜_70-75.jpg";
		} else if (score >= 75 && score < 80) {
			return "mm/06_苍井空_75-80.jpg";
		} else if (score >= 80 && score < 85) {
			return "mm/07_邱淑贞_80-85.jpg";
		} else if (score >= 85 && score < 90) {
			return "mm/08_关之琳_85-90.jpg";
		} else if (score >= 90 && score < 95) {
			return "mm/09_高圆圆_90-95.jpg";
		} else if (score >= 95 && score < 100) {
			return "mm/10_林志玲_95-100.jpg";
		} else {
			return null;
		}
	}
}
