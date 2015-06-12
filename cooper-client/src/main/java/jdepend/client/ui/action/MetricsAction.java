package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.dialog.MetricsDialog;

public class MetricsAction extends AbstractAction {

	private JDependCooper frame;

	public MetricsAction(JDependCooper frame) {
		super("指标");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		MetricsDialog d = new MetricsDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}
