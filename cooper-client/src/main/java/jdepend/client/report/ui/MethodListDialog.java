package jdepend.client.report.ui;

import java.awt.BorderLayout;
import java.util.Collection;

import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.metadata.Method;

public class MethodListDialog extends CooperDialog {

	public MethodListDialog(Collection<Method> methods) {
		super();
		getContentPane().setLayout(new BorderLayout());

		MethodListPanel methodListPanel = new MethodListPanel(methods);
		methodListPanel.loadMethodList();
		this.add(methodListPanel);
	}

	public MethodListDialog(jdepend.model.JavaClassUnit javaClass) {
		this(javaClass.getJavaClass().getMethods());

	}
}
