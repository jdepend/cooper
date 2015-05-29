package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.ui.JDependCooper;
import jdepend.ui.dialog.ScoreAndMetricsDialog;

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
