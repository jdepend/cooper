package jdepend.ui.property;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.core.config.CommandConfMgr;
import jdepend.core.config.GroupConfChangeListener;
import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.knowledge.database.AnalysisResultRepository;
import jdepend.knowledge.database.ExecuteResultSummry;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisResultListener;
import jdepend.report.util.ReportConstant;
import jdepend.ui.JDependCooper;
import jdepend.ui.util.AnalysisResultExportUtil;
import jdepend.util.refactor.AdjustHistory;

public class ExecuteHistoryPanel extends JPanel implements GroupConfChangeListener, AnalysisResultListener {

	private JDependCooper frame;

	private DefaultTableModel histroyModel;

	private JTable histroyTable;

	private String currentId;

	private int currentRow;

	private String group;

	private String command;

	private List<String> selectedIDs;

	public ExecuteHistoryPanel(JDependCooper frame) {
		super();
		setLayout(new BorderLayout());

		this.frame = frame;

		this.add(this.initHistory());

		// 向命令组配置组件增加监听器
		try {
			CommandConfMgr.getInstance().addGroupListener(this);
		} catch (JDependException e) {
			e.printStackTrace();
			frame.showStatusError(e.getMessage());
		}

		// 向分析结果管理器注册监听器
		JDependUnitMgr.getInstance().addAnalysisResultListener(this);
	}

	public void showHistory(String group, String command) throws JDependException {
		histroyModel.setRowCount(0);
		loadHistory(group, command);
	}

	public void clearHistory() {
		histroyModel.setRowCount(0);
	}

