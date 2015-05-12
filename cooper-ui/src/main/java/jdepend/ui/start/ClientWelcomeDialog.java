package jdepend.ui.start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import jdepend.framework.ui.WelcomeDialog;
import jdepend.model.JavaClassUnit;
import jdepend.parse.ParseListener;
import jdepend.ui.JDependCooper;

public class ClientWelcomeDialog extends WelcomeDialog implements ParseListener {

	private JProgressBar progressBar;

	private int process;

	private JDependCooper frame;

	public ClientWelcomeDialog(JDependCooper frame) {
		super();

		this.frame = frame;

		JPanel welcomePanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		this.add(BorderLayout.CENTER, welcomePanel);
		(new Timer()).schedule(new ChangeTask(this), 400, 400);
	}

	public JDependCooper getFrame() {
		return frame;
	}

	public void startProgressMonitor(final int maxValue) {
		setupProgress();

		progressBar.setMinimum(0);
		progressBar.setMaximum(maxValue);
	}

	private void setupProgress() {

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBackground(Color.WHITE);
		progressBar.setForeground(Color.GREEN);

		this.add(BorderLayout.SOUTH, progressBar);
		this.setVisible(true);
	}

	public void showProgress() {
		if (progressBar != null) {
			progressBar.setValue(this.process);
		}
	}

	class ChangeTask extends TimerTask {

		private ClientWelcomeDialog dialog;

		public ChangeTask(ClientWelcomeDialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void run() {
			dialog.showProgress();
		}
	}

	@Override
	public void onParsedJavaClass(JavaClassUnit parsedClass, int process) {
		this.process += process;
	}
}
