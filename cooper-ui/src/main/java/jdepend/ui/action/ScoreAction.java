package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.ui.JDependCooper;
import jdepend.ui.ScoreListDialog;

public class ScoreAction extends AbstractAction {
	private JDependCooper frame;

	public ScoreAction(JDependCooper frame) {
		super("分数列表");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		ScoreListDialog d = new ScoreListDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}