package jdepend.ui.property;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.util.BundleUtil;

public final class LogPanel extends JPanel {

	public LogPanel(JDependFrame frame) {

		this.setLayout(new BorderLayout());

		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setTabPlacement(JTabbedPane.BOTTOM);

		tabPane.addTab(BundleUtil.getString(BundleUtil.ClientWin_Property_Log_Business), new BusiLogPanel(frame));
		tabPane.addTab(BundleUtil.getString(BundleUtil.ClientWin_Property_Log_System), new SystemLogPanel(frame));

		this.add(tabPane);
	}

}
