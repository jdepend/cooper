package jdepend.ui.command;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.model.component.judge.ComponentJudgeFactory;
import jdepend.model.component.judge.JudgeConfigure;
import jdepend.ui.JDependCooper;

public final class ArgsHighSettingDialog extends JDialog {

	private JDependCooper frame;

	private String group;

	private String command;

	private JCheckBox applyChildren;

	private JTextField childrenKeys;

	private JCheckBox applyLayer;

	private JTextField layer;

	public ArgsHighSettingDialog(JDependCooper frame, String group, String command) {
		super();
		this.frame = frame;
		this.group = group;
		this.command = command;

		this.setTitle("参数高级设置");
		setResizable(false);

		getContentPane().setLayout(new BorderLayout());
		setSize(350, 120);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		JudgeConfigure conf = new JudgeConfigure();
		try {
			conf = (new ComponentJudgeFactory(group, command)).getConfigure();
		} catch (JDependException e1) {
			e1.printStackTrace();
		}

		JPanel panel = new JPanel(new BorderLayout());

		JPanel content = new JPanel(new BorderLayout());

		JPanel left = new JPanel(new GridLayout(2, 1));
		JPanel right = new JPanel(new GridLayout(2, 1));

		left.add(new JLabel("子包名判定器"));

		JPanel childrenPanel = new JPanel(new BorderLayout());
		this.applyChildren = new JCheckBox();
		this.applyChildren.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!ArgsHighSettingDialog.this.applyChildren.isSelected()) {
					ArgsHighSettingDialog.this.childrenKeys.setText(null);
				}
			}

		});
		if (conf.applyChildren) {
			this.applyChildren.setSelected(true);
		}
		childrenPanel.add(BorderLayout.WEST, this.applyChildren);
		this.childrenKeys = new JTextField();
		this.childrenKeys.setToolTipText("以','分割");
		if (conf.getChildrenKeys() != null) {
			this.childrenKeys.setText(conf.getChildrenKeys());
		}
		childrenPanel.add(BorderLayout.CENTER, this.childrenKeys);

		right.add(childrenPanel);

		left.add(new JLabel("层判定器"));

		JPanel layerPanel = new JPanel(new BorderLayout());
		this.applyLayer = new JCheckBox();
		this.applyLayer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!ArgsHighSettingDialog.this.applyLayer.isSelected()) {
					ArgsHighSettingDialog.this.layer.setText(null);
				}
			}

		});
		if (conf.applyLayer) {
			this.applyLayer.setSelected(true);
		}
		layerPanel.add(BorderLayout.WEST, this.applyLayer);
		this.layer = new JTextField();
		if (conf.layer != null) {
			this.layer.setText(String.valueOf(conf.layer));
		}
		layerPanel.add(BorderLayout.CENTER, this.layer);

		right.add(layerPanel);

		content.add(BorderLayout.WEST, left);
		content.add(BorderLayout.CENTER, right);

		JPanel buttonBar = new JPanel();
		buttonBar.add(createSaveButton());
		buttonBar.add(createCloseButton());

		panel.add(BorderLayout.CENTER, content);
		panel.add(BorderLayout.SOUTH, buttonBar);

		getContentPane().add(BorderLayout.CENTER, panel);
	}

	/**
	 * Creates and returns a button with the specified label.
	 * 
	 * @param label
	 *            Button label.
	 * @return Button.
	 */
	private JButton createCloseButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		return button;
	}

	private JButton createSaveButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Save));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JudgeConfigure conf = new JudgeConfigure();
				conf.applyChildren = applyChildren.isSelected();
				conf.applyLayer = applyLayer.isSelected();
				if (childrenKeys.getText() != null) {
					for (String key : childrenKeys.getText().split(",")) {
						conf.childrenKeys.add(key);
					}
				}
				if (layer.getText() != null && layer.getText().length() > 0) {
					conf.layer = Integer.parseInt(layer.getText());
				}
				try {
					(new ComponentJudgeFactory(group, command)).save(conf);
					dispose();
				} catch (JDependException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(frame, "保存失败", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}
}
