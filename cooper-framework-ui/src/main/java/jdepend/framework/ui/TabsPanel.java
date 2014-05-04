package jdepend.framework.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class TabsPanel extends JPanel {

	protected JClosableTabbedPane tabPane;

	public TabsPanel() {
		this(new JClosableTabbedPane(true));
	}

	public TabsPanel(JClosableTabbedPane tabPane) {
		this.tabPane = tabPane;

		setLayout(new BorderLayout());
		this.add(BorderLayout.CENTER, this.tabPane);
	}

	public void show(String label, JComponent component) {
		this.setVisible(false);
		int index = this.getTab(label);
		if (index == -1) {
			addTab(label, component);
			this.setLastedTab();
		} else {
			TabWrapper wrapper = (TabWrapper) this.tabPane.getComponentAt(index);
			wrapper.removeAll();
			wrapper.add(component);
			this.tabPane.setSelectedIndex(index);
		}
		this.setVisible(true);
	}

	public void setVisible(boolean aFlag) {
		this.tabPane.setVisible(aFlag);
	}

	protected int getTab(String label) {
		for (int i = 0; i < this.tabPane.getTabCount(); i++) {
			if (this.tabPane.getTitleAt(i).equals(label)) {
				return i;
			}
		}
		return -1;
	}

	protected void addTab(String label, JComponent component) {
		TabWrapper wrapper = new TabWrapper(new BorderLayout());
		wrapper.add(component);

		this.tabPane.addTab(label, wrapper);
	}

	public void setLastedTab() {
		this.tabPane.setSelectedIndex(this.tabPane.getTabCount() - 1);
	}

	public void removeAll() {
		this.tabPane.removeAll();
	}

	public void showError(Exception e) {
		TabInfo errorInfo = this.createErrorResult(e);
		this.show(errorInfo.title, errorInfo.component);
	}

	private TabInfo createErrorResult(Exception e) {
		return new TabInfo("error", ExceptionPrinter.createComponent(e));
	}

	class TabInfo {
		public String title;
		public JComponent component;

		public TabInfo(String title, JComponent component) {
			this.title = title;
			this.component = component;
		}
	}

}
