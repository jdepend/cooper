package jdepend.report.ui;

import java.awt.BorderLayout;
import java.util.Collection;

import jdepend.framework.ui.CooperDialog;
import jdepend.model.Method;

public class MethodListDialog extends CooperDialog {

	public MethodListDialog(Collection<Method> methods) {
		super();
		getContentPane().setLayout(new BorderLayout());

		this.add(new MethodListPanel(methods));
	}

	public MethodListDialog(jdepend.model.JavaClass javaClass) {
		this(javaClass.getMethods());

	}
}
