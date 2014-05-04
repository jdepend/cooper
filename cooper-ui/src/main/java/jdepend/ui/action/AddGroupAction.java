package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.ui.JDependCooper;
import jdepend.ui.command.CreateGroupDialog;

//
// Add Group action handler.
//
public class AddGroupAction extends AbstractAction {

	private JDependCooper frame;

	/**
	 * Constructs an <code>ExitAction</code> instance.
	 */
	public AddGroupAction(JDependCooper frame) {
		super("增加组");
		this.frame = frame;
	}

	/**
	 * Handles the action.
	 */
	public void actionPerformed(ActionEvent e) {
		CreateGroupDialog d = new CreateGroupDialog(this.frame, null);
		d.setModal(true);
		d.setVisible(true);
	}
}
