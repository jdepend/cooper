package jdepend.ui.result;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import jdepend.ui.framework.UIPropertyConfigurator;

public class SubResultTab extends JTabbedPane implements MouseListener {

	private JPopupMenu popupMenu;
	private ResultTab parentTab;

	public SubResultTab() {
		super();

		this.setTabPlacement(JTabbedPane.BOTTOM);
		addMouseListener(this);
		
		this.popupMenu = new JPopupMenu();
		JMenuItem defaultItem = new JMenuItem("设为默认显示");
		defaultItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					setDefault(e);
				} catch (Exception e1) {
					e1.printStackTrace();
					Component source = (Component) e.getSource();
					JOptionPane.showMessageDialog(source, "设置失败", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(defaultItem);
	}

	private void setDefault(ActionEvent e) throws IOException {
		UIPropertyConfigurator.getInstance().setDefaultTab(this.parentTab.getSelectedIndex(), this.getSelectedIndex());
	}

	public void setParentTab(ResultTab tabPane) {
		this.parentTab = tabPane;

	}

	public void mouseClicked(MouseEvent e) {

		SubResultTab tab = (SubResultTab) e.getSource();

		if (e.getButton() == 3) {
			popupMenu.show(tab, e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
