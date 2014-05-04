package jdepend.report.ui;

import jdepend.framework.ui.CooperDialog;
import jdepend.model.Relation;

public class RelationDetailDialog extends CooperDialog {

	public RelationDetailDialog(Relation relation) {
		super(relation.getCurrent().getName() + " 依赖于 " + relation.getDepend().getName());
		this.add(new RelationDetailPanel(relation));
	}

	public RelationDetailDialog(String current, String depend) {
		super(current + " 依赖于 " + depend);
		this.add(new RelationDetailPanel(current, depend));
	}
}
