package jdepend.client.ui.result.framework;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jdepend.framework.ui.util.JDependUIUtil;

public class ResultOperationPanel extends JPanel {

	private int currentTabIndex = -1;

	private List<TabIndexInfo> mementoTabs = new LinkedList<TabIndexInfo>();
	
	private ResultPanel resultPanel;

	public ResultOperationPanel(ResultPanel resultPanel1) {
		
		this.resultPanel = resultPanel1;
		this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());

		JPanel operationPanel = new JPanel();
		operationPanel.setLayout(new GridLayout(4, 1));

		operationPanel.add(this.splitPanel());

		JLabel navPriorButton = new JLabel();
		navPriorButton.setIcon(new ImageIcon(JDependUIUtil
				.getImage("nav-left.png")));
		navPriorButton.setCursor(new java.awt.Cursor(
				java.awt.Cursor.HAND_CURSOR));
		navPriorButton.setToolTipText("导航-后退");
		navPriorButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				prior();
				resultPanel.setTab(getCurrentTab().tabOne, getCurrentTab().tabTwo);
			}
		});

		operationPanel.add(navPriorButton);

		operationPanel.add(this.splitPanel());

		JLabel navNextButton = new JLabel();
		navNextButton.setIcon(new ImageIcon(JDependUIUtil
				.getImage("nav-right.png")));
		navNextButton
				.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		navNextButton.setToolTipText("导航-前进");
		navNextButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				next();
				resultPanel.setTab(getCurrentTab().tabOne, getCurrentTab().tabTwo);
			}
		});

		operationPanel.add(navNextButton);

		contentPanel.add(BorderLayout.NORTH, operationPanel);

		this.add(contentPanel);
	}

	private JPanel splitPanel() {
		JPanel splitPanel = new JPanel();

		splitPanel.setBounds(0, 0, 16, 16);

		return splitPanel;
	}

	public void addMemetoTab(int tabOne, int tabTwo) {
		
		TabIndexInfo tabInfo = new TabIndexInfo(tabOne, tabTwo);
		if (mementoTabs.size() > 0
				&& mementoTabs.get(currentTabIndex).equals(tabInfo)) {
			return;
		} else {
			if (currentTabIndex != mementoTabs.size() - 1) {
				for (int index = mementoTabs.size() - 1; index > currentTabIndex; index--) {
					mementoTabs.remove(tabInfo);
				}
			}
			mementoTabs.add(tabInfo);
			currentTabIndex = mementoTabs.size() - 1;
		}
	}

	private void next() {
		if (currentTabIndex < mementoTabs.size() - 1) {
			currentTabIndex++;
		}
	}

	private void prior() {
		if (currentTabIndex > 0) {
			currentTabIndex--;
		}
	}

	public TabIndexInfo getCurrentTab() {
		return mementoTabs.get(currentTabIndex);
	}
	
	public class TabIndexInfo{
		
		public int tabOne;
		public int tabTwo;
		
		public TabIndexInfo(int tabOne, int tabTwo) {
			super();
			this.tabOne = tabOne;
			this.tabTwo = tabTwo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + tabTwo;
			result = prime * result + tabOne;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TabIndexInfo other = (TabIndexInfo) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (tabTwo != other.tabTwo)
				return false;
			if (tabOne != other.tabOne)
				return false;
			return true;
		}

		private ResultOperationPanel getOuterType() {
			return ResultOperationPanel.this;
		}
	}

}
