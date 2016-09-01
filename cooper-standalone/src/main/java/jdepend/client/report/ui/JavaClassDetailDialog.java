package jdepend.client.report.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import jdepend.framework.ui.component.TextViewer;
import jdepend.framework.ui.dialog.CooperDialog;
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