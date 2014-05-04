package jdepend.report.way.dddui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jdepend.model.JDependUnitMgr;
import jdepend.report.ui.ClassListDialog;

import org.wilmascope.control.OptionsClient;
import org.wilmascope.control.GraphControl.GraphElementFacade;

public class NodeOptionsMenu extends JPopupMenu implements OptionsClient {

	private DDDJDepend depend;

	private JMenuItem viewClassListItem = new JMenuItem("查看类列表");

	private String currentName;

	public NodeOptionsMenu(DDDJDepend depend) {
		this.depend = depend;

		viewClassListItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showClassList(currentName);
			}
		});
		this.add(viewClassListItem);
	}

	@Override
	public void callback(MouseEvent e, GraphElementFacade n) {
		currentName = n.getView().getLabel();
		show(depend, e.getX(), e.getY());
		updateUI();
	}

	private void showClassList(String source) {
		jdepend.model.Component component = JDependUnitMgr.getInstance().getTheComponent(source);
		ClassListDialog d = new ClassListDialog(component);
		d.setModal(true);
		d.setVisible(true);

	}
}
