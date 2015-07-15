package jdepend.client.ui.property;

import javax.swing.JComponent;

import jdepend.framework.ui.component.JDependFrame;
import jdepend.model.JDependUnitMgr;
import jdepend.client.report.way.mapui.GraphPanel;
import jdepend.util.todolist.TODORelationData;

public class RelationDataTODOItemRender implements TODOItemRender {

	@Override
	public JComponent render(JDependFrame frame, Object info) {
		TODORelationData relationData = (TODORelationData) info;
		relationData.init();
		relationData.appendRelations(JDependUnitMgr.getInstance().getComponents());
		return new GraphPanel(frame, null, relationData.getRelations());
	}
}
