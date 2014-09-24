package jdepend.report.ui;

import java.awt.BorderLayout;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.model.Relation;
import jdepend.report.way.mapui.GraphPanel;
import jdepend.util.todolist.RelationData;

public class RelationDetailForMoveDialog extends CooperDialog {

	private JDependFrame frame;

	public RelationDetailForMoveDialog(JDependFrame frame, Relation relation) {

		this.frame = frame;

		this.setLayout(new BorderLayout());
		
		RelationData relationData = new RelationData(relation);
		relationData.init();
		this.add(new GraphPanel(this.frame, relationData.getRelations()));
	}

}
