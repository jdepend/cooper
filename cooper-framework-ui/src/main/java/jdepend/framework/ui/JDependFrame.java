package jdepend.framework.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import jdepend.framework.exception.JDependException;

public abstract class JDependFrame extends JFrame {

	protected Map<String, String> resourceStrings;

	protected Map<String, AbstractAction> actions;

	protected Map<String, String> accelerators;

	private StatusPanel statusPanel;

	private StatusField statusField;

	private JProgressBar progressBar;

	private static Font BOLD_FONT = new Font("dialog", Font.BOLD, 12);

	protected Dimension scrSize;

	public JDependFrame(String name) {
		super(name);
	}

	public void display() {
		this.doDisplay();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		scrSize = this.getSize();
	}

	protected void doDisplay() {
	}

	protected StatusPanel getStatusPanel() {
		if (statusPanel == null) {
			statusPanel = createStatusPanel();
		}
		return statusPanel;
	}

	private StatusPanel createStatusPanel() {
		StatusPanel panel = new StatusPanel();
		panel.setStatusComponent(getStatusField());

		return panel;
	}

	public StatusField getStatusField() {
		if (statusField == null) {
			statusField = createStatusField();
		}
		return statusField;
	}

	protected StatusField createStatusField() {
		return new StatusField(this);
	}

	public JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = createProgressBar();
		}

		return progressBar;
	}

	public abstract void show(Map<String, JComponent> result);

	private JProgressBar createProgressBar() {
		JProgressBar bar = new JProgressBar();
		bar.setStringPainted(true);

		return bar;
	}

	public void startProgressMonitor(final int maxValue) {
		if (maxValue > 0) {
			getProgressBar().setMinimum(0);
			getProgressBar().setMaximum(maxValue);
			getStatusPanel().setStatusComponent(getProgressBar());
		}
	}

	public void stopProgressMonitor() {
		getStatusPanel().setStatusComponent(getStatusField());
		getProgressBar().setValue(0);
	}

	public void progress(int progress) {
		this.getProgressBar().setValue(this.getProgressBar().getValue() + progress);
	}

	public void progress() {
		this.progress(1);
	}

	public void showStatusMessage(final String message) {
		getStatusField().setFont(BOLD_FONT);
		getStatusField().setForeground(Color.black);
		getStatusField().setText(" " + message);
	}

	public void showStatusError(final String message) {
		getStatusField().setFont(BOLD_FONT);
		getStatusField().setForeground(Color.red);
		getStatusField().setText(" " + message);
	}

	public void postStatusMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				showStatusMessage(message);
			}
		});
	}

	public void postStatusError(final String message) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				showStatusError(message);
			}
		});
	}

	public abstract void refresh() throws JDependException;

	public Dimension getScrSize() {
		return scrSize;
	}
	
	public void onRefactoring() throws JDependException{
	}

	protected JMenuBar createMenubar() {

		JMenuBar menuBar = new JMenuBar();

		String[] menuKeys = tokenize((String) resourceStrings.get("menubar"));
		for (int i = 0; i < menuKeys.length; i++) {
			JMenu m = createMenu(menuKeys[i]);
			if (m != null) {
				menuBar.add(m);
			}
		}
		return menuBar;
	}

	private JMenu createMenu(String key) {

		String[] itemKeys = tokenize((String) resourceStrings.get(key));
		JMenu menu = new JMenu(key);
		for (int i = 0; i < itemKeys.length; i++) {
			if (itemKeys[i].equals("-")) {
				menu.addSeparator();
			} else {
				JMenuItem mi = createMenuItem(itemKeys[i]);
				menu.add(mi);
			}
		}

		menu.setMnemonic(this.accelerators.get(key).charAt(0));

		return menu;
	}

	private JMenuItem createMenuItem(String key) {

		JMenuItem mi = new JMenuItem(key);

		if (this.accelerators.get(key) != null) {
			mi.setMnemonic(this.accelerators.get(key).charAt(0));
			mi.setAccelerator(KeyStroke.getKeyStroke(this.accelerators.get(key).charAt(0), java.awt.Event.ALT_MASK));
		}
		String actionString = key;
		mi.setActionCommand(actionString);

		Action a = getActionForCommand(actionString);
		if (a != null) {
			mi.addActionListener(a);
			mi.setEnabled(a.isEnabled());
		} else {
			mi.setEnabled(false);
		}

		return mi;
	}

	private String[] tokenize(String input) {
		return input.split("/");
	}

	private Action getActionForCommand(String command) {
		return (Action) actions.get(command);
	}
}
