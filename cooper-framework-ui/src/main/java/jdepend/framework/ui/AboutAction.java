package jdepend.framework.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public final class AboutAction extends AbstractAction {

	private JDependFrame frame;

	/**
	 * Constructs an <code>AboutAction</code> instance.
	 */
	public AboutAction(JDependFrame frame) {
		super("关于");
		this.frame = frame;
	}

	/**
	 * Handles the action.
	 */
	public void actionPerformed(ActionEvent e) {
		AboutDialog d = new AboutDialog(frame);
		d.setModal(true);
		d.setVisible(true);
	}
}