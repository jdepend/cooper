package jdepend.ui.motive;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import jdepend.framework.util.BundleUtil;
import jdepend.knowledge.motive.MotiveContainer;
import jdepend.ui.JDependCooper;

public abstract class MotivePanel extends JPanel {

	protected JDependCooper frame;

	protected MotiveDialog motiveDialog;

	protected MotiveContainer motiveContainer;

	public MotivePanel(JDependCooper frame, MotiveDialog motiveDialog) {

		this.frame = frame;
		this.motiveDialog = motiveDialog;
		this.motiveContainer = motiveDialog.getMotiveContainer();
		
		this.setLayout(new BorderLayout());

		this.add(BorderLayout.CENTER, createWorkspacePanel());

		this.add(BorderLayout.SOUTH, createButtonBar());
	}

	protected abstract JPanel createWorkspacePanel();

	protected abstract JPanel createButtonBar();

	protected Component createCloseButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motiveDialog.dispose();
			}
		});

		return button;
	}

	public MotiveContainer getMotiveContainer() {
		return motiveContainer;
	}

}
