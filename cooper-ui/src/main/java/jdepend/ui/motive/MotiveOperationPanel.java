package jdepend.ui.motive;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.knowledge.motive.MotiveContainerMgr;
import jdepend.knowledge.motive.Problem;
import jdepend.knowledge.motive.Reason;
import jdepend.model.AreaComponent;
import jdepend.model.JDependUnitMgr;
import jdepend.ui.JDependCooper;

public class MotiveOperationPanel extends MotivePanel {

	private JTable problemTable;

	private DefaultTableModel problems;

	private JTable middleTable;

	private DefaultTableModel middleList;

	private JTable stableTable;

	private DefaultTableModel stableList;

	private JTable mutabilityTable;

	private DefaultTableModel mutabilityList;

	private DefaultTableModel areaNames;

	private DefaultListModel areaComponents;

	private JTable areaNameList;

	private String currentAreaName;

	private String currentComponentName;

	public MotiveOperationPanel(JDependCooper frame, MotiveDialog motiveDialog) {

		super(frame, motiveDialog);

		this.refreshProblem();
		this.refreshStable();
		this.refreshArea();
	}

	@Override
	protected JPanel createButtonBar() {
		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createExecuteButton());
		buttonBar.add(createSaveButton());
		buttonBar.add(createReBuildButton());
		buttonBar.add(createCloseButton());

