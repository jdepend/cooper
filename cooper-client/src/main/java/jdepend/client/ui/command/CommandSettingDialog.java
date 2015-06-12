package jdepend.client.ui.command;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jdepend.client.core.local.config.CommandConf;
import jdepend.client.core.local.config.CommandConfMgr;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.model.component.AptitudeComponent;
import jdepend.client.ui.JDependCooper;

/**
 * The <code>AboutDialog</code> displays the about information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class CommandSettingDialog extends JDialog {

	private JDependCooper frame;

	private String label = null;

	JTextField groupname;

	JTextField ordername;

	JTextField labelname;

	JTextField tip;

	JTextField args;

	CommandPanel commandPanel;

	String group;

	/**
	 * Constructs an <code>AboutDialog</code> with the specified parent frame.
	 * 
	 * @param parent
	 *            Parent frame.
	 * @throws JDependException
	 */
	public CommandSettingDialog(JDependCooper parent, CommandPanel commandPanel, String label, String group)
			throws JDependException {
		super(parent);

		this.frame = parent;

		this.group = group;

		if (label == null) {
			setTitle("增加命令");
		} else {
			setTitle("修改命令");
		}

		setResizable(false);

		getContentPane().setLayout(new BorderLayout());
		setSize(500, 250);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		this.commandPanel = commandPanel;

		this.label = label;

		CommandConf info = CommandConfMgr.getInstance().findCommand(group, label);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel content = new JPanel(new BorderLayout());

		JPanel left = new JPanel(new GridLayout(7, 1));
		JPanel right = new JPanel(new GridLayout(7, 1));

		JLabel groupLabel = new JLabel("组名：");
		left.add(groupLabel);

		groupname = new JTextField();
		groupname.setEditable(false);
		groupname.setText(group);

		right.add(groupname);

		JLabel orderLabel = new JLabel("显示顺序：");
		left.add(orderLabel);

		ordername = new JFormattedTextField(NumberFormat.getIntegerInstance());
		if (info != null)
			ordername.setText(String.valueOf(info.order));
		right.add(ordername);

		JLabel labelLabel = new JLabel("名称：");
		left.add(labelLabel);

		labelname = new JTextField();
		if (info != null)
			labelname.setText(info.label);
		right.add(labelname);

		JLabel tipLabel = new JLabel("说明：");
		left.add(tipLabel);
		tip = new JTextField();
		if (info != null)
			tip.setText(info.tip);
		right.add(tip);

		JLabel argsLabel = new JLabel("参数：");
		left.add(argsLabel);
		args = new JTextField();
		if (info != null) {
			args.setText(info.getArgInfo());
		}

		JPanel argsPanel = new JPanel();
		argsPanel.setLayout(new BorderLayout());

		argsPanel.add(args, BorderLayout.CENTER);
		argsPanel.add(this.argsButton(), BorderLayout.EAST);

		right.add(argsPanel);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createSaveButton());
		buttonBar.add(createCloseButton());

		content.add(BorderLayout.WEST, left);

		content.add(BorderLayout.CENTER, right);

		panel.add(BorderLayout.CENTER, content);

		panel.add(BorderLayout.SOUTH, buttonBar);

		getContentPane().add(BorderLayout.CENTER, panel);

	}

	private JButton argsButton() {
		JButton b = new JButton("高级");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (args.getText() == null || args.getText().indexOf(AptitudeComponent.class.getName()) == -1) {
					JOptionPane.showMessageDialog(frame, "该参数没有高级信息", "alert", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				ArgsHighSettingDialog d = new ArgsHighSettingDialog(frame, groupname.getText(), labelname.getText());
				d.setModal(true);
				d.setVisible(true);
			}
		});
		return b;
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
				CommandConf info = new CommandConf();
				info.group = groupname.getText();
				info.order = Integer.parseInt(ordername.getText());
				info.label = labelname.getText();
				info.tip = tip.getText();
				if (args.getText() != null && args.getText().length() > 0) {
					info.args = args.getText().split(" ");
				}
				try {
					if (label != null)
						CommandConfMgr.getInstance().updateCommand(group, label, info);
					else
						CommandConfMgr.getInstance().createCommand(group, info);
					commandPanel.refreshCommand();
					dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
					Component source = (Component) e.getSource();
					JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}
}
