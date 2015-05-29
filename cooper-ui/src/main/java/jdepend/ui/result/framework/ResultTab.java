package jdepend.ui.result.framework;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jdepend.framework.util.BundleUtil;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.CooperTabbedPane;

public class ResultTab extends CooperTabbedPane {

	private JPopupMenu popupMenu;

	public ResultTab(JDependCooper frame) {
		super(frame, true, true, CooperTabbedPane.Workspace);

		this.popupMenu = new JPopupMenu();
		JMenuItem closeItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Close));
		closeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeSelf(e);
			}
		});
		popupMenu.add(closeItem);

		JMenuItem closeOthersItem = new JMenuItem("关闭其它");
		closeOthersItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeOthers();
			}
		});
		popupMenu.add(closeOthersItem);

		JMenuItem closeAllItem = new JMenuItem("关闭所有");
		closeAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeAll();
			}
		});
		popupMenu.add(closeAllItem);

	}

	private void closeOthers() {

		String title = this.getTitleAt(this.getSelectedIndex());
		Component component = this.getComponentAt(this.getSelectedIndex());
		this.removeAll();
		addTab(title, component);
	}

	private void closeAll() {
		this.removeAll();
	}

	private void closeSelf(ActionEvent e) {
		int tabNumber = this.getSelectedIndex();
		if (tabNumber < 0)
			return;
		this.removeTabAt(tabNumber);
	}

	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		ResultTab tab = (ResultTab) e.getSource();

		if (e.getButton() == 3) {
			popupMenu.show(tab, e.getX(), e.getY());
		}
	}

}