		return buttonBar;
	}

	@Override
	protected JPanel createWorkspacePanel() {
		JPanel workspacePanel = new JPanel(new GridLayout(3, 1));

		workspacePanel.add(new JScrollPane(this.createProblemList()));

		JPanel stablePanel = new JPanel(new GridLayout(1, 2));
		stablePanel.add(new JScrollPane(createMiddleList()));
		JPanel decisionPanel = new JPanel(new GridLayout(2, 1));
		decisionPanel.add(this.createMutabilityList());
		decisionPanel.add(this.createStableList());
		stablePanel.add(decisionPanel);
		workspacePanel.add(stablePanel);

		JPanel areaPanel = new JPanel(new GridLayout(1, 2));

		areaPanel.add(this.createAreaNameList());
		areaPanel.add(new JScrollPane(this.createAreaComponentList()));

		workspacePanel.add(areaPanel);

		return workspacePanel;
	}

	private JTable createMiddleList() {

		middleList = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		middleList.addColumn("组件名称");
		middleList.addColumn("稳定性");
		TableSorter sorter = new TableSorter(middleList);
		middleTable = new JTable(sorter);

		sorter.setTableHeader(middleTable.getTableHeader());
		sorter.setSortingStatus(1, TableSorter.DESCENDING);

		return middleTable;
	}

	private JComponent createMutabilityList() {
		JPanel contentPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
		JButton toRight = new JButton(">>");
		toRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Object[] rowData = toRight(mutabilityList);
					String componentName = (String) rowData[0];
					Float balance = (Float) rowData[1];
					motiveContainer.addMutability(componentName, balance);
				} catch (JDependException e1) {
					JOptionPane.showMessageDialog(MotiveOperationPanel.this, e1.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JButton toLeft = new JButton("<<");
		toLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Object[] rowData = toLeft(mutabilityTable);
					String componentName = (String) rowData[0];
					motiveContainer.deleteMutability(componentName);
				} catch (JDependException e1) {
					JOptionPane.showMessageDialog(MotiveOperationPanel.this, e1.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonPanel.add(toRight);
		buttonPanel.add(toLeft);
		contentPanel.add(BorderLayout.WEST, buttonPanel);
		mutabilityList = new DefaultTableModel();
		mutabilityList.addColumn("组件名称");
		mutabilityList.addColumn("稳定性");
		TableSorter sorter = new TableSorter(mutabilityList);
		mutabilityTable = new JTable(sorter);
		sorter.setTableHeader(mutabilityTable.getTableHeader());
		sorter.setSortingStatus(1, TableSorter.DESCENDING);

		contentPanel.add(BorderLayout.CENTER, new JScrollPane(mutabilityTable));

		return contentPanel;
	}

	private JComponent createStableList() {
		JPanel contentPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
		JButton toRight = new JButton(">>");
		toRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Object[] rowData = toRight(stableList);
					String componentName = (String) rowData[0];
					Float balance = (Float) rowData[1];
					motiveContainer.addStable(componentName, balance);
				} catch (JDependException e1) {
					JOptionPane.showMessageDialog(MotiveOperationPanel.this, e1.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JButton toLeft = new JButton("<<");
		toLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Object[] rowData = toLeft(stableTable);
					String componentName = (String) rowData[0];
					motiveContainer.deleteStable(componentName);
				} catch (JDependException e1) {
					JOptionPane.showMessageDialog(MotiveOperationPanel.this, e1.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonPanel.add(toRight);
		buttonPanel.add(toLeft);
		contentPanel.add(BorderLayout.WEST, buttonPanel);
		stableList = new DefaultTableModel();
		stableList.addColumn("组件名称");
		stableList.addColumn("稳定性");
		TableSorter sorter = new TableSorter(stableList);
		stableTable = new JTable(sorter);
		sorter.setTableHeader(stableTable.getTableHeader());
		sorter.setSortingStatus(1, TableSorter.DESCENDING);

		contentPanel.add(BorderLayout.CENTER, new JScrollPane(stableTable));

		return contentPanel;
	}

	private Object[] toRight(DefaultTableModel tableModel) throws JDependException {
		int row = this.middleTable.getSelectedRow();
		if (row == -1) {
			throw new JDependException("请选择需要移动的组件");
		}
		String componentName = (String) this.middleTable.getValueAt(row, 0);
		Object[] rowData = new Object[2];
		rowData[0] = componentName;
		rowData[1] = (Float) this.middleTable.getValueAt(row, 1);
		tableModel.addRow(rowData);

		this.middleList.removeRow(((TableSorter) this.middleTable.getModel()).modelIndex(row));
		motiveContainer.deleteMiddle(componentName);

		return rowData;
	}

	private Object[] toLeft(JTable table) throws JDependException {
		int row = table.getSelectedRow();
		if (row == -1) {
			throw new JDependException("请选择需要移动的组件");
		}
		String componentName = (String) table.getValueAt(row, 0);
		Float balance = (Float) table.getValueAt(row, 1);
		Object[] rowData = new Object[2];
		rowData[0] = (String) componentName;
		rowData[1] = (Float) balance;
		middleList.addRow(rowData);
		motiveContainer.addMiddle(componentName, balance);

		TableSorter sorter = (TableSorter) table.getModel();
		((DefaultTableModel) sorter.getTableModel()).removeRow(sorter.modelIndex(row));

		return rowData;
	}

	private JComponent createAreaNameList() {

		areaNames = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		areaNames.addColumn("区域名称");
		areaNames.addColumn("稳定性");

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem addItem = new JMenuItem("添加");
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addArea();
			}
		});
		popupMenu.add(addItem);
		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentAreaName != null) {
					deleteArea(currentAreaName);
				}
			}
		});
		popupMenu.add(deleteItem);

		areaNameList = new JTable(areaNames);
		areaNameList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				JTable table = (JTable) e.getSource();
				int currentRow = table.rowAtPoint(e.getPoint());
				if (currentRow >= 0) {
					currentAreaName = (String) areaNames.getValueAt(currentRow, 0);
					refreshAreaComponent();
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				JTable table = (JTable) e.getSource();
				if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});

		final JPopupMenu popupMenu1 = new JPopupMenu();
		JMenuItem addItem1 = new JMenuItem("添加");
		addItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addArea();
			}
		});
		popupMenu1.add(addItem1);
		JScrollPane jp = new JScrollPane(areaNameList);
		jp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					popupMenu1.show((JScrollPane) e.getSource(), e.getX(), e.getY());
				}
			}
		});

		return jp;
	}

	private JList createAreaComponentList() {

		areaComponents = new DefaultListModel();

		final JList list = new JList(areaComponents);
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem addItem = new JMenuItem("添加");
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addComponent(currentAreaName);
			}
		});
		popupMenu.add(addItem);
		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentAreaName != null && currentComponentName != null) {
					try {
						deleteComponent(currentAreaName, currentComponentName);
					} catch (JDependException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(MotiveOperationPanel.this, "删除失败", "error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		popupMenu.add(deleteItem);

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JList list = (JList) e.getSource();
				int index = list.getSelectedIndex();
				if (index >= 0) {
					currentComponentName = (String) areaComponents.getElementAt(index);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				JList list = (JList) e.getSource();
				if (e.getButton() == 3) {
					popupMenu.show(list, e.getX(), e.getY());
				}
			}
		});

		return list;
	}

	private JTable createProblemList() {

		problems = new DefaultTableModel();
		problemTable = new JTable(problems);
		problems.addColumn("名称");
		problems.addColumn("类型");
		problems.addColumn("重要程度");
		problems.addColumn("原因");
		problems.addColumn("原因描述");

		JComboBox types = new JComboBox();
		types.addItem("");
		for (String type : MotiveContainerMgr.getProblemTypes()) {
			types.addItem(type);
		}
		problemTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(types));

		final JComboBox isImportant = new JComboBox();
		isImportant.addItem("");
		isImportant.addItem("重要");
		isImportant.addItem("不重要");
		problemTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(isImportant));
		isImportant.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int selectedRow = problemTable.getSelectedRow();
					String problemName = (String) problemTable.getValueAt(selectedRow, 0);
					String isImportantValue = (String) isImportant.getSelectedItem();
					Boolean isImportant = isImportantValue == null || isImportantValue.length() == 0 ? null
							: isImportantValue.equals("重要") ? true : false;
					motiveContainer.updateProblemImportant(problemName, isImportant);
				}
			}
		});

		final JComboBox reasons = new JComboBox();
		reasons.setRenderer(new CustomComboBoxRenderer());
		reasons.addItem("");
		for (String reason : MotiveContainerMgr.getReasons()) {
			reasons.addItem(reason);
		}
		problemTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(reasons));
		reasons.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int selectedRow = problemTable.getSelectedRow();
					String problemName = (String) problemTable.getValueAt(selectedRow, 0);
					Reason reason = new Reason((String) reasons.getSelectedItem(), (String) problemTable.getValueAt(
							selectedRow, 4));
					try {
						motiveContainer.updateProblemReason(problemName, reason);
					} catch (JDependException e1) {
						JOptionPane.showMessageDialog(MotiveOperationPanel.this, e1.getMessage(), "error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				this.setToolTipText((String) value);
				return component;
			}
		};
		problemTable.getColumnModel().getColumn(3).setCellRenderer(renderer);

		return problemTable;
	}

	private void refreshProblem() {
		problems.setRowCount(0);
		Object[] row;
		for (Problem problem : motiveContainer.getProblems()) {
			row = new Object[5];
			row[0] = problem.getName();
			row[1] = problem.getType();
			row[2] = problem.isImportant() == null ? "" : problem.isImportant() ? "重要" : "不重要";
			row[3] = problem.getReason() == null ? "" : problem.getReason().getName();
			row[4] = problem.getReason() == null ? "" : problem.getReason().getDesc();

			problems.addRow(row);
		}

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add("名称");
		JTableUtil.fitTableColumns(problemTable, fitColNames);
	}

	private void refreshStable() {

		middleList.setRowCount(0);
		Object[] row;
		Map<String, Float> listData = this.motiveContainer.getMiddles();
		for (String name : listData.keySet()) {
			row = new Object[2];
			row[0] = name;
			row[1] = MetricsFormat.toFormattedMetrics(listData.get(name));
			middleList.addRow(row);
		}

		mutabilityList.setRowCount(0);
		Map<String, Float> listData_m = this.motiveContainer.getMutabilitys();
		for (String name : listData_m.keySet()) {
			row = new Object[2];
			row[0] = name;
			row[1] = MetricsFormat.toFormattedMetrics(listData_m.get(name));
			mutabilityList.addRow(row);
		}

		stableList.setRowCount(0);
		Map<String, Float> listData_s = this.motiveContainer.getStables();
		for (String name : listData_s.keySet()) {
			row = new Object[2];
			row[0] = name;
			row[1] = MetricsFormat.toFormattedMetrics(listData_s.get(name));
			stableList.addRow(row);
		}

	}

	protected void refreshArea() {
		areaNames.setRowCount(0);

		Object[] row;
		for (AreaComponent areaComponent : motiveContainer.getAreas()) {
			row = new Object[2];
			row[0] = areaComponent.getName();
			row[1] = MetricsFormat.toFormattedMetrics(areaComponent.instability());
			areaNames.addRow(row);
		}
		if (areaNames.getRowCount() > 0) {
			this.currentAreaName = (String) areaNames.getValueAt(0, 0);
			this.areaNameList.setRowSelectionInterval(0, 0);
			refreshAreaComponent();
		} else {
			this.areaComponents.setSize(0);
		}
	}

	private void refreshAreaComponent() {
		this.areaComponents.setSize(0);

		for (AreaComponent areaComponent : motiveContainer.getAreas()) {
			if (areaComponent.getName().equals(this.currentAreaName)) {
				for (String componentName : areaComponent.getComponents()) {
					areaComponents.addElement(componentName);
				}
			}
		}

	}

	private Component createExecuteButton() {
		JButton button = new JButton("执行");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					save();
					execute();
					JDependUnitMgr.getInstance().getResult().clearScore();
					JDependUnitMgr.getInstance().getResult().clearRelationCache();
					frame.getResultPanelWrapper().showResults();
					motiveDialog.dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane
							.showMessageDialog(MotiveOperationPanel.this, "执行失败", "error", JOptionPane.ERROR_MESSAGE);
				} catch (JDependException e1) {
					JOptionPane.showMessageDialog(MotiveOperationPanel.this, e1.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}

	private Component createSaveButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Save));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					save();
					JOptionPane.showMessageDialog(MotiveOperationPanel.this, "保存成功", "info",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane
							.showMessageDialog(MotiveOperationPanel.this, "保存失败", "error", JOptionPane.ERROR_MESSAGE);
				} catch (JDependException e1) {
					JOptionPane.showMessageDialog(MotiveOperationPanel.this, e1.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}

	private Component createReBuildButton() {
		JButton button = new JButton("重新计算");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rebuild();
					JOptionPane.showMessageDialog(MotiveOperationPanel.this, "计算成功", "info",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane
							.showMessageDialog(MotiveOperationPanel.this, "计算失败", "error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}

	private void deleteArea(String areaName) {
		motiveContainer.deleteAreaComponent(areaName);
		this.refreshArea();
	}

	private void addArea() {
		CreateAreaDialog d = new CreateAreaDialog(this);
		d.setModal(true);
		d.setVisible(true);
	}

	private void deleteComponent(String areaName, String componentName) throws JDependException {
		motiveContainer.deleteComponent(areaName, componentName);
		this.refreshArea();
	}

	private void addComponent(String areaName) {
		AddComponentDialog d = new AddComponentDialog(this, areaName);
		d.setModal(true);
		d.setVisible(true);
	}

	private void execute() throws IOException, JDependException {
		motiveContainer.execute();
		// 记录日志
		BusiLogUtil.getInstance().businessLog(Operation.executeMotive);
	}

	private void save() throws IOException, JDependException {
		motiveContainer.check();
		motiveContainer.save();
	}

	private void rebuild() throws IOException {

		this.motiveContainer.reBuild(JDependUnitMgr.getInstance().getResult());

		this.refreshProblem();
		this.refreshStable();
		this.refreshArea();
		this.motiveDialog.getMotiveHistoryPanel().refresh();
	}

	class CustomComboBoxRenderer extends BasicComboBoxRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
				if (-1 < index) {
					list.setToolTipText((value == null) ? null : value.toString());
				}
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setFont(list.getFont());
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

}
