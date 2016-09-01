package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.dialog.ScoreListDialog;

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