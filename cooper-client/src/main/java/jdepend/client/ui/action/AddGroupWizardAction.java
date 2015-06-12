package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import jdepend.framework.exception.JDependException;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.wizard.NewGroupWizard;

//
// Add Group action handler.
//
public class AddGroupWizardAction extends AbstractAction {

	private JDependCooper frame;

	/**
	 * Constructs an <code>AddGroupWizardAction</code> instance.
	 */
	public AddGroupWizardAction(JDependCooper frame) {
		super("Add Group Wizard");
		this.frame = frame;
	}

	/**
	 * Handles the action.
	 */
	public void actionPerformed(ActionEvent e) {
		NewGroupWizard d;
		try {
			d = new NewGroupWizard(this.frame);
			d.setModal(true);
			d.setVisible(true);
		} catch (JDependException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this.frame, e1.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
		}

	}
}
