package jdepend.client.report.ui;

import java.awt.BorderLayout;
import java.util.Collection;

import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.model.JavaClassUnit;

public class ClassListDialog extends CooperDialog {

	public ClassListDialog(JDependFrame frame, Collection<JavaClassUnit> javaClasses) {
		super("类列表");
		getContentPane().setLayout(new BorderLayout());

		ClassListPanel classListPanel = new ClassListPanel(frame);
		this.add(classListPanel);
		classListPanel.showClassList(javaClasses);

		classListPanel.initPopupMenu(new JavaClassMoveToDialogListener() {
			@Override
			public void onFinish() {
				ClassListDialog.this.dispose();
			}
		});
	}

	public ClassListDialog(JDependFrame frame, jdepend.model.Component component) {
		super("类列表");
		getContentPane().setLayout(new BorderLayout());

		ClassListPanel classListPanel = new ClassListPanel(frame);
		this.add(classListPanel);
		classListPanel.showClassList(component);

		classListPanel.initPopupMenu(new JavaClassMoveToDialogListener() {
			@Override
			public void onFinish() {
				ClassListDialog.this.dispose();
			}
		});
	}

	public ClassListDialog(JDependFrame frame) {
		super("类列表");
		this.setSize(frame.getScrSize().width, frame.getScrSize().height);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示
		getContentPane().setLayout(new BorderLayout());

		ClassListPanel classListPanel = new ClassListPanel(frame);

		classListPanel.initPopupMenu(new JavaClassMoveToDialogListener() {
			@Override
			public void onFinish() {
				ClassListDialog.this.dispose();
			}
		});

		ClassListOperationPanel classListOperationPanel = new ClassListOperationPanel(classListPanel);

		this.add(classListOperationPanel);
	}

}
