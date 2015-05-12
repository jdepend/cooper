package jdepend.report.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.TextViewer;
import jdepend.model.JavaClassUnit;

public class JavaClassDetailDialog extends CooperDialog {

	public JavaClassDetailDialog(JavaClassUnit javaClass) {

		super(javaClass.getName());

		getContentPane().setLayout(new BorderLayout());

		TextViewer classProperty = new TextViewer();

		classProperty.setText(javaClass.toString());
		classProperty.setCaretPosition(0);

		this.add(new JScrollPane(classProperty));

	}
}