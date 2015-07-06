package jdepend.client.ui.command;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;

import jdepend.client.core.config.CommandConf;
import jdepend.client.core.config.CommandConfMgr;
import jdepend.client.ui.JDependCooper;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;

public class CommandPanel extends JPanel {

	JDependCooper frame;

	CommandList list;

	DefaultListModel model;

	String group;

	String command;

	Map<String, CommandConf> commands;

	int index;

	public CommandPanel(JDependCooper parent, String group) throws JDependException {

		this.frame = parent;

		this.group = group;

		setLayout(new BorderLayout());

		this.initCommandList();

		JPanel content = new JPanel(new BorderLayout());
		content.add(BorderLayout.NORTH, list);
		content.setBackground(new Color(255, 255, 255));

		add(content);

	}

	private void initCommandList() throws JDependException {

		model = new DefaultListModel();
		list = new CommandList(model, frame);
		list.setGroup(this.group);

		this.initCommand();

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem runItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Run));
		runItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (command != null)
					try {
						run();
					} catch (JDependException e1) {
						e1.printStackTrace();
						frame.showStatusError(e1.getMessage());
					}
			}
		});
		popupMenu.add(runItem);

		popupMenu.addSeparator();

		JMenuItem addItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_AddCommand));
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					add();
				} catch (JDependException e1) {
					e1.printStackTrace();
					frame.showStatusError(e1.getMessage());
				}
			}
		});
		popupMenu.add(addItem);

		JMenuItem editItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_EditCommand));
		editItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (command != null)
					try {
						edit();
					} catch (JDependException e1) {
						e1.printStackTrace();
						frame.showStatusError(e1.getMessage());
					}
			}
		});
		popupMenu.add(editItem);

		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_DeleteCommand));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (command != null)
					try {
						delete();
					} catch (JDependException e1) {
						e1.printStackTrace();
						frame.showStatusError(e1.getMessage());
					}
			}
		});
		popupMenu.add(deleteItem);

		popupMenu.addSeparator();

		JMenuItem viewHistoryItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewReportHistory));
		viewHistoryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (command != null)
					viewReportHistory();
			}
		});
		popupMenu.add(viewHistoryItem);

		JMenuItem viewExecuteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewExecuteHistory));
		viewExecuteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (command != null)
					viewExecuteHistory();
			}
		});
		popupMenu.add(viewExecuteItem);

		popupMenu.addSeparator();

		JMenuItem profileSettingItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ProfileSetting));
		profileSettingItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				profileSetting();
			}
		});
		popupMenu.add(profileSettingItem);

		popupMenu.addSeparator();

		JMenuItem refreshItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Refresh));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refreshCommand();
				} catch (JDependException e1) {
					e1.printStackTrace();
					frame.showStatusError(e1.getMessage());
				}
			}
		});
		popupMenu.add(refreshItem);

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JList theList = (JList) e.getSource();
				ListModel model = theList.getModel();

				index = theList.locationToIndex(e.getPoint());
				if (index >= 0) {
					index = theList.locationToIndex(e.getPoint());
					command = (String) model.getElementAt(index);
					theList.setSelectedIndex(index);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				JList theList = (JList) e.getSource();
				if (e.getButton() == 3) {
					popupMenu.show(theList, e.getX(), e.getY());
				}
				if (e.getClickCount() == 2) {
					try {
						run();
					} catch (JDependException e1) {
						e1.printStackTrace();
						frame.showStatusError(e1.getMessage());
					}
				}
			}
		});
	}

	private void run() throws JDependException {
		CommandAction action = new CommandAction(frame, group, command);
		action.actionPerformed(null);
	}

	private void edit() throws JDependException {
		CommandSettingDialog d = new CommandSettingDialog(frame, this, command, this.group);
		d.setModal(true);
		d.setVisible(true);
	}

	private void add() throws JDependException {
		CommandSettingDialog d = new CommandSettingDialog(frame, this, null, this.group);
		d.setModal(true);
		d.setVisible(true);

	}

	private void delete() throws JDependException {
		if (JOptionPane.showConfirmDialog(frame, "您是否确认删除？", "提示", JOptionPane.YES_NO_OPTION) == 0) {
			CommandConfMgr.getInstance().deleteCommand(group, command);
			refreshCommand();
		}
	}

	private void profileSetting() {
		CommandProfileSettingDialog d = new CommandProfileSettingDialog(frame, group, command);
		d.setModal(true);
		d.setVisible(true);
	}

	private void viewReportHistory() {
		frame.getPropertyPanel().showReportHistory(group, command);
	}

	private void viewExecuteHistory() {
		frame.getPropertyPanel().showExecuteHistory(group, command);
	}

	private void initCommand() throws JDependException {

		commands = CommandConfMgr.getInstance().getCommands(this.group);
		if (commands != null) {
			Iterator<String> it = commands.keySet().iterator();
			while (it.hasNext())
				model.addElement(commands.get(it.next()).label);
		}
	}

	public void refreshCommand() throws JDependException {
		CommandConfMgr.getInstance().refresh();
		model.removeAllElements();

		this.initCommand();
	}

	class sequence {
		Integer order = 0;
	}

	class running {
		boolean running = false;
	}
}
