package jdepend.ui.componentconf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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
import jdepend.model.component.modelconf.ComponentConf;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.component.modelconf.ComponentModelConfMgr;
import jdepend.model.component.modelconf.GroupComponentModelConf;
import jdepend.model.component.modelconf.JavaPackageComponentConf;
import jdepend.model.component.modelconf.JavaPackageComponentModelConf;
import jdepend.report.util.ReportConstant;
import jdepend.ui.JDependCooper;

public final class ChangedPackageListDialog extends JDialog {

	private String group;

	private String componentModelConfName;

	private JTable listTable;

	private DefaultTableModel listModel;

	private Map<String, ArrayList<String>> selectedPackages;

	public ChangedPackageListDialog(JDependCooper frame) {

		this.setTitle("包变动列表");
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

		listModel.addColumn("包名");
		listModel.addColumn("变动");

		final JPopupMenu addPopupMenu = new JPopupMenu();

		JMenuItem viewClassItem = new JMenuItem("查看类列表");
		viewClassItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedPackages.size() != 1 && selectedPackages.get("新增").size() != 1) {
					JOptionPane.showMessageDialog(null, "请选择一个包", "alert", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					String javaPackageName = selectedPackages.get("新增").get(0);
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
				ComponentModelConf componentModelConf1 = groupComponentModelConf.getComponentModelConfs().get(
						componentModelConfName);
				JavaPackageComponentModelConf componentModelConf = (JavaPackageComponentModelConf) componentModelConf1;
				JoinCustomComponentConfDialog d = new JoinCustomComponentConfDialog(selectedPackages.get("新增"),
						componentModelConf) {
					@Override
					protected void doService() throws JDependException {
						groupComponentModelConf.save();
						deleteSelectedPackages();
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
				CreateComponentConfDialog d = new CreateComponentConfDialog(selectedPackages.get("新增"), false) {
					@Override
					protected void doService(ActionEvent e) throws JDependException {
						GroupComponentModelConf groupComponentModelConf = ComponentModelConfMgr.getInstance()
								.getTheGroupComponentModelConf(group);
						ComponentModelConf componentModelConf = groupComponentModelConf.getComponentModelConfs().get(
								componentModelConfName);
						((JavaPackageComponentModelConf) componentModelConf).addComponentConf(componentname.getText(),
								getComponentLayer(), units);
						groupComponentModelConf.save();
						deleteSelectedPackages();
					}
				};
				d.setModal(true);
				d.setVisible(true);
			}
		});
		addPopupMenu.add(createItem);

		final JPopupMenu deletePopupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("从组件模型中清除这些包");
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(listTable, "您是否确认？") == JOptionPane.OK_OPTION) {
					try {
						deletePackagesFromConf();
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
						selectedPackages = collect();
						if (selectedPackages.size() > 0) {
							if (selectedPackages.containsKey("新增")) {
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

		Map<String, String> diffPackages = JDependUnitMgr.getInstance().getResult().getDiffPackages();

		for (String packageName : diffPackages.keySet()) {
			row = new Object[2];
			row[0] = packageName;
			row[1] = diffPackages.get(packageName).equals("ADD") ? "新增" : "已删除";
			listModel.addRow(row);
		}

		sorter.setSortingStatus(0, TableSorter.DESCENDING);

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(ReportConstant.Name);
		JTableUtil.fitTableColumns(listTable, fitColNames);

		this.add(BorderLayout.CENTER, new JScrollPane(listTable));

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.createCloseButton());
		this.add(BorderLayout.SOUTH, buttonPanel);
	}

	private Map<String, ArrayList<String>> collect() throws JDependException {

		Map<String, ArrayList<String>> selectedPackages = new HashMap<String, ArrayList<String>>();

		int[] rows = listTable.getSelectedRows();
		if (rows == null || rows.length == 0)
			return selectedPackages;

		String operation = null;
		String javaPackageName = null;

		for (int i = 0; i < rows.length; i++) {
			javaPackageName = (String) listTable.getValueAt(rows[i], 0);
			operation = (String) listTable.getValueAt(rows[i], 1);
			if (!selectedPackages.containsKey(operation)) {
				selectedPackages.put(operation, new ArrayList<String>());
			}
			selectedPackages.get(operation).add(javaPackageName);
		}
		if (selectedPackages.size() > 1) {
			throw new JDependException("不能够包含新增和已删除两类包！");
		}
		return selectedPackages;

	}

	private void deletePackagesFromConf() throws JDependException {
		GroupComponentModelConf groupComponentModelConf = ComponentModelConfMgr.getInstance()
				.getTheGroupComponentModelConf(group);
		ComponentModelConf<ComponentConf> componentModelConf = groupComponentModelConf.getComponentModelConfs().get(
				componentModelConfName);

		ArrayList<String> deletePackages = this.selectedPackages.get("已删除");
		for (ComponentConf componentConf : componentModelConf.getComponentConfs()) {
			Iterator<String> it = componentConf.getItemNames().iterator();
			while (it.hasNext()) {
				if (deletePackages.contains(it.next())) {
					it.remove();
				}
			}
		}

		groupComponentModelConf.save();
		deleteSelectedPackages();
	}

	private void deleteSelectedPackages() {
		List<String> packages;
		if (this.selectedPackages.containsKey("新增")) {
			packages = this.selectedPackages.get("新增");
		} else {
			packages = this.selectedPackages.get("已删除");
		}
		for (int row = listModel.getRowCount() - 1; row >= 0; row--) {
			if (packages.contains(listModel.getValueAt(row, 0))) {
				listModel.removeRow(row);
			}
		}
		this.selectedPackages = new HashMap<String, ArrayList<String>>();
	}

	private Component createCloseButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangedPackageListDialog.this.dispose();
			}
		});

		return button;
	}
}
