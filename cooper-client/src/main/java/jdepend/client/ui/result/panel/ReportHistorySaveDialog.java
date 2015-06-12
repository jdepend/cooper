package jdepend.client.ui.result.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.util.BundleUtil;
import jdepend.client.report.history.ReportHistory;
import jdepend.client.ui.result.framework.ReportCreator;

public class ReportHistorySaveDialog extends JDialog {

	private JTextArea tip;

	private String title;

	private ReportCreator reportCreator;

	public ReportHistorySaveDialog(ReportCreator reportCreator, String title) {

		setTitle("Tip");

		setResizable(false);

		this.reportCreator = reportCreator;
		this.title = title;

		getContentPane().setLayout(new BorderLayout());
		setSize(400, 200);

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		tip = new JTextArea();
		tip.setText(title);

		JScrollPane pane = new JScrollPane(tip);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createSaveButton());
		buttonBar.add(createCloseButton());

		panel.add(BorderLayout.CENTER, pane);

		panel.add(BorderLayout.SOUTH, buttonBar);

		getContentPane().add(BorderLayout.CENTER, panel);

	}

	private JButton createCloseButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		return button;
	}

	private JButton createSaveButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Save));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					ReportHistory rh = new ReportHistory(reportCreator.getGroup());
					rh.save(reportCreator.getCommand(), reportCreator.getReportText(title), tip.getText());
					reportCreator.onReportHistorySave();
					// 记录日志
					BusiLogUtil.getInstance().businessLog(Operation.saveTextReport);
					JOptionPane.showMessageDialog((java.awt.Component) e.getSource(), "保存成功", "alert",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
					java.awt.Component source = (java.awt.Component) e.getSource();
					JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}
}
