package jdepend.ui.property;

import javax.swing.JComponent;

import jdepend.framework.ui.JDependFrame;
import jdepend.report.way.mapui.GraphPanel;
import jdepend.util.todolist.RelationData;

public class RelationDataTODOItemRender implements TODOItemRender {

	@Override
	public JComponent render(JDependFrame frame, Object info) {
		RelationData relationData = (RelationData) info;
		return new GraphPanel(frame, relationData.getRelations());
	}
}
