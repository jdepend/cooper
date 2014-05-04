package jdepend.ui.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;

public abstract class Step extends JPanel {

	private NewGroupWizard wizard;

	protected static final int DO_PRIOR_STEP = -1;
	protected static final int DO_NEXT_STEP = 0;
	protected static final int CANCEL_NEXT_STEP = 1;

	public Step(String name, NewGroupWizard wizard) {
		this.setName(name);
		this.wizard = wizard;
		this.wizard.setTitle(name);
	}

	protected Component createPriorButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_PriorStep));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					wizard.priorStep();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(getWizard(), ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}

	protected Component createNextButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_NextStep));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					work();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(getWizard(), ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}

	protected Component createCancelButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Cancel));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				wizard.dispose();
			}
		});

		return button;
	}

	protected Component createSkipButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_SkipStep));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					skip();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(getWizard(), ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}

	protected Component createRepeatButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_RepeatStep));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					repeat();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(getWizard(), ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}

	public final void work() throws JDependException {
		this.validateData();
		if (this.doWork() == DO_NEXT_STEP) {
			this.wizard.nextStep(this.createNextStep());
		}
	}

	public final void repeat() throws JDependException {
		this.validateData();
		this.doWork();
		try {
			this.wizard.nextStep((Step) this.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public final void skip() throws JDependException {
		this.wizard.nextStep(this.createNextStep());
	}

	protected abstract void validateData() throws JDependException;

	protected abstract int doWork() throws JDependException;

	protected abstract Step createNextStep();

	protected NewGroupWorker getWorker() {
		return this.wizard.getWorker();
	}

	protected NewGroupWizard getWizard() {
		return wizard;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Step other = (Step) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

}
