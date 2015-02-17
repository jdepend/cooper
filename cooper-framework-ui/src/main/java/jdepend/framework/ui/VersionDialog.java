package jdepend.framework.ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.StreamUtil;

public class VersionDialog extends JDialog {

	private static final String VERSION_FILE = "/version.txt";

	private String[] data;

	private Label l1;
	private Label l2;
	private Label l3;
	private Label l4;
	private Label l5;

	private JSlider slider;

	private int current;

	private Timer timer;

	public VersionDialog() {
		getContentPane().setLayout(new BorderLayout());
		setSize(800, 200);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		this.data = getVersionData();

		getContentPane().add(BorderLayout.CENTER, this.createContentPanel());
		getContentPane().add(BorderLayout.SOUTH, this.createJSlider());

		timer = new Timer();
		timer.schedule(new PlayTask(), 0, 1000);
	}

	private JPanel createContentPanel() {
		JPanel contentPanel = new JPanel(new GridLayout(5, 1));
	
		l1 = new Label();
		l2 = new Label();
		l3 = new Label();
		l4 = new Label();
		l5 = new Label();

		contentPanel.add(l1);
		contentPanel.add(l2);
		contentPanel.add(l3);
		contentPanel.add(l4);
		contentPanel.add(l5);

		return contentPanel;
	}

	private JSlider createJSlider() {
		this.slider = new JSlider(0, data.length - 5, 0);

		this.slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				current = slider.getValue();
			}
		});

		return this.slider;
	}

	private void play() {
		if (data.length - current > 5) {

			l1.setText(data[data.length - current - 5]);
			l2.setText(data[data.length - current - 4]);
			l3.setText(data[data.length - current - 3]);
			l4.setText(data[data.length - current - 2]);
			l5.setText(data[data.length - current - 1]);

			current++;
			this.slider.setValue(current);
		} else {
			l1.setText(data[0]);
			l2.setText(data[1]);
			l3.setText(data[2]);
			l4.setText(data[3]);
			l5.setText(data[4]);
		}
	}

	private String[] getVersionData() {
		InputStream input = null;
		try {
			input = VersionDialog.class.getResourceAsStream(VERSION_FILE);
			byte[] data = StreamUtil.getData(input);

			return new String(data).split("\n");
		} catch (JDependException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class PlayTask extends TimerTask {

		public PlayTask() {
		}

		@Override
		public void run() {
			play();
		}
	}

	class Label extends AlphaPane {

		private String text;

		public Label() {
		}

		public void setText(String text) {
			this.text = text;
			this.repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawString(text, 20, 10);
		}
	}
}
