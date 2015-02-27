package jdepend.framework.ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
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
	private Label l6;
	private Label l7;
	private Label l8;
	private Label l9;
	private Label l10;

	private JSlider slider;

	private int current;

	private Timer timer;

	public VersionDialog() {
		getContentPane().setLayout(new BorderLayout());
		setSize(800, 300);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		this.data = getVersionData();

		getContentPane().add(BorderLayout.CENTER, this.createContentPanel());
		getContentPane().add(BorderLayout.SOUTH, this.createJSlider());

		PlayTask playTask = new PlayTask();

		timer = new Timer();
		timer.schedule(playTask, 0, 1000);
	}

	private JPanel createContentPanel() {
		JPanel contentPanel = new JPanel(new GridLayout(10, 1));

		l1 = new Label();
		l2 = new Label();
		l3 = new Label();
		l4 = new Label();
		l5 = new Label();
		l6 = new Label();
		l7 = new Label();
		l8 = new Label();
		l9 = new Label();
		l10 = new Label();

		contentPanel.add(l1);
		contentPanel.add(l2);
		contentPanel.add(l3);
		contentPanel.add(l4);
		contentPanel.add(l5);
		contentPanel.add(l6);
		contentPanel.add(l7);
		contentPanel.add(l8);
		contentPanel.add(l9);
		contentPanel.add(l10);

		return contentPanel;
	}

	private JSlider createJSlider() {
		this.slider = new JSlider(0, data.length - 10, 0);

		this.slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				current = slider.getValue();
				if (data.length - current > 10) {
					if (timer == null) {
						PlayTask playTask = new PlayTask();

						timer = new Timer();
						timer.schedule(playTask, 0, 1000);
					}
				}
			}
		});

		return this.slider;
	}

	private void play() {
		if (data.length - current > 10) {

			l1.setText(data[data.length - current - 10]);
			l2.setText(data[data.length - current - 9]);
			l3.setText(data[data.length - current - 8]);
			l4.setText(data[data.length - current - 7]);
			l5.setText(data[data.length - current - 6]);
			l6.setText(data[data.length - current - 5]);
			l7.setText(data[data.length - current - 4]);
			l8.setText(data[data.length - current - 3]);
			l9.setText(data[data.length - current - 2]);
			l10.setText(data[data.length - current - 1]);

			current++;
			this.slider.setValue(current);
		} else {
			l1.setText(data[0]);
			l2.setText(data[1]);
			l3.setText(data[2]);
			l4.setText(data[3]);
			l5.setText(data[4]);
			l6.setText(data[5]);
			l7.setText(data[6]);
			l8.setText(data[7]);
			l9.setText(data[8]);
			l10.setText(data[9]);

			timer.cancel();
			timer = null;
		}
	}

	private String[] getVersionData() {
		InputStream input = null;
		try {
			input = VersionDialog.class.getResourceAsStream(VERSION_FILE);
			byte[] data = StreamUtil.getData(input);

			return new String(data, "UTF-8").split("\n");
		} catch (Exception e) {
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
