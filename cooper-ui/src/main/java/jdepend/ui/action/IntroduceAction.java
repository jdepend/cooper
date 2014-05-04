package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.ui.IntroduceDialog;
import jdepend.ui.JDependCooper;

public class IntroduceAction extends AbstractAction {

	private JDependCooper frame;

	public IntroduceAction(JDependCooper frame) {
		super("介绍");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		IntroduceDialog d = new IntroduceDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}