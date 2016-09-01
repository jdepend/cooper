package jdepend.client.ui.property;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.util.BundleUtil;

public final class LogPanel extends JPanel {

	private JTabbedPane tabPane;

	private BusiLogPanel busiLogPanel;

	private SystemLogPanel systemLogPanel;

	public LogPanel(JDependFrame frame) {

		this.setLayout(new BorderLayout());

		tabPane = new JTabbedPane();
		tabPane.setTabPlacement(JTabbedPane.BOTTOM);
		
		systemLogPanel = new SystemLogPanel(frame);
		tabPane.addTab(
				BundleUtil.getString(BundleUtil.ClientWin_Property_Log_System),
				systemLogPanel);

		busiLogPanel = new BusiLogPanel(frame);
		tabPane.addTab(BundleUtil
				.getString(BundleUtil.ClientWin_Property_Log_Business),
				busiLogPanel);
		

		this.add(tabPane);
	}

	public void showSystemLog() {
		this.tabPane.setSelectedIndex(0);
	}

	public BusiLogPanel getBusiLogPanel() {
		return busiLogPanel;
	}

	public SystemLogPanel getSystemLogPanel() {
		return systemLogPanel;
	}

}
