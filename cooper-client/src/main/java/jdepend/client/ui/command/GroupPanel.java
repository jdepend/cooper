package jdepend.client.ui.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import jdepend.client.core.local.command.CommandAdapterMgr;
import jdepend.client.core.local.config.CommandConfMgr;
import jdepend.client.core.local.config.GroupConf;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.action.AddGroupWizardAction;
import jdepend.client.ui.componentconf.ComponentModelMgrDialog;
import jdepend.client.ui.framework.UIPropertyConfigurator;

public class GroupPanel extends JPanel {

	private JDependCooper parent;

	private String currentGroup;

	private int index;

	private JTabbedPane tabPane = new JTabbedPane();

	public GroupPanel(JDependCooper parent) throws JDependException {

		this.parent = parent;

		this.setLayout(new BorderLayout());

		this.refreshGroup();

		this.initPopupMenu(tabPane);

		this.add(tabPane);
	}

	private JPopupMenu initPopupMenu(JTabbedPane tabPane) {

		final JPopupMenu popupMenu = getJPopupMenu();

		tabPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				JTabbedPane obj = (JTabbedPane) e.getSource();
				index = obj.getSelectedIndex();
				if (index != -1 && obj.getComponentAt(index) instanceof CommandPanel) {
					currentGroup = ((CommandPanel) obj.getComponentAt(index)).group;
				}
				UIPropertyConfigurator.getInstance().setGroupIndex(index);
				if (e.getButton() == 3)
					popupMenu.show(obj, e.getX(), e.getY());
			}
		});

		return popupMenu;
	}

	private JPopupMenu getJPopupMenu() {

		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem addWizardItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_AddGroup));
		addWizardItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addWizard(e);
			}
		});
		popupMenu.add(addWizardItem);

		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_DeleteGroup));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentGroup != null)
					delete();
			}
		});
		popupMenu.add(deleteItem);

		popupMenu.addSeparator();

		JMenuItem refreshItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_RefreshGroup));
		refreshItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refreshGroup();
				} catch (JDependException e1) {
					e1.printStackTrace();
					parent.showStatusError(e1.getMessage());
				}
			}
		});
		popupMenu.add(refreshItem);

		JMenuItem viewItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewGroup));
		viewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewGroup();
			}
		});
		popupMenu.add(viewItem);

		JMenuItem hideItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_HideGroup));
		hideItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					hideGroup();
				} catch (JDependException e1) {
					e1.printStackTrace();
					parent.showStatusError(e1.getMessage());
				}
			}
		});
		popupMenu.add(hideItem);

		JMenuItem selectItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SelectShowGroup));
		selectItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectGroup();
			}
		});
		popupMenu.add(selectItem);

		popupMenu.addSeparator();

		JMenuItem mgrComponentGroupItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ManageComponentModel));
		mgrComponentGroupItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mgrComponentGroup();
			}
		});
		popupMenu.add(mgrComponentGroupItem);

		return popupMenu;
	}

	private void addWizard(ActionEvent e) {
		(new AddGroupWizardAction(parent)).actionPerformed(e);
	}

	private void delete() {

		try {
			if (JOptionPane.showConfirmDialog(parent, "您是否确认删除？", "提示", JOptionPane.YES_NO_OPTION) == 0) {
				CommandConfMgr.getInstance().deleteGroup(currentGroup);
			} else {
				return;
			}
			refreshGroup();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(parent, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void refreshGroup() throws JDependException {

		if (UIPropertyConfigurator.getInstance().getGroupTabLayoutPolicy()
				.equals(UIPropertyConfigurator.GroupTabLayoutPolicy_SCROLL_TAB_LAYOUT)) {
			tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		} else {
			tabPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		}

		CommandConfMgr.getInstance().refresh();

		this.refreshPanel();

	}

	public void refreshPanel() throws JDependException {

		tabPane.setVisible(false);
		tabPane.removeAll();

		if (CommandConfMgr.getInstance().getGroupNames().size() > 0) {
			Iterator<String> it = CommandConfMgr.getInstance().getGroupNames().iterator();

			CommandPanel commandPanel;

			String group;

			while (it.hasNext()) {

				group = it.next();

				if (CommandConfMgr.getInstance().getTheGroup(group).isVisible()) {

					commandPanel = new CommandPanel(parent, group);

					tabPane.addTab(group, commandPanel);
				}
			}
			// 设置current tab
			if (tabPane.getTabCount() == 0) {
				index = -1;
			} else {
				Integer groupIndex = UIPropertyConfigurator.getInstance().obtainGroupIndex();
				if (groupIndex != null && groupIndex > -1 && groupIndex < tabPane.getTabCount()) {
					index = groupIndex;
				} else {
					index = 0;
				}
				tabPane.setSelectedIndex(index);
			}
			if (index != -1 && tabPane.getTabCount() > index) {
				tabPane.setSelectedIndex(index);
				currentGroup = ((CommandPanel) tabPane.getComponentAt(index)).group;
			}
			UIPropertyConfigurator.getInstance().setGroupIndex(index);
		} else {
			JLabel tip = new JLabel("右键选择“增加组”");
			JPanel content = new JPanel(new BorderLayout());
			content.add(BorderLayout.CENTER, tip);
			content.setBackground(new Color(255, 255, 255));

			tabPane.addTab("提示", content);
		}
		tabPane.setVisible(true);
	}

	public void viewGroup() {
		CreateGroupDialog d = new CreateGroupDialog(this.parent, currentGroup);
		d.setModal(true);

		d.setVisible(true);
	}

	public void hideGroup() throws JDependException {

		GroupConf group = CommandConfMgr.getInstance().getTheGroup(currentGroup);
		group.setVisible(false);
		CommandConfMgr.getInstance().updateGroup(group);

		this.refreshGroup();
	}

	public void selectGroup() {
		SelectGroupDialog d = new SelectGroupDialog(this.parent);
		d.setModal(true);

		d.setVisible(true);
	}

	private void mgrComponentGroup() {

		ComponentModelMgrDialog d = new ComponentModelMgrDialog(this.parent, currentGroup);
		d.setModal(true);

		d.setVisible(true);
	}
}
