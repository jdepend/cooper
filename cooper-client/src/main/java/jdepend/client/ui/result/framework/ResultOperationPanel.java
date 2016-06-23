package jdepend.client.ui.result.framework;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jdepend.framework.ui.util.JDependUIUtil;

public class ResultOperationPanel extends JPanel {

	private int currentTabIndex = -1;

	private List<Integer> mementoTabs = new LinkedList<Integer>();
	
	private JTabbedPane tab;

	public ResultOperationPanel(JTabbedPane tab1) {
		
		this.tab = tab1;
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
				tab.setSelectedIndex(getCurrentTab());
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
				tab.setSelectedIndex(getCurrentTab());
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

	public void addMemetoTab(int tabNumber) {
		if (mementoTabs.size() > 0
				&& mementoTabs.get(currentTabIndex).equals(tabNumber)) {
			return;
		} else {
			if (currentTabIndex != mementoTabs.size() - 1) {
				for (int index = mementoTabs.size() - 1; index > currentTabIndex; index--) {
					mementoTabs.remove(index);
				}
			}
			mementoTabs.add(tabNumber);
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

	public Integer getCurrentTab() {
		return mementoTabs.get(currentTabIndex);
	}

}
