package jdepend.client.report.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import jdepend.framework.ui.component.TextViewer;
import jdepend.framework.ui.dialog.CooperDialog;
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