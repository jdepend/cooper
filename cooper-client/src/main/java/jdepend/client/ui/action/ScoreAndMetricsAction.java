package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.dialog.ScoreAndMetricsDialog;

public class ScoreAndMetricsAction extends AbstractAction {
	private JDependCooper frame;

	public ScoreAndMetricsAction(JDependCooper frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ScoreAndMetricsDialog d = new ScoreAndMetricsDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}
