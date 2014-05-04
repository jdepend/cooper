package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JavaClass;

public class ClassListDialog extends CooperDialog {

	public ClassListDialog(jdepend.model.Component component) {
		super();
		getContentPane().setLayout(new BorderLayout());

		final ClassListPanel classListPanel = new ClassListPanel();
		this.add(classListPanel);
		classListPanel.showClassList(component);

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(classListPanel.getClassListTable());
			}
		});
		popupMenu.add(saveAsItem);

		classListPanel.getClassListTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getButton() == 3) {
					popupMenu.show(classListPanel.getClassListTable(), e.getX(), e.getY());
				}
			}
		});
	}

}
