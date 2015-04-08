package jdepend.server.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.Naming;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.service.local.ServiceFactory;
import jdepend.service.remote.RemoteServiceFactory;
import jdepend.service.remote.analyzer.AnalyzerRepository;
import jdepend.service.remote.analyzer.AnalyzerService;
import jdepend.service.remote.analyzer.AnalyzerSummaryDTO;

public final class AnalyzerMgrPanel extends JPanel {

	private JDependServer server;

	private AnalyzerService analyzerService;

	private List<AnalyzerSummaryDTO> analyzers;

	private DefaultTableModel model;

	private String currentClassName;

	public AnalyzerMgrPanel(JDependServer server) {
		this.server = server;
		this.setLayout(new BorderLayout());

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		JTable table = new JTable(model);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				int currentRow = table.rowAtPoint(e.getPoint());
				currentClassName = (String) table.getModel().getValueAt(currentRow, 3);
				if (currentRow >= 0) {
					table.setRowSelectionInterval(currentRow, currentRow);
				}
			}
		});

		model.addColumn("名称");
		model.addColumn("提示");
		model.addColumn("类型");
		model.addColumn("类名");
		model.addColumn("上传用户名");
		model.addColumn("上传时间");

		final JScrollPane pane = new JScrollPane(table);

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem refreshItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Refresh));
		refreshItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(AnalyzerMgrPanel.this.server, e1.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(refreshItem);
		pane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					popupMenu.show(pane, e.getX(), e.getY());
				}
			}
		});

		this.add(BorderLayout.CENTER, pane);

		JPanel buttonBar = new JPanel();
		buttonBar.setLayout(new FlowLayout());

		JButton refreshButton = new JButton(BundleUtil.getString(BundleUtil.Command_Refresh));
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(AnalyzerMgrPanel.this.server, e1.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonBar.add(refreshButton);

		JButton deleteButton = new JButton(BundleUtil.getString(BundleUtil.Command_Delete));
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (JOptionPane.showConfirmDialog(AnalyzerMgrPanel.this, "您是否确认删除？", "提示",
							JOptionPane.YES_NO_OPTION) == 0) {
						deleteAnalyzer();
						refresh();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(AnalyzerMgrPanel.this, ex.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonBar.add(deleteButton);

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	private void deleteAnalyzer() throws JDependException {
		if (this.currentClassName == null) {
			throw new JDependException("请选择分析器。");
		} else {
			(new AnalyzerRepository()).delete(this.currentClassName);
		}
	}

	private void refresh() throws JDependException {

		model.setRowCount(0);

		analyzers = (new AnalyzerRepository()).queryAll();

		Object[] row;
		for (AnalyzerSummaryDTO analyzer : analyzers) {
			row = new Object[6];
			row[0] = analyzer.getName();
			row[1] = analyzer.getTip();
			row[2] = analyzer.getType();
			row[3] = analyzer.getClassName();
			row[4] = analyzer.getUserName();
			row[5] = analyzer.getCreateDate();

			model.addRow(row);
		}

	}

	public void bindService() throws JDependException {
		try {
			this.analyzerService = RemoteServiceFactory.createAnalyzerService();
			Naming.rebind("rmi://localhost:1099/AnalyzerService", analyzerService);
		} catch (Exception e) {
			throw new JDependException("绑定分析器服务错误！", e);
		}
	}

	public void unbindService() throws JDependException {
		try {
			Naming.unbind("rmi://localhost:1099/AnalyzerService");
		} catch (Exception e) {
			throw new JDependException("解除分析器服务绑定错误！", e);
		}
	}

}