	private JComponent initHistory() {

		histroyModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(histroyModel);

		histroyTable = new JTable(sorter);

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem viewItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_View));
		viewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view();
			}
		});
		popupMenu.add(viewItem);

		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedIDs.size() > 0) {
					if (JOptionPane.showConfirmDialog(frame, "您是否确认删除？", "提示", JOptionPane.YES_NO_OPTION) == 0) {
						try {
							for (String id : selectedIDs) {
								AnalysisResultRepository.delete(id);
							}
							showHistory(group, command);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(frame, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "请选择要删除的记录", "alert", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		popupMenu.add(deleteItem);

		JMenuItem compareItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Compare));
		compareItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedIDs == null || selectedIDs.size() != 2) {
					JOptionPane.showMessageDialog(frame, "请选择2条需要比较的记录", "alert", JOptionPane.INFORMATION_MESSAGE);
				} else {
					compare(selectedIDs.get(0), selectedIDs.get(1));
				}
			}
		});
		popupMenu.add(compareItem);

		popupMenu.addSeparator();

		JMenuItem refreshItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Refresh));
		refreshItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					showHistory(group, command);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(refreshItem);
		JMenuItem displayLineItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ShowLineChart));
		displayLineItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					displayLine();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(displayLineItem);
		popupMenu.addSeparator();

		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(histroyTable);
			}
		});
		popupMenu.add(saveAsItem);

		JMenuItem exportItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Export));
		exportItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		popupMenu.add(exportItem);

		histroyTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				selectedIDs = new ArrayList<String>();
				for (int row : table.getSelectedRows()) {
					selectedIDs.add((String) table.getValueAt(row, 0));
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				currentRow = table.rowAtPoint(e.getPoint());
				currentId = (String) table.getValueAt(currentRow, 0);
				if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
				if (e.getClickCount() == 2)
					view();
			}
		});

		sorter.setTableHeader(histroyTable.getTableHeader());

		histroyModel.addColumn("ID");
		histroyModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_ExecuteDate));
		histroyModel.addColumn(ReportConstant.LC);
		histroyModel.addColumn(ReportConstant.CN);
		histroyModel.addColumn(ReportConstant.CC);
		histroyModel.addColumn(ReportConstant.AC);
		histroyModel.addColumn(ReportConstant.Ca);
		histroyModel.addColumn(ReportConstant.Ce);
		histroyModel.addColumn(ReportConstant.A);
		histroyModel.addColumn(ReportConstant.I);
		histroyModel.addColumn(ReportConstant.D);
		histroyModel.addColumn(ReportConstant.Coupling);
		histroyModel.addColumn(ReportConstant.Cohesion);
		histroyModel.addColumn(ReportConstant.Balance);
		histroyModel.addColumn(ReportConstant.Encapsulation);

		histroyTable.getColumn("ID").setMaxWidth(0);
		histroyTable.getColumn("ID").setMinWidth(0);

		sorter.setSortingStatus(1, TableSorter.ASCENDING);

		JScrollPane pane = new JScrollPane(histroyTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		final JPopupMenu popupMenu1 = new JPopupMenu();
		JMenuItem refreshItem1 = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Refresh));
		refreshItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					showHistory(group, command);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu1.add(refreshItem1);
		JMenuItem displayLineItem1 = new JMenuItem("显示折线图");
		displayLineItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					displayLine();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu1.add(displayLineItem1);

		pane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				if (e.getButton() == 3 && histroyModel.getRowCount() > 0) {
					popupMenu1.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}
		});

		return pane;
	}

	private void loadHistory(String group, String command) throws JDependException {

		Object[] row;

		this.group = group;
		this.command = command;

		List<ExecuteResultSummry> summrys = AnalysisResultRepository.getResultSummrys(group, command);

		for (ExecuteResultSummry summry : summrys) {
			row = new Object[15];
			row[0] = summry.getId();
			row[1] = summry.getCreateDate();
			row[2] = summry.getSummry().getLineCount();
			row[3] = summry.getSummry().getClassCount();
			row[4] = summry.getSummry().getConcreteClassCount();
			row[5] = summry.getSummry().getAbstractClassCount();
			row[6] = summry.getSummry().getAfferentCoupling();
			row[7] = summry.getSummry().getEfferentCoupling();
			row[8] = MetricsFormat.toFormattedMetrics(summry.getSummry().getAbstractness());
			row[9] = MetricsFormat.toFormattedMetrics(summry.getSummry().getInstability());
			row[10] = MetricsFormat.toFormattedMetrics(summry.getSummry().getDistance());
			row[11] = MetricsFormat.toFormattedMetrics(summry.getSummry().getCoupling());
			row[12] = MetricsFormat.toFormattedMetrics(summry.getSummry().getCohesion());
			row[13] = MetricsFormat.toFormattedMetrics(summry.getSummry().getBalance());
			row[14] = MetricsFormat.toFormattedMetrics(summry.getSummry().getEncapsulation());

			histroyModel.addRow(row);
		}
	}

	private void view() {
		try {
			AnalysisResult result = AnalysisResultRepository.getResult(currentId);
			JDependUnitMgr.getInstance().setResult(result);
			frame.getResultPanelWrapper().showResults(true);
		} catch (JDependException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "读取执行历史失败！", "alert", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void compare(String id1, String id2) {
		try {
			AdjustHistory.getInstance().clear();
			AnalysisResult result1 = AnalysisResultRepository.getResult(id1);
			JDependUnitMgr.getInstance().setResult(result1);
			AdjustHistory.getInstance().addMemento();
			AnalysisResult result2 = AnalysisResultRepository.getResult(id2);
			JDependUnitMgr.getInstance().setResult(result2);
			frame.getResultPanelWrapper().showResults(false);
		} catch (JDependException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "读取执行历史失败！", "alert", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void displayLine() {
		try {
			List<ExecuteResultSummry> summrys = AnalysisResultRepository.getResultSummrys(group, command);
			frame.getResultPanel().addResult(group + "." + command + "执行历史折线图",
					new ExecuteHistoryChartPanel(frame, summrys));
		} catch (Throwable e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "读取执行历史失败！", "alert", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void export() {
		try {
			AnalysisResult result = AnalysisResultRepository.getResult(currentId);
			AnalysisResultExportUtil.exportResult(frame, result);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "导出失败！", "alert", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void onCreate(String group) throws JDependException {
	}

	@Override
	public void onDelete(String group) throws JDependException {
		// 删除执行历史
		AnalysisResultRepository.deleteAll(group);
		// 清空显示数据
		if (this.group != null && this.group.equals(group)) {
			this.clearHistory();
		}

	}

	@Override
	public void onUpdate(String group) throws JDependException {
	}

	@Override
	public void onRefresh() throws JDependException {
	}

	@Override
	public void onExecuted(AnalysisResult result) throws JDependException {
		// 保存执行结果
		if (result.getRunningContext().getGroup() != null && result.getRunningContext().getCommand() != null
				&& (new PropertyConfigurator()).isSaveResult()) {
			AnalysisResultRepository.save(result);
		}
	}
}
