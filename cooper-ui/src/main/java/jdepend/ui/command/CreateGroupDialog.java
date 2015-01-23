package jdepend.ui.command;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jdepend.core.config.CommandConfMgr;
import jdepend.core.config.GroupConf;
import jdepend.framework.util.BundleUtil;
import jdepend.ui.JDependCooper;
import jdepend.ui.componentconf.ComponentModelPanel;
import jdepend.ui.wizard.GroupSettingPanel;

/**
 * The <code>CreateGroupDialog</code> displays the about information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class CreateGroupDialog extends JDialog {

	JDependCooper frame;

	GroupSettingPanel groupPanel;

	String currentGroup;

	/**
	 * Constructs an <code>CreateGroupDialog</code> with the specified parent
	 * frame.
	 * 
	 * @param parent
	 *            Parent frame.
	 */
	public CreateGroupDialog(JDependCooper parent, String name) {
		super(parent);

		this.frame = parent;

		this.currentGroup = name;

		setTitle("增加组");

		setResizable(false);

		getContentPane().setLayout(new BorderLayout());
		setSize(ComponentModelPanel.Width, ComponentModelPanel.Height);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel content = new JPanel(new BorderLayout());

		groupPanel = new GroupSettingPanel(frame, currentGroup);

		content.add(BorderLayout.CENTER, groupPanel);

		JPanel buttonBar = new JPanel(new FlowLayout());
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
				saveGroup(e);
			}
		});

		return button;
	}

	private void saveGroup(ActionEvent e) {
		String group = groupPanel.getGroupname().getText();
		String path = groupPanel.getPathname().getText();
		if (path != null) {
			path = path.replaceAll("\n", "");
		}
		String srcPath = groupPanel.getSrcPathName().getText();
		if (srcPath != null) {
			srcPath = srcPath.replaceAll("\n", "");
		}
		List<String> filteredPackages = groupPanel.getFilteredPackages();

		String attribute = groupPanel.getAttribute().getText();
		try {
			if (this.currentGroup == null) {
				CommandConfMgr.getInstance().createGroup(group, path, srcPath, filteredPackages, attribute);
			} else {
				GroupConf gc = CommandConfMgr.getInstance().getTheGroup(this.currentGroup);

				gc.setPath(path);
				gc.setSrcPath(srcPath);
				gc.setFilteredPackages(filteredPackages);
				gc.setAttribute(attribute);

				CommandConfMgr.getInstance().updateGroup(gc);
			}
			frame.getGroupPanel().refreshGroup();
			dispose();
		} catch (Exception ex) {
			ex.printStackTrace();
			Component source = (Component) e.getSource();
			JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
		}
	}
}
