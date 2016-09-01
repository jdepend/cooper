package jdepend.client.ui.wizard;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import jdepend.framework.exception.JDependException;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.componentconf.ComponentModelPanel;

public final class NewGroupWizard extends JDialog {

	private NewGroupWorker worker = new DefaultNewGroupWorker();

	private JDependCooper frame;

	private List<Step> steps = new ArrayList<Step>();

	private int currentStepIndex = -1;

	public NewGroupWizard(JDependCooper frame) throws JDependException {

		setTitle("增加组向导");

		setResizable(false);

		getContentPane().setLayout(new BorderLayout());
		setSize(ComponentModelPanel.Width, ComponentModelPanel.Height);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		this.frame = frame;

		this.nextStep(this.createRoot());
	}

	public void nextStep(Step next) throws JDependException {
		if (next != null) {
			// 维护StepList
			next = this.doNextStep(next);
			next.refresh();

			this.setVisible(false);
			getContentPane().removeAll();
			getContentPane().add(BorderLayout.CENTER, next);
			this.setVisible(true);
			this.repaint();
		} else {
			this.frame.getGroupPanel().refreshGroup();
			this.dispose();
		}
	}

	public void priorStep() {
		this.setVisible(false);
		getContentPane().removeAll();
		getContentPane().add(BorderLayout.CENTER, this.getPriorStep());
		this.setVisible(true);
		this.repaint();
	}

	private Step createRoot() {
		return new RootStep(this);
	}

	public JDependCooper getFrame() {
		return frame;
	}

	public NewGroupWorker getWorker() {
		return worker;
	}

	private Step getPriorStep() {
		if (currentStepIndex > 0) {
			currentStepIndex--;
			return this.steps.get(currentStepIndex);
		} else {
			return null;
		}
	}

	private Step doNextStep(Step next) {
		if (this.steps.contains(next)) {
			for (Step step : steps) {
				if (step.equals(next)) {
					next = step;
				}
			}
		} else {
			this.steps.add(next);
		}
		this.currentStepIndex++;

		return next;
	}
}
