package jdepend.client.ui.property;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.FileLogWriter;
import jdepend.framework.log.LogListener;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.ui.component.TextViewer;
import jdepend.framework.util.BundleUtil;

public class SystemLogPanel extends JPanel implements LogListener {

	private TextViewer log;

	public static final int interval = 10;// 显示间隔

	private int count = 0;// 显示计数

	private JDependFrame frame;

	public SystemLogPanel(JDependFrame frame1) {
		super();

		this.frame = frame1;

		setLayout(new BorderLayout());

		log = new TextViewer();

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem clearItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		clearItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (JOptionPane.showConfirmDialog(frame, "您是否确认删除？", "提示", JOptionPane.YES_NO_OPTION) == 0) {
						clear();
						refresh();
					}
				} catch (JDependException e1) {
					e1.printStackTrace();
					frame.showStatusError(e1.getMessage());
				}
			}
		});
		popupMenu.add(clearItem);

		JMenuItem refreshItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Refresh));
		refreshItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onLog();
			}
		});
		popupMenu.add(refreshItem);

		log.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				if (e.getButton() == 3) {
					popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}
		});

		this.refresh();

		JScrollPane pane = new JScrollPane(log);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);

		// 设置Listener
		LogUtil.getInstance().addLogListener(this);

		// 定时触发refresh
		(new Timer()).schedule(new LogTask(this), 2000, 5000);
	}

	private void clear() throws JDependException {
		FileLogWriter flog = new FileLogWriter();
		flog.clear();

		this.refresh();

	}

	private void refresh() {
		FileLogWriter flog = new FileLogWriter();

		try {
			log.setText(flog.read().toString());
			count = 0;
		} catch (JDependException e) {
			e.printStackTrace();
		}
	}

	public synchronized void onLog() {
		if (++count >= interval) {
			this.refresh();
		}
	}

	class LogTask extends TimerTask {

		private LogListener logListener;

		public LogTask(LogListener logListener) {
			this.logListener = logListener;
		}

		@Override
		public void run() {
			logListener.onLog();
		}
	}
}
