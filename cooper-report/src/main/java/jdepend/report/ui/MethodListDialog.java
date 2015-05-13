package jdepend.report.ui;

import java.awt.BorderLayout;
import java.util.Collection;

import jdepend.framework.ui.CooperDialog;
import jdepend.model.Method;

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
