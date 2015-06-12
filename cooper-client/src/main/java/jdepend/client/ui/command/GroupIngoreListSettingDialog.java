package jdepend.client.ui.command;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jdepend.client.core.local.config.CommandConfMgr;
import jdepend.client.core.local.config.GroupConf;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.client.ui.JDependCooper;

/**
 * The <code>GroupIngoreListSettingDialog</code> displays the about information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class GroupIngoreListSettingDialog extends JDialog {

	private JDependCooper frame;

	private String currentGroup;

	private JTextArea filteredPackages;

	/**
	 * Constructs an <code>AboutDialog</code> with the specified parent frame.
	 * 
	 * @param parent
	 *            Parent frame.
	 */
	public GroupIngoreListSettingDialog(JDependCooper parent, String name) {
		super(parent);

		this.frame = parent;

		this.currentGroup = name;

		setTitle("增加组");

		setResizable(false);

		getContentPane().setLayout(new BorderLayout());
		setSize(650, 380);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel content = new JPanel(new BorderLayout());

		panel.add(BorderLayout.WEST, new JLabel("过滤列表："));

		filteredPackages = new JTextArea();

		GroupConf group = null;
		try {
			group = CommandConfMgr.getInstance().getTheGroup(currentGroup);
		} catch (JDependException e1) {
			e1.printStackTrace();
		}
		if (group != null) {
			StringBuilder filteredPackagesBuffer = new StringBuilder();
			for (String filteredPackage : group.getFilteredPackages()) {
				filteredPackagesBuffer.append(filteredPackage);
				filteredPackagesBuffer.append("\n");
			}
			filteredPackages.setText(filteredPackagesBuffer.toString());
			filteredPackages.setCaretPosition(0);
		}
		panel.add(new JScrollPane(filteredPackages));

		content.add(BorderLayout.CENTER, panel);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createSaveButton());
		buttonBar.add(createCloseButton());

		this.add(BorderLayout.CENTER, content);
		this.add(BorderLayout.SOUTH, buttonBar);
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
				saveIngoreList(e);
			}
		});

		return button;
	}

	private void saveIngoreList(ActionEvent e) {

		List<String> filteredPackageList = new ArrayList<String>();

		for (String filteredPackage : filteredPackages.getText().split("\n")) {
			filteredPackageList.add(filteredPackage);
		}
		try {
			GroupConf group = CommandConfMgr.getInstance().getTheGroup(currentGroup);
			group.setFilteredPackages(filteredPackageList);
			CommandConfMgr.getInstance().updateGroups();
			frame.getGroupPanel().refreshGroup();
			dispose();
		} catch (Exception ex) {
			Component source = (Component) e.getSource();
			JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
		}
	}
}
