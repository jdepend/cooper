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

public class MethodListOperationPanel extends JPanel {

	private MethodListPanel methodListPanel;

	private JLabel tipLabel;

	private JTextField classNameFilter;

	private JTextField nameFilter;

	private JTextField callerFilter;

	public MethodListOperationPanel(MethodListPanel methodListPanel1) {
		this.setLayout(new BorderLayout());

		this.add(BorderLayout.NORTH, this.createSearchPanel());

		this.methodListPanel = methodListPanel1;
		this.add(this.methodListPanel);

		new Thread() {
			@Override
			public void run() {
				int methodCount = methodListPanel.loadMethodList();
				tipLabel.setText("共" + methodCount + "个方法");
			}
		}.start();
	}

	private JPanel createSearchPanel() {

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel titleLabel = new JLabel(BundleUtil.getString(BundleUtil.TableHead_ClassName) + ":");
		searchPanel.add(titleLabel);

		classNameFilter = new JTextField();
		classNameFilter.setPreferredSize(new Dimension(250, 20));
		classNameFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int methodCount = methodListPanel.filterMehtodList(classNameFilter.getText(), nameFilter.getText(),
						callerFilter.getText());
				tipLabel.setText("共" + methodCount + "个方法");
			}
		});
		searchPanel.add(classNameFilter);

		tipLabel = new JLabel();
		searchPanel.add(tipLabel);

		JLabel methodNameLabel = new JLabel(BundleUtil.getString(BundleUtil.ClientWin_MethodListDialog_Name) + ":");
		searchPanel.add(methodNameLabel);

		nameFilter = new JTextField();
		nameFilter.setPreferredSize(new Dimension(250, 20));
		nameFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int methodCount = methodListPanel.filterMehtodList(classNameFilter.getText(), nameFilter.getText(),
						callerFilter.getText());
				tipLabel.setText("共" + methodCount + "个方法");
			}
		});
		searchPanel.add(nameFilter);

		JLabel callerNameLabel = new JLabel(BundleUtil.getString(BundleUtil.ClientWin_ClassListDialog_Caller) + ":");
		searchPanel.add(callerNameLabel);

		callerFilter = new JTextField();
		callerFilter.setPreferredSize(new Dimension(250, 20));
		callerFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int methodCount = methodListPanel.filterMehtodList(classNameFilter.getText(), nameFilter.getText(),
						callerFilter.getText());
				tipLabel.setText("共" + methodCount + "个方法");
			}
		});
		searchPanel.add(callerFilter);

		return searchPanel;
	}

}
