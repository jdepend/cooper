package jdepend.client.ui.remote.analyzer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.client.core.remote.analyzer.AnalyzerRemoteMgr;
import jdepend.client.core.remote.analyzer.AnalyzerSummaryInfo;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.analyzer.AnalyzerPanel;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.util.BundleUtil;

public final class AnalyzerDownloadDialog extends CooperDialog {

	private JDependCooper frame;

	private String currentClassName;

	private DefaultTableModel model;

	private List<AnalyzerSummaryInfo> analyzers;

	private String type;

	private AnalyzerPanel analyzerPanel;
	
	private AnalyzerRemoteMgr analyzerRemoteMgr;

	public AnalyzerDownloadDialog(JDependCooper frame, AnalyzerPanel analyzerPanel, String type) {
		super();

		this.analyzerRemoteMgr = new AnalyzerRemoteMgr();
		
		this.frame = frame;
		this.analyzerPanel = analyzerPanel;
		this.type = type;

		this.setTitle("分析器列表");

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
				currentClassName = (String) table.getModel().getValueAt(currentRow, 2);
				if (currentRow >= 0) {
					table.setRowSelectionInterval(currentRow, currentRow);
				}
			}
		});

		model.addColumn("名称");
		model.addColumn("提示");
		model.addColumn("类名");
		model.addColumn("上传用户名");
		model.addColumn("上传时间");

		try {
			this.refresh();
		} catch (JDependException e) {
			e.printStackTrace();
		}

		this.add(BorderLayout.CENTER, new JScrollPane(table));

		JPanel buttonBar = new JPanel();
		buttonBar.setLayout(new FlowLayout());

		JButton downButton = new JButton("下载");
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					downAnalyzer();
					AnalyzerDownloadDialog.this.analyzerPanel.refresh();
					dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(AnalyzerDownloadDialog.this, ex.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonBar.add(downButton);

		JButton closeButton = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonBar.add(closeButton);

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	private void downAnalyzer() throws JDependException {
		if (this.currentClassName == null) {
			throw new JDependException("请选择下载的分析器。");
		} else {
			this.analyzerRemoteMgr.download(this.currentClassName);
		}

	}

	private void refresh() throws JDependException {

		model.setRowCount(0);

		analyzers = this.analyzerRemoteMgr.getRemoteAnalyzers(type);

		Object[] row;
		for (AnalyzerSummaryInfo analyzer : analyzers) {
			row = new Object[5];
			row[0] = analyzer.getName();
			row[1] = analyzer.getTip();
			row[2] = analyzer.getClassName();
			row[3] = analyzer.getUserName();
			row[4] = analyzer.getCreateDate();

			model.addRow(row);
		}

	}

}
