package jdepend.client.ui.result.panel;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.ui.panel.ImagePanel;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;

public class MMDialog extends CooperDialog {

	public MMDialog() {

		super("送个福利");

		this.refresh();

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createNextButton());
		buttonBar.add(createCloseButton());

		this.add(BorderLayout.SOUTH, buttonBar);

	}

	protected JButton createNextButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_NextStep));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		return button;
	}

	public void refresh() {
		ImagePanel imagePanel = new ImagePanel(getImageNameWithRandom());
		imagePanel.setStretch(false);
		imagePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		imagePanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				refresh();
			}
		});

		this.add(BorderLayout.CENTER, imagePanel);

		this.setVisible(true);
	}

	private String getImageNameWithScore() {

		float score = JDependUnitMgr.getInstance().getResult().getScore();

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

	private String getImageNameWithRandom() {
		String fileNumber;
		int randomNumber = (int) Math.round(Math.random() * 10);
		if (randomNumber < 10) {
			fileNumber = "0" + randomNumber;
		} else {
			fileNumber = String.valueOf(randomNumber);
		}

		return "mm/" + fileNumber + ".jpg";
	}

}
