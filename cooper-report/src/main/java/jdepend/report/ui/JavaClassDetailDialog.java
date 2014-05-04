package jdepend.report.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.TextViewer;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;

public class JavaClassDetailDialog extends CooperDialog {

	public JavaClassDetailDialog(String name) {

		super(name);

		getContentPane().setLayout(new BorderLayout());

		TextViewer classProperty = new TextViewer();

		JavaClass javaClass = JDependUnitMgr.getInstance().getTheClass(name);

		classProperty.setText(javaClass.toString());
		classProperty.setCaretPosition(0);

		this.add(new JScrollPane(classProperty));

	}
}