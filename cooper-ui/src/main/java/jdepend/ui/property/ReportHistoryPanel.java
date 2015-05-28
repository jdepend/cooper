package jdepend.ui.property;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.core.local.config.CommandConfMgr;
import jdepend.core.local.config.GroupConfChangeListener;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.ui.util.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.report.history.ReportHistory;
import jdepend.report.history.ReportHistoryComparor;
import jdepend.report.history.ReportHistoryItemInfo;
import jdepend.ui.JDependCooper;

public class ReportHistoryPanel extends JPanel implements GroupConfChangeListener {

	private JDependCooper frame;

	private DefaultTableModel histroyModel;

	private JTable histroyTable;

	private String currentVersion;

	private String currentGroup;

	private int currentRow;

	public ReportHistoryPanel(JDependCooper frame) {
		super();
		setLayout(new BorderLayout());

		this.frame = frame;

		this.initHistory();

		JScrollPane pane = new JScrollPane(histroyTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);

		// 向命令组配置组件增加监听器
		try {
			CommandConfMgr.getInstance().addGroupListener(this);
		} catch (JDependException e) {
			e.printStackTrace();
			frame.showStatusError(e.getMessage());
		}
	}

	public void showHistory(String group, String command) {

		this.currentGroup = group;
		this.showHistory(command);
	}

	public void clearHistory() {
		histroyModel.setRowCount(0);
	}

	private void showHistory(String command) {

		histroyModel.setRowCount(0);
		loadHistory(command);
	}

	private void initHistory() {

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

		JMenuItem compareItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Compare));
		compareItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				compare();
			}
		});
		popupMenu.add(compareItem);

		popupMenu.addSeparator();

		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(frame, "您是否确认删除？", "提示", JOptionPane.YES_NO_OPTION) == 0) {
					try {
						(new ReportHistory(currentGroup)).delete(currentVersion);
						showHistory(currentVersion.substring(0, currentVersion.indexOf('_')));
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(frame, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		popupMenu.add(deleteItem);
		popupMenu.addSeparator();

		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(histroyTable);
			}
		});
		popupMenu.add(saveAsItem);

		histroyTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				currentRow = table.rowAtPoint(e.getPoint());
				currentVersion = (String) table.getValueAt(currentRow, 0);
				if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
				if (e.getClickCount() == 2)
					view();
			}
		});

		sorter.setTableHeader(histroyTable.getTableHeader());

		histroyModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_Version));
		histroyModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_CreateDate));
		histroyModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_Explain));
	}

	private void loadHistory(String command) {

		Object[] row;

		List<ReportHistoryItemInfo> infos = (new ReportHistory(currentGroup)).load(command);

		for (ReportHistoryItemInfo info : infos) {
			row = new Object[3];
			row[0] = info.key;
			row[1] = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(info.date);
			row[2] = info.tip;

			histroyModel.addRow(row);
		}

	}

	private void view() {
		try {
			frame.getResultPanel().addResult(currentVersion,
					(new ReportHistory(currentGroup)).getContent(currentVersion));
			frame.getResultPanel().setLastedTab();
		} catch (JDependException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "读取报告[" + currentVersion + "]历史失败！", "alert",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private void compare() {
		int[] rows = histroyTable.getSelectedRows();
		if (rows.length != 2) {
			JOptionPane.showMessageDialog(null, "请选择两行报告进行比较!", "alert", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String v1 = (String) histroyTable.getValueAt(rows[0], 0);
		String v2 = (String) histroyTable.getValueAt(rows[1], 0);

		frame.getResultPanel()
				.addResult(v1 + " & " + v2, ReportHistoryComparor.comparePanel(this.currentGroup, v1, v2));
		frame.getResultPanel().setLastedTab();
	}

	@Override
	public void onCreate(String group) throws JDependException {
	}

	@Override
	public void onDelete(String group) throws JDependException {
		new ReportHistory(group).deleteGroup();
		if (this.currentGroup != null && this.currentGroup.equals(group)) {
			this.clearHistory();
		}
	}

	@Override
	public void onUpdate(String group) throws JDependException {
	}

	@Override
	public void onRefresh() throws JDependException {
	}
}
