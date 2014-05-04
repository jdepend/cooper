package jdepend.ui.componentconf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

import jdepend.core.config.CommandConf;
import jdepend.core.config.CommandConfMgr;
import jdepend.core.config.GroupConf;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JavaPackage;
import jdepend.model.component.modelconf.ComponentConf;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.ui.JDependCooper;

public final class ComponentModelMgrDialog extends JDialog {

	private GroupConf groupInfo;

	private DefaultListModel componentGroupListModel = new DefaultListModel();

	private DefaultListModel componentListModel = new DefaultListModel();
	// 组件关联的包
	private DefaultListModel packageListModel = new DefaultListModel();

	private String currentComponentModelName;

	private String currentComponent;

	private JDependCooper frame;

	public ComponentModelMgrDialog(JDependCooper parent, String currentGroup) {
		super(parent);

		this.frame = parent;

		this.setTitle("管理组件模型");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				refreshGroup();
			}
		});

		try {
			groupInfo = CommandConfMgr.getInstance().getTheGroup(currentGroup);
		} catch (JDependException e) {
			e.printStackTrace();
		}

		this.setLayout(new BorderLayout());
		setSize(ComponentModelPanel.Width, ComponentModelPanel.Height);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		this.refreshContent(this.getDefaultContent());
	}

	private void refreshGroup() {
		try {
			frame.getGroupPanel().refreshGroup();
		} catch (JDependException e1) {
			e1.printStackTrace();
		}
	}

	private void refreshContent(JPanel content) {
		this.setVisible(false);
		getContentPane().removeAll();
		getContentPane().add(BorderLayout.CENTER, content);
		this.setVisible(true);
	}

	private JPanel getDefaultContent() {

		JPanel framework = new JPanel(new BorderLayout());

		JPanel content = new JPanel(new GridLayout(1, 3));

		JPanel componentGroup = new JPanel(new BorderLayout());

		componentGroup.add(BorderLayout.NORTH, new JLabel(BundleUtil
				.getString(BundleUtil.ClientWin_ComponentModel_Name)
				+ ":"));
		componentGroup.add(BorderLayout.CENTER, this.createComponentGroupList());

		content.add(componentGroup);

		JPanel componentList = new JPanel(new BorderLayout());

		componentList.add(BorderLayout.NORTH, new JLabel(BundleUtil
				.getString(BundleUtil.ClientWin_ComponentModel_ComponentList)
				+ ":"));
		componentList.add(BorderLayout.CENTER, this.createComponentList());

		content.add(componentList);

		JPanel packageList = new JPanel(new BorderLayout());

		packageList.add(BorderLayout.NORTH, new JLabel(BundleUtil
				.getString(BundleUtil.ClientWin_ComponentModel_PackageList)
				+ ":"));
		packageList.add(BorderLayout.CENTER, this.createPackageList());

		content.add(packageList);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createCloseButton());

		framework.add(BorderLayout.CENTER, content);
		framework.add(BorderLayout.SOUTH, buttonBar);

		return framework;
	}

	private Component createPackageList() {

		JList packageList = new JList(packageListModel);

		return new JScrollPane(packageList);
	}

	private Component createComponentGroupList() {

		refreshComponentGroupList();

		final JList componentGroupList = new JList(componentGroupListModel);

		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem addItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Add));
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (groupInfo.getPath() != null) {
					refreshContent(getComponentGroupAddingPanel());
				} else {
					JOptionPane.showMessageDialog(frame, "该命令组还没有配置分析资源的路径！");
				}
			}

		});
		popupMenu.add(addItem);

		JMenuItem updateItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Update));
		updateItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] names = componentGroupList.getSelectedValues();
				if (names == null || names.length == 0) {
					JOptionPane.showMessageDialog(frame, "请选择修改的组件模型！");
					return;
				} else if (names.length > 1) {
					JOptionPane.showMessageDialog(frame, "请选择一个修改的组件模型！");
					return;
				} else {
					refreshContent(getComponentGroupUpdatePanel((String) names[0]));
				}
			}

		});
		popupMenu.add(updateItem);

		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] names = componentGroupList.getSelectedValues();
				if (names == null || names.length == 0) {
					JOptionPane.showMessageDialog(frame, "请选择删除的组件模型！");
					return;
				} else if (JOptionPane.showConfirmDialog(frame, "您是否确认删除？", "提示", JOptionPane.YES_NO_OPTION) == 0) {
					try {
						for (Object name : names) {
							groupInfo.deleteComponentModel((String) name);
							CommandConf info = groupInfo.getCommandInfoByComponentGroup((String) name);
							if (info != null) {
								groupInfo.deleteCommand(info.label);
							}
						}
						refreshComponentGroupList();
					} catch (JDependException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frame, e1.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		});
		popupMenu.add(deleteItem);

		JMenuItem copyItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Copy));
		copyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] names = componentGroupList.getSelectedValues();
				if (names == null || names.length == 0) {
					JOptionPane.showMessageDialog(frame, "请选择拷贝的组件模型！");
					return;
				} else if (names.length > 1) {
					JOptionPane.showMessageDialog(frame, "请选择一个拷贝的组件模型！");
					return;
				} else {
					CreateComponentModelFromCopyDialog d = new CreateComponentModelFromCopyDialog((String) names[0]);
					d.setModal(true);
					d.setVisible(true);
				}
			}

		});
		popupMenu.add(copyItem);

		popupMenu.addSeparator();

		JMenuItem creatCommandItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_CreateCommand));
		creatCommandItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] names = componentGroupList.getSelectedValues();
				if (names == null || names.length == 0) {
					JOptionPane.showMessageDialog(frame, "请选择组件组！");
					return;
				} else if (names.length > 1) {
					JOptionPane.showMessageDialog(frame, "请选择一个组件组！");
					return;
				} else {
					try {
						// 根据新建的组件组信息创建命令
						CommandConf info = CommandConf.create(((String) names[0]), groupInfo.getName());
						groupInfo.insertCommand(info);
						JOptionPane.showMessageDialog(frame, "创建成功！");
					} catch (JDependException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frame, e1.getMessage());
					}
				}
			}

		});
		popupMenu.add(creatCommandItem);

		componentGroupList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JList theList = (JList) e.getSource();
				ListModel model = theList.getModel();

				int index = theList.locationToIndex(e.getPoint());
				if (index >= 0) {
					currentComponentModelName = (String) model.getElementAt(index);
					theList.setSelectedIndex(index);
					refreshComponentList();
				}
				if (e.getButton() == 3)
					popupMenu.show(theList, e.getX(), e.getY());
			}
		});

		return new JScrollPane(componentGroupList);

	}

	private Component createCloseButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				refreshGroup();
				dispose();
			}
		});

		return button;
	}

	private JPanel getComponentGroupAddingPanel() {

		JPanel addingPanel = new JPanel();

		addingPanel.setLayout(new BorderLayout());

		ComponentModelPanel componentPanel = new ComponentModelPanel(frame, this.groupInfo.getPath(), this.groupInfo
				.getName());

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createOKButton(componentPanel, "Add"));
		buttonBar.add(createCancelButton());

		addingPanel.add(BorderLayout.CENTER, componentPanel);
		addingPanel.add(BorderLayout.SOUTH, buttonBar);

		return addingPanel;
	}

	private JPanel getComponentGroupUpdatePanel(String componentModelName) {

		JPanel addingPanel = new JPanel();

		addingPanel.setLayout(new BorderLayout());

		ComponentModelPanel componentPanel = new ComponentModelPanel(frame, this.groupInfo.getPath(), this.groupInfo
				.getName(), componentModelName);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createOKButton(componentPanel, "Update"));
		buttonBar.add(createCancelButton());

		addingPanel.add(BorderLayout.CENTER, componentPanel);
		addingPanel.add(BorderLayout.SOUTH, buttonBar);

		return addingPanel;
	}

	private Component createOKButton(final ComponentModelPanel componentPanel, final String operation) {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_OK));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					if (operation.equals("Add")) {
						create(componentPanel);
					} else {
						update(componentPanel);
					}
					refreshContent(getDefaultContent());
				} catch (Exception ex) {
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

	private void create(ComponentModelPanel componentPanel) throws JDependException {

		componentPanel.validateData();

		ComponentModelConf componentModel = componentPanel.getComponentModelConf();

		if (groupInfo.getComponentModelConfNames().contains(componentModel.getName())) {
			throw new JDependException("组件组名称已经存在!");
		}

		// 保存组件组信息
		groupInfo.addComponentModel(componentModel);
		// 根据新建的组件组信息创建命令
		CommandConf info = CommandConf.create(componentModel.getName(), groupInfo.getName());
		groupInfo.insertCommand(info);

	}

	private void update(ComponentModelPanel componentPanel) throws JDependException {
		componentPanel.validateData();
		// 保存组件组信息
		this.groupInfo.insertComponentGroups();
	}

	private Component createCancelButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Cancel));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				refreshContent(getDefaultContent());
			}
		});

		return button;
	}

	private Component createComponentList() {

		final JList componentList = new JList(componentListModel);

		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem detailItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewDetail));
		detailItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				detailComponentConf(currentComponent);
			}

		});
		popupMenu.add(detailItem);

		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(frame, "您是否确认删除？", "提示", JOptionPane.YES_NO_OPTION) == 0) {
					try {
						deleteComponentConf(currentComponent);
						refreshComponentGroupList();
					} catch (JDependException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog((Component) e.getSource(), e1.getMessage(), "alert",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		});
		popupMenu.add(deleteItem);

		componentList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JList theList = (JList) e.getSource();
				ListModel model = theList.getModel();

				int index = theList.locationToIndex(e.getPoint());
				if (index >= 0) {
					currentComponent = (String) model.getElementAt(index);
					theList.setSelectedIndex(index);
					refreshPackageList();
				}
				if (e.getButton() == 3)
					popupMenu.show(theList, e.getX(), e.getY());
			}
		});

		return new JScrollPane(componentList);

	}

	private void deleteComponentConf(String currentComponent) throws JDependException {
		this.groupInfo.getTheComponentModelConf(this.currentComponentModelName).deleteComponentConf(currentComponent);
		this.groupInfo.insertComponentGroups();

	}

	private void detailComponentConf(String currentComponent) {

		ComponentConf componentConf = this.groupInfo.getTheComponentModelConf(this.currentComponentModelName)
				.getTheComponentConf(currentComponent);

		UpdateComponentConfDialog d = new UpdateComponentConfDialog(this.groupInfo.getName(),
				this.currentComponentModelName, componentConf);
		d.setModal(true);
		d.setVisible(true);

	}

	private void refreshComponentGroupList() {

		componentGroupListModel.removeAllElements();

		Collection<String> componentGroupNames = new ArrayList<String>();

		componentGroupNames = this.groupInfo.getComponentModelConfNames();

		for (String componentGroupName : componentGroupNames) {
			componentGroupListModel.addElement(componentGroupName);
		}

		if (componentGroupListModel.size() > 0) {
			currentComponentModelName = (String) componentGroupListModel.get(0);
			refreshComponentList();
		}

	}

	private void refreshComponentList() {

		componentListModel.removeAllElements();

		ComponentModelConf componentGroup = this.groupInfo.getTheComponentModelConf(currentComponentModelName);

		for (String componentName : componentGroup.getComponentConfNames()) {
			componentListModel.addElement(componentName);
		}

		if (componentListModel.size() > 0) {
			currentComponent = (String) componentListModel.get(0);
			refreshPackageList();
		}
	}

	private void refreshPackageList() {

		packageListModel.removeAllElements();

		ComponentModelConf componentGroup = this.groupInfo.getTheComponentModelConf(currentComponentModelName);

		for (String packageName : componentGroup.getTheComponentConf(currentComponent).getPackages())
			packageListModel.addElement(packageName);

	}

	class CreateComponentModelFromCopyDialog extends JDialog {
		private String source;
		private JTextField componentModelName;

		public CreateComponentModelFromCopyDialog(String source) {
			this.source = source;

			this.setLayout(new BorderLayout());
			setSize(250, 120);
			this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

			JPanel content = new JPanel(new BorderLayout());

			JLabel componentModelLabel = new JLabel("组件模型名：");
			content.add(BorderLayout.WEST, componentModelLabel);

			componentModelName = new JTextField();
			content.add(BorderLayout.CENTER, componentModelName);

			JPanel buttonBar = new JPanel(new FlowLayout());
			buttonBar.add(createOkButton());
			buttonBar.add(createCancelButton());

			this.add(BorderLayout.CENTER, content);

			this.add(BorderLayout.SOUTH, buttonBar);
		}

		protected Component createOkButton() {
			JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_OK));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (componentModelName.getText() == null || componentModelName.getText().length() == 0) {
						JOptionPane.showMessageDialog((Component) e.getSource(), "请录入组件模型名称", "alert",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					String modelName = componentModelName.getText();
					if (groupInfo.getComponentModelConfNames().contains(modelName)) {
						JOptionPane.showMessageDialog((Component) e.getSource(), "组件模型名称重复，请从新录入", "alert",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						ComponentModelConf sourceModel = groupInfo.getTheComponentModelConf(source);
						ComponentModelConf newModel = sourceModel.clone();
						newModel.setName(modelName);
						groupInfo.addComponentModel(newModel);
						groupInfo.getGroupComponentModelConf().save();
						refreshComponentGroupList();
						CreateComponentModelFromCopyDialog.this.dispose();
					} catch (JDependException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog((Component) e.getSource(), e1.getMessage(), "alert",
								JOptionPane.ERROR_MESSAGE);
					} catch (CloneNotSupportedException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog((Component) e.getSource(), e1.getMessage(), "alert",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			return button;
		}

		private Component createCancelButton() {
			JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Cancel));
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					CreateComponentModelFromCopyDialog.this.dispose();
				}
			});

			return button;
		}
	}

}
