package jdepend.report.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.TextViewer;
import jdepend.metadata.Method;

public class MethodDetailDialog extends CooperDialog {

	public MethodDetailDialog(Method method) {

		getContentPane().setLayout(new BorderLayout());

		TextViewer classProperty = new TextViewer();

		classProperty.setText(method.toString());
		classProperty.setCaretPosition(0);

		this.add(new JScrollPane(classProperty));

	}
}