package jdepend.ui.property;

import javax.swing.JComponent;

import jdepend.framework.ui.component.JDependFrame;
import jdepend.report.way.mapui.GraphPanel;
import jdepend.util.todolist.TODORelationData;

public class RelationDataTODOItemRender implements TODOItemRender {

	@Override
	public JComponent render(JDependFrame frame, Object info) {
		TODORelationData relationData = (TODORelationData) info;
		relationData.init();
		relationData.appendRelations();
		return new GraphPanel(frame, null, relationData.getRelations());
	}
}
