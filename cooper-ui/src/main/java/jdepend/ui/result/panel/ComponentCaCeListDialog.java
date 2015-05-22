package jdepend.ui.result.panel;

import java.util.Collection;
import java.util.HashSet;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.model.Relation;
import jdepend.report.util.ReportConstant;

public final class ComponentCaCeListDialog extends CooperDialog {

	public ComponentCaCeListDialog(JDependFrame frame, jdepend.model.Component component, String metrics) {

		super(component.getName() + " " + metrics + " list");

		Collection<Relation> relations = new HashSet<Relation>();
		if (metrics.equals(ReportConstant.Ca)) {
			relations = component.getAfferentRelations();
		} else if (metrics.equals(ReportConstant.Ce)) {
			relations = component.getEfferentRelations();
		}

		this.add(new RelationListPanel(frame, relations));
	}

}
