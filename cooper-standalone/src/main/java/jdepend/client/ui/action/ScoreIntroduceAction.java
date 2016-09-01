package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.dialog.ScoreIntroduceDialog;

public class ScoreIntroduceAction extends AbstractAction {

	private JDependCooper frame;

	public ScoreIntroduceAction(JDependCooper frame) {
		super("分数介绍");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		ScoreIntroduceDialog d = new ScoreIntroduceDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}
