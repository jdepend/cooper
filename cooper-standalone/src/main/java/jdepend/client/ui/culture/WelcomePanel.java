package jdepend.client.ui.culture;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import jdepend.framework.ui.panel.ImagePanel;

public final class WelcomePanel extends ImagePanel {

	private boolean visibleTitle = true;

	public WelcomePanel() {

		super("mascot.jpg");

		this.setLayout(new GridBagLayout());

		final JLabel titleLabel = new JLabel("MINI的历史-四十年的辉煌");
		titleLabel.setFont(new Font("dialog", Font.BOLD, 24));

		this.add(titleLabel, createConstraints(1, 1));

		this.add(new JLabel("\n"), createConstraints(1, 2));

		final JLabel name1Label = new JLabel("1957年Mini的原始设计者，亚力克.依斯哥尼爵士(Alec Issigonis)曾立下宏愿：");
		name1Label.setFont(new Font("dialog", Font.PLAIN, 18));
		this.add(name1Label, createConstraints(1, 3));

		final JLabel name2Label = new JLabel("要设计出一款经济，人人都买得起，且能够搭载四个人的汽车。");
		name2Label.setFont(new Font("dialog", Font.PLAIN, 18));
		this.add(name2Label, createConstraints(1, 4));

		this.add(new JLabel("\n"), createConstraints(1, 5));

		final JLabel name3Label = new JLabel("1958年第一部Mini原型车诞生，并于一年后开始量产。");
		name3Label.setFont(new Font("dialog", Font.PLAIN, 18));
		this.add(name3Label, createConstraints(1, 6));

		this.add(new JLabel("\n"), createConstraints(1, 7));

		final JLabel name4Label = new JLabel("1959年Mini正式发表！当时订价500马克(折合美金约$786.75)。媒体大肆报道，");
		name4Label.setFont(new Font("dialog", Font.PLAIN, 18));
		this.add(name4Label, createConstraints(1, 8));

		final JLabel name5Label = new JLabel("群众更为之哗然，毕竟从未出现过一部像MINI这样的车!");
		name5Label.setFont(new Font("dialog", Font.PLAIN, 18));
		this.add(name5Label, createConstraints(1, 9));

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (visibleTitle) {
					titleLabel.setVisible(false);
					name1Label.setVisible(false);
					name2Label.setVisible(false);
					name3Label.setVisible(false);
					name4Label.setVisible(false);
					name5Label.setVisible(false);
					visibleTitle = false;
				} else {
					titleLabel.setVisible(true);
					name1Label.setVisible(true);
					name2Label.setVisible(true);
					name3Label.setVisible(true);
					name4Label.setVisible(true);
					name5Label.setVisible(true);
					visibleTitle = true;
				}
			}
		});
	}

	private GridBagConstraints createConstraints(int x, int y) {

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;

		return constraints;
	}

}
