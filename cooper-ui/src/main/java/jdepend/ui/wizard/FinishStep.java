package jdepend.ui.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.TextViewer;
import jdepend.framework.util.BundleUtil;

public class FinishStep extends Step {

	public FinishStep(NewGroupWizard wizard) {
		super("Finish Wizard", wizard);

		this.setLayout(new BorderLayout());

		JPanel content = new JPanel(new BorderLayout());

		content.add(BorderLayout.CENTER, initSummary());

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createPriorButton());
		buttonBar.add(createFinishButton());
		buttonBar.add(createCancelButton());

		this.add(BorderLayout.CENTER, content);

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	protected Component createFinishButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Finish));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					work();
				} catch (Exception ex) {
					ex.printStackTrace();
					Component source = (Component) e.getSource();
					JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}

	private Component initSummary() {

		TextViewer summary = new TextViewer();

		summary.setText(this.getWorker().toString());
		summary.setCaretPosition(0);

		return new JScrollPane(summary);
	}

	@Override
	protected Step createNextStep() {
		return null;
	}

	@Override
	protected int doWork() throws JDependException {

		this.getWorker().create();
		return DO_NEXT_STEP;
	}

	@Override
	protected void validateData() throws JDependException {
		// TODO Auto-generated method stub

	}

}
