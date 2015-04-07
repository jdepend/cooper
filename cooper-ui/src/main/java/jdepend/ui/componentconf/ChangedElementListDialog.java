package jdepend.ui.componentconf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaPackage;
import jdepend.model.component.CustomComponent;
import jdepend.model.component.modelconf.CandidateUtil;
import jdepend.model.component.modelconf.ComponentConf;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.component.modelconf.ComponentModelConfMgr;
import jdepend.model.component.modelconf.GroupComponentModelConf;
import jdepend.ui.JDependCooper;

public final class ChangedElementListDialog extends JDialog {

	private String group;

	private String componentModelConfName;

	private JTable listTable;

	private DefaultTableModel listModel;

	private Map<String, ArrayList<String>> selectedElements;

	private static final String NEW = "新增";
	private static final String DELETED = "已删除";

	public ChangedElementListDialog(JDependCooper frame) {

		this.setTitle("元素变动列表");
		this.setLayout(new BorderLayout());
		setSize(ComponentModelPanel.Width, ComponentModelPanel.Height);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		this.group = JDependUnitMgr.getInstance().getResult().getRunningContext().getGroup();
		this.componentModelConfName = ((CustomComponent) JDependUnitMgr.getInstance().getResult().getRunningContext()
				.getComponent()).getComponentModelConf().getName();

		listModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(listModel);

		listTable = new JTable(sorter);

		sorter.setTableHeader(listTable.getTableHeader());

		listModel.addColumn("位置");
		listModel.addColumn("元素名");
		listModel.addColumn("变动");

		final JPopupMenu addPopupMenu = new JPopupMenu();

		JMenuItem viewClassItem = new JMenuItem("查看类列表");
		viewClassItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedElements.size() != 1 && selectedElements.get(NEW).size() != 1) {
					JOptionPane.showMessageDialog(null, "请选择一个包", "alert", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					String javaPackageName = selectedElements.get(NEW).get(0);
					JavaPackage javaPackage = JDependUnitMgr.getInstance().getResult().getRunningContext()
							.getThePackage(javaPackageName);
					ClassListInThePackageDialog d = new ClassListInThePackageDialog(javaPackage);
					d.setModal(true);
					d.setVisible(true);
				}
			}
		});
		addPopupMenu.add(viewClassItem);

		JMenuItem addItem = new JMenuItem("选择加入的组件");
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final GroupComponentModelConf groupComponentModelConf = ComponentModelConfMgr.getInstance()
						.getTheGroupComponentModelConf(group);
				ComponentModelConf componentModelConf = groupComponentModelConf.getComponentModelConfs().get(
						componentModelConfName);
				JoinCustomComponentConfDialog d = new JoinCustomComponentConfDialog(selectedElements.get(NEW),
						componentModelConf) {
					@Override
					protected void doService() throws JDependException {
						groupComponentModelConf.save();
						deleteSelectedElements();
					}
				};
				d.setModal(true);
				d.setVisible(true);
			}
		});
		addPopupMenu.add(addItem);

		JMenuItem createItem = new JMenuItem("创建新组件");
		createItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateComponentConfDialog d = new CreateComponentConfDialog(selectedElements.get(NEW), false) {
					@Override
					protected void doService(ActionEvent e) throws JDependException {
						GroupComponentModelConf groupComponentModelConf = ComponentModelConfMgr.getInstance()
								.getTheGroupComponentModelConf(group);
						ComponentModelConf componentModelConf = groupComponentModelConf.getComponentModelConfs().get(
								componentModelConfName);
						componentModelConf.addComponentConf(componentname.getText(), getComponentLayer(), units);
						groupComponentModelConf.save();
						deleteSelectedElements();
					}
				};
				d.setModal(true);
				d.setVisible(true);
			}
		});
		addPopupMenu.add(createItem);

		JMenuItem ignoreItem = new JMenuItem("加入忽略列表");
		ignoreItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(listTable, "您是否确认？") == JOptionPane.OK_OPTION) {
					final GroupComponentModelConf groupComponentModelConf = ComponentModelConfMgr.getInstance()
							.getTheGroupComponentModelConf(group);
					ComponentModelConf componentModelConf = groupComponentModelConf.getComponentModelConfs().get(
							componentModelConfName);
					for (String ignoreItem : selectedElements.get(NEW)) {
						componentModelConf.addIgnoreItem(ignoreItem);
					}
					try {
						groupComponentModelConf.save();
						deleteSelectedElements();
					} catch (JDependException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, e1.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		addPopupMenu.add(ignoreItem);

		final JPopupMenu deletePopupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("从组件模型中清除这些包");
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(listTable, "您是否确认？") == JOptionPane.OK_OPTION) {
					try {
						deleteElementsFromConf();
					} catch (JDependException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, e1.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		deletePopupMenu.add(deleteItem);
		listTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				if (e.getButton() == 3) {
					try {
						selectedElements = collect();
						if (selectedElements.size() > 0) {
							if (selectedElements.containsKey(NEW)) {
								addPopupMenu.show(table, e.getX(), e.getY());
							} else {
								deletePopupMenu.show(table, e.getX(), e.getY());
							}
						}
					} catch (JDependException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		Object[] row;

		Map<String, String> diffElements = JDependUnitMgr.getInstance().getResult().getDiffElements();

		for (String elementName : diffElements.keySet()) {
			row = new Object[3];
			row[0] = CandidateUtil.getPlace(elementName);
			row[1] = CandidateUtil.getName(elementName);
			row[2] = diffElements.get(elementName).equals(ComponentModelConf.ADD) ? NEW : DELETED;
			listModel.addRow(row);
		}

		sorter.setSortingStatus(1, TableSorter.DESCENDING);

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add("元素名");
		JTableUtil.fitTableColumns(listTable, fitColNames);

		this.add(BorderLayout.CENTER, new JScrollPane(listTable));

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.createCloseButton());
		this.add(BorderLayout.SOUTH, buttonPanel);
	}

	private Map<String, ArrayList<String>> collect() throws JDependException {

		Map<String, ArrayList<String>> selectedElements = new HashMap<String, ArrayList<String>>();

		int[] rows = listTable.getSelectedRows();
		if (rows == null || rows.length == 0)
			return selectedElements;

		String operation = null;
		String elementName = null;

		for (int i = 0; i < rows.length; i++) {
			elementName = CandidateUtil.getId((String) listTable.getValueAt(rows[i], 0),
					(String) listTable.getValueAt(rows[i], 1));
			operation = (String) listTable.getValueAt(rows[i], 2);
			if (!selectedElements.containsKey(operation)) {
				selectedElements.put(operation, new ArrayList<String>());
			}
			selectedElements.get(operation).add(elementName);
		}
		if (selectedElements.size() > 1) {
			throw new JDependException("不能够包含新增和已删除两类元素！");
		}
		return selectedElements;

	}

	private void deleteElementsFromConf() throws JDependException {
		GroupComponentModelConf groupComponentModelConf = ComponentModelConfMgr.getInstance()
				.getTheGroupComponentModelConf(group);
		ComponentModelConf componentModelConf = groupComponentModelConf.getComponentModelConfs().get(
				componentModelConfName);

		ArrayList<String> deletePackages = this.selectedElements.get(DELETED);
		for (ComponentConf componentConf : componentModelConf.getComponentConfs()) {
			Iterator<String> it = componentConf.getItemIds().iterator();
			while (it.hasNext()) {
				if (deletePackages.contains(it.next())) {
					it.remove();
				}
			}
		}

		groupComponentModelConf.save();
		deleteSelectedElements();
	}

	private void deleteSelectedElements() {
		int[] rows = listTable.getSelectedRows();
		if (rows == null || rows.length == 0) {
			return;
		} else {
			for (int index = rows.length - 1; index >= 0; index--) {
				listModel.removeRow(rows[index]);
			}
		}
		this.selectedElements = new HashMap<String, ArrayList<String>>();
	}

	private Component createCloseButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangedElementListDialog.this.dispose();
			}
		});

		return button;
	}
}
