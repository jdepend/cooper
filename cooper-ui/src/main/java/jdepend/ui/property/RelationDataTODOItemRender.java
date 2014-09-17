package jdepend.ui.property;

import javax.swing.JComponent;

import jdepend.report.way.mapui.GraphJDepend;
import jdepend.util.todolist.RelationData;

public class RelationDataTODOItemRender implements TODOItemRender {

	@Override
	public JComponent render(Object info) {
		RelationData relationData = (RelationData) info;
		return GraphJDepend.printGraph(relationData.getRelations());
	}
}
