package jdepend.report.way.dddui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jdepend.report.ui.RelationDetailDialog;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.OptionsClient;
import org.wilmascope.control.GraphControl.GraphElementFacade;

public class EdgeOptionsMenu extends JPopupMenu implements OptionsClient {

	private DDDJDepend depend;

	private JMenuItem viewRelationDetailItem = new JMenuItem("查看关系明细");

	private String start;

	private String end;

	public EdgeOptionsMenu(DDDJDepend depend) {
		this.depend = depend;

		viewRelationDetailItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewRelation(start, end);
			}
		});
		this.add(viewRelationDetailItem);
	}

	@Override
	public void callback(MouseEvent e, GraphElementFacade n) {
		if (n instanceof GraphControl.Edge) {
			GraphControl.Edge edge = (GraphControl.Edge) n;
			this.start = edge.getStartNode().getView().getLabel();
			this.end = edge.getEndNode().getView().getLabel();
			show(depend, e.getX(), e.getY());
			updateUI();
		}
	}

	private void viewRelation(String source, String target) {
		RelationDetailDialog d = new RelationDetailDialog(source, target);
		d.setModal(true);
		d.setVisible(true);
	}

}
