package jdepend.framework.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.VersionUtil;

/**
 * The <code>AboutDialog</code> displays the about information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class AboutDialog extends JDialog {

	/**
	 * Constructs an <code>AboutDialog</code> with the specified parent frame.
	 * 
	 * @param parent
	 *            Parent frame.
	 */
	public AboutDialog(JFrame parent) {
		super(parent);

		setTitle("关于");

		setResizable(false);

		getContentPane().setLayout(new FlowLayout());

		setSize(300, 220);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel titleLabel = new JLabel("Cooper");
		titleLabel.setFont(new Font("dialog", Font.BOLD, 18));

		JLabel versionLabel = new JLabel("Version：" + VersionUtil.getVersion());
		versionLabel.setFont(new Font("dialog", Font.PLAIN, 12));

		JLabel buildDateLabel = new JLabel("Build Date： " + VersionUtil.getBuildDate());
		buildDateLabel.setFont(new Font("dialog", Font.PLAIN, 12));

		JLabel nameLabel = new JLabel("王德刚");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 12));

		JLabel companyLabel = new JLabel("Neusoft, Inc.");
		companyLabel.setFont(new Font("dialog", Font.PLAIN, 12));

		final JLabel httpLabel = new JLabel(
				"<html><a href='https://github.com/jdepend/cooper'>jdepend-cooper</a></html>");
		httpLabel.setFont(new Font("dialog", Font.PLAIN, 12));
		httpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		httpLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URI("https://github.com/jdepend/cooper"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton closeButton = createButton(BundleUtil.getString(BundleUtil.Command_Close));

		panel.add(titleLabel, createConstraints(1, 1));

		panel.add(versionLabel, createConstraints(1, 2));

		panel.add(buildDateLabel, createConstraints(1, 3));

		panel.add(new JLabel(" "), createConstraints(1, 4));

		panel.add(nameLabel, createConstraints(1, 5));

		panel.add(companyLabel, createConstraints(1, 6));

		panel.add(httpLabel, createConstraints(1, 7));

		panel.add(new JLabel(" "), createConstraints(1, 8));

		panel.add(closeButton, createConstraints(1, 9));

		getContentPane().add(panel);

		ImageIcon background = new ImageIcon(JDependUIUtil.getImage("about.png"));// 背景图片
		JLabel label = new JLabel(background);// 把背景图片显示在一个标签里面
		// 把标签的大小位置设置为图片刚好填充整个面板
		label.setBounds(230, 0, background.getIconWidth(), background.getIconHeight());
		label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				VersionDialog d = new VersionDialog();
				d.setModal(true);
				d.setVisible(true);
			}
		});
		// 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
		((JPanel) this.getContentPane()).setOpaque(false);
		this.getLayeredPane().setLayout(null);
		// 把背景图片添加到分层窗格的最底层作为背景
		this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

		this.setBackground(new Color(255, 255, 255));
		panel.setBackground(new Color(255, 255, 255));

	}

	/**
	 * Creates and returns a button with the specified label.
	 * 
	 * @param label
	 *            Button label.
	 * @return Button.
	 */
	private JButton createButton(String label) {

		JButton button = new JButton(label);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		return button;
	}

	/**
	 * Creates and returns a grid bag constraint with the specified x and y
	 * values.
	 * 
	 * @param x
	 *            X-coordinate.
	 * @param y
	 *            Y-coordinate.
	 * @return GridBagConstraints
	 */
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
