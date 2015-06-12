package jdepend.client.report.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jdepend.framework.util.BundleUtil;

public class ClassListOperationPanel extends JPanel {

	private ClassListPanel classListPanel;

	private JLabel tipLabel;

	private JTextField nameFilter;

	private JTextField callerFilter;

	private JTextField calleeFilter;

	public ClassListOperationPanel(ClassListPanel classListPanel) {
		this.setLayout(new BorderLayout());

		this.add(BorderLayout.NORTH, this.createSearchPanel());

		this.classListPanel = classListPanel;
		this.add(this.classListPanel);
		int classCount = this.classListPanel.showAllClassList();
		tipLabel.setText("共" + classCount + "个类");

	}

	private JPanel createSearchPanel() {

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel titleLabel = new JLabel(BundleUtil.getString(BundleUtil.Metrics_Name) + ":");
		searchPanel.add(titleLabel);

		nameFilter = new JTextField();
		nameFilter.setPreferredSize(new Dimension(250, 20));
		nameFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int classCount = classListPanel.filterClassList(nameFilter.getText(), callerFilter.getText(),
						calleeFilter.getText());
				tipLabel.setText("共" + classCount + "个类");
			}
		});
		searchPanel.add(nameFilter);

		tipLabel = new JLabel();
		searchPanel.add(tipLabel);

		JLabel callerLabel = new JLabel(BundleUtil.getString(BundleUtil.ClientWin_ClassListDialog_Caller) + ":");
		searchPanel.add(callerLabel);

		callerFilter = new JTextField();
		callerFilter.setPreferredSize(new Dimension(250, 20));
		callerFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int classCount = classListPanel.filterClassList(nameFilter.getText(), callerFilter.getText(),
						calleeFilter.getText());
				tipLabel.setText("共" + classCount + "个类");
			}
		});
		searchPanel.add(callerFilter);

		JLabel calleeLabel = new JLabel(BundleUtil.getString(BundleUtil.ClientWin_ClassListDialog_Callee) + ":");
		searchPanel.add(calleeLabel);

		calleeFilter = new JTextField();
		calleeFilter.setPreferredSize(new Dimension(250, 20));
		calleeFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int classCount = classListPanel.filterClassList(nameFilter.getText(), callerFilter.getText(),
						calleeFilter.getText());
				tipLabel.setText("共" + classCount + "个类");
			}
		});
		searchPanel.add(calleeFilter);

		return searchPanel;
	}

}
