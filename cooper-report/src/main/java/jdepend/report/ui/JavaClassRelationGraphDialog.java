package jdepend.report.ui;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassWrapper;
import jdepend.report.way.mapui.GraphPanel;

public class JavaClassRelationGraphDialog extends CooperDialog {

	public JavaClassRelationGraphDialog(JDependFrame frame, JavaClass javaClass) {
		super(javaClass.getName() + "传出|传入");
		this.add(new GraphPanel(frame, this, new JavaClassWrapper(javaClass).getRelations()));
	}

}
