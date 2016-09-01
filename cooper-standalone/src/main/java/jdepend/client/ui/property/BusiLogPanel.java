package jdepend.client.ui.property;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogItem;
import jdepend.framework.log.BusiLogListener;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.DBBusinessLogWriter;
import jdepend.framework.log.Operation;
import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.ui.util.JTableUtil;
import jdepend.framework.util.BundleUtil;

public final class BusiLogPanel extends JPanel implements BusiLogListener {

	private DefaultTableModel model;

	private JDependFrame frame;

	private int count;

	private boolean limit = true;

	public static final int interval = 10;// 显示间隔

	public BusiLogPanel(final JDependFrame frame) {

		this.frame = frame;

		this.setLayout(new BorderLayout());

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(model);

		final JTable table = new JTable(sorter);

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem clearItem = new JMenuItem("清除");
		clearItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (JOptionPane.showConfirmDialog(BusiLogPanel.this.frame, "您是否确认删除？", "提示",
							JOptionPane.YES_NO_OPTION) == 0) {
						clear();
						refresh();
					}
				} catch (JDependException e1) {
					e1.printStackTrace();
					BusiLogPanel.this.frame.showStatusError(e1.getMessage());
				}
			}
		});
		popupMenu.add(clearItem);

		JMenuItem refreshItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Refresh));
		refreshItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, "刷新失败", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(refreshItem);

		popupMenu.addSeparator();

		final JMenuItem allItem = new JMenuItem("显示全部");
		final JMenuItem partItem = new JMenuItem("显示部分");

		allItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limit = false;
				try {
					refresh();
					resetPopupMenu(popupMenu, allItem, partItem);
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, "刷新失败", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		refreshItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limit = true;
				try {
					refresh();
					resetPopupMenu(popupMenu, allItem, partItem);
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, "刷新失败", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		if (limit) {
			popupMenu.add(allItem);
		} else {
			popupMenu.add(partItem);
		}
		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(table);
			}
		});
		popupMenu.add(saveAsItem);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				if (e.getButton() == 3) {
					popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}
		});

		model.addColumn("用户名");
		model.addColumn("操作");
		model.addColumn("时间");

		sorter.setTableHeader(table.getTableHeader());

		try {
			this.refresh();
		} catch (JDependException e) {
			e.printStackTrace();
		}

		final JScrollPane pane = new JScrollPane(table);

		pane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				if (e.getButton() == 3) {
					popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}
		});

		this.add(BorderLayout.CENTER, pane);

		// 设置Listener
		BusiLogUtil.getInstance().addLogListener(this);
		// 定时触发refresh
		(new Timer()).schedule(new LogTask(this), 2000, 5000);
	}

	private void resetPopupMenu(JPopupMenu popupMenu, JMenuItem allItem, JMenuItem partItem) {
		if (limit) {
			popupMenu.remove(partItem);
			popupMenu.add(allItem);
		} else {
			popupMenu.remove(allItem);
			popupMenu.add(partItem);
		}
	}

	private void clear() throws JDependException {
		DBBusinessLogWriter.deleteAll();
	}

	private void refresh() throws JDependException {

		model.setRowCount(0);

		Object[] row;
		for (BusiLogItem item : DBBusinessLogWriter.getAllLogItems(limit)) {
			row = new Object[3];
			row[0] = item.username;
			row[1] = item.operation;
			row[2] = item.getCreatedate();

			model.addRow(row);
		}
		count = 0;
	}

	public boolean isLimit() {
		return limit;
	}

	public void setLimit(boolean limit) {
		this.limit = limit;
	}

	class LogTask extends TimerTask {

		private BusiLogPanel busiLogPanel;

		public LogTask(BusiLogPanel busiLogPanel) {
			this.busiLogPanel = busiLogPanel;
		}

		@Override
		public void run() {
			busiLogPanel.refresh1();
		}
	}

	@Override
	public void onBusiLog(String id, String userName, Operation operation) {
		refresh1();
	}

	private synchronized void refresh1() {
		if (++count >= interval) {
			try {
				this.refresh();
			} catch (JDependException e) {
				e.printStackTrace();
			}
		}
	}
}
