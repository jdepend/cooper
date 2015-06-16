package jdepend.client.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import jdepend.client.ui.command.GroupPanel;
import jdepend.client.ui.culture.CulturePanel;
import jdepend.framework.exception.JDependException;

public class LeftPanel extends JPanel {

	private JSplitPane splitPane;

	private GroupPanel groupPanel;

	private CulturePanel culturePanel;

	public LeftPanel(JDependCooper parent) throws JDependException {

		this.setLayout(new BorderLayout());

		groupPanel = new GroupPanel(parent);

		culturePanel = new CulturePanel(parent);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, groupPanel, culturePanel);

		add(splitPane);
	}

	public void setDividerLocation(int location) {
		this.splitPane.setDividerLocation(location);
	}

	public GroupPanel getGroupPanel() {
		return groupPanel;
	}

	public CulturePanel getCulturePanel() {
		return culturePanel;
	}

}
