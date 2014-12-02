package jdepend.report.ui;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.model.Relation;
import jdepend.report.way.mapui.GraphPanel;
import jdepend.util.todolist.RelationData;

public class RelationDetailForMoveDialog extends CooperDialog {

	private JDependFrame frame;

	private JDialog parentDialog;

	public RelationDetailForMoveDialog(JDependFrame frame, JDialog parentDialog, Relation relation) {

		this.frame = frame;

		this.parentDialog = parentDialog;

		this.setLayout(new BorderLayout());

		RelationData relationData = new RelationData(relation);
		relationData.init();
		relationData.appendRelations();
		this.add(new GraphPanel(this.frame, this, relationData.getRelations()));
	}

	@Override
	public void dispose() {
		if (this.parentDialog != null) {
			this.parentDialog.dispose();
		}
		super.dispose();
	}

}
