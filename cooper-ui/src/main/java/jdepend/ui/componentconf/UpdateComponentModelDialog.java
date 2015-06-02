package jdepend.ui.componentconf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jdepend.core.local.config.CommandConfMgr;
import jdepend.core.local.config.GroupConf;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.ui.JDependCooper;

public final class UpdateComponentModelDialog extends JDialog {

	private GroupConf groupInfo;

	private JDependCooper frame;

	public UpdateComponentModelDialog(JDependCooper parent, String currentGroup, String componentModelName) {
		super(parent);

		this.frame = parent;
		this.setTitle("修改组件模型");

		setSize(ComponentModelPanel.Width, ComponentModelPanel.Height);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		try {
			groupInfo = CommandConfMgr.getInstance().getTheGroup(currentGroup);
		} catch (JDependException e) {
			e.printStackTrace();
		}

		this.setLayout(new BorderLayout());

		ComponentModelPanel componentPanel = new ComponentModelPanel(frame, this.groupInfo.getPath(), this.groupInfo
				.getName(), componentModelName);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createOKButton(componentPanel));
		buttonBar.add(createCancelButton());

		this.add(BorderLayout.CENTER, componentPanel);
		this.add(BorderLayout.SOUTH, buttonBar);
	}

	private Component createOKButton(final ComponentModelPanel componentPanel) {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_OK));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					componentPanel.validateData();
					// 保存组件组信息
					groupInfo.insertComponentGroups();
					UpdateComponentModelDialog.this.dispose();
				} catch (JDependException ex) {
					ex.printStackTrace();
					if (ex.getMessage() != null) {
						Component source = (Component) e.getSource();
						JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		return button;
	}

	private Component createCancelButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Cancel));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateComponentModelDialog.this.dispose();
			}
		});

		return button;
	}

}
