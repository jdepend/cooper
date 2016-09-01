package jdepend.server.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.Naming;
import java.util.Collections;
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
import jdepend.framework.util.MetricsFormat;
import jdepend.server.service.RemoteServiceFactory;
import jdepend.server.service.score.ScoreListRepository;
import jdepend.server.service.score.ScoreDTO;
import jdepend.server.service.score.ScoreRemoteService;

public final class ScoreMgrPanel extends JPanel {

	private JDependServer server;

	private DefaultTableModel model;

	private String current;

	private ScoreRemoteService scoreRemoteService;

	public ScoreMgrPanel(final JDependServer server) {

		this.server = server;

		this.setLayout(new BorderLayout());

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(ScoreMgrPanel.this, "您是否确认删除？", "提示", JOptionPane.YES_NO_OPTION) == 0) {
					try {
						delete();
						refresh();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(ScoreMgrPanel.this, ex.getMessage(), "alert",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		popupMenu.add(deleteItem);

		JTable table = new JTable(model);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				int currentRow = table.rowAtPoint(e.getPoint());
				if (currentRow >= 0) {
					table.setRowSelectionInterval(currentRow, currentRow);
					current = (String) table.getValueAt(currentRow, 0);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					JTable table = (JTable) e.getSource();
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});

		model.addColumn("ID");
		model.addColumn("IP");
		model.addColumn("用户");
		model.addColumn("命令组名称");
		model.addColumn("命令名称");
		model.addColumn("抽象程度合理性");
		model.addColumn("内聚性指数");
		model.addColumn("封装性");
		model.addColumn("关系合理性");
		model.addColumn("总分");
		model.addColumn("创建时间");
		model.addColumn("上传时间");

		table.getColumn("ID").setMaxWidth(0);
		table.getColumn("ID").setMinWidth(0);

		final JScrollPane pane = new JScrollPane(table);

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
					JOptionPane.showMessageDialog(ScoreMgrPanel.this.server, e1.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonBar.add(refreshButton);

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	private void delete() throws JDependException {
		ScoreListRepository.delete(this.current);
	}

	private void refresh() throws JDependException {

		model.setRowCount(0);

		Object[] row;
		List<ScoreDTO> scoreList = ScoreListRepository.getList();
		Collections.sort(scoreList);
		for (ScoreDTO scoreDTO : scoreList) {
			row = new Object[11];

			row[0] = scoreDTO.id;
			row[1] = scoreDTO.ip;
			row[2] = scoreDTO.user;
			row[3] = scoreDTO.group;
			row[4] = scoreDTO.command;
			row[5] = MetricsFormat.toFormattedMetrics(scoreDTO.d);
			row[6] = MetricsFormat.toFormattedMetrics(scoreDTO.balance);
			row[7] = MetricsFormat.toFormattedMetrics(scoreDTO.encapsulation);
			row[8] = MetricsFormat.toFormattedMetrics(scoreDTO.relation);
			row[9] = MetricsFormat.toFormattedMetrics(scoreDTO.score);
			row[10] = scoreDTO.getCreateDate();
			row[11] = scoreDTO.getUploadDate();

			model.addRow(row);
		}
	}

	public void bindService() throws JDependException {
		try {
			this.scoreRemoteService = RemoteServiceFactory.createScoreRemoteService();
			Naming.rebind("rmi://localhost:1099/ScoreRemoteService", scoreRemoteService);
		} catch (Exception e) {
			throw new JDependException("绑定分数服务错误！", e);
		}
	}

	public void unbindService() throws JDependException {
		try {
			Naming.unbind("rmi://localhost:1099/ScoreRemoteService");
		} catch (Exception e) {
			throw new JDependException("解除积分服务绑定错误！", e);
		}

	}
}
