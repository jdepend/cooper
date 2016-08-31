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
import javax.swing.JTextArea;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.ui.component.TextViewer;
import jdepend.framework.ui.config.UIProperty;
import jdepend.framework.util.BundleUtil;

public class SystemLogPanel extends JPanel {

	private JTextArea log;

	private JDependFrame frame;

	private BufferLogWriter bufferLogWriter;

	public SystemLogPanel(JDependFrame frame1) {
		super();

		bufferLogWriter = new BufferLogWriter();
		LogUtil.getInstance().setSystemLogWriter(bufferLogWriter);

		this.frame = frame1;

		setLayout(new BorderLayout());

		log = new JTextArea();
		log.setFont(UIProperty.TEXTFONT);

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem clearItem = new JMenuItem(
				BundleUtil.getString(BundleUtil.Command_Delete));
		clearItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (JOptionPane.showConfirmDialog(frame, "您是否确认删除？", "提示",
							JOptionPane.YES_NO_OPTION) == 0) {
						clear();
					}
				} catch (JDependException e1) {
					e1.printStackTrace();
					frame.showStatusError(e1.getMessage());
				}
			}
		});
		popupMenu.add(clearItem);

		log.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				if (e.getButton() == 3) {
					popupMenu.show((Component) e.getSource(), e.getX(),
							e.getY());
				}
			}
		});

		this.refresh();

		JScrollPane pane = new JScrollPane(log);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);

		// 定时触发refresh
		(new Timer()).schedule(new TimerTask() {

			@Override
			public void run() {
				refresh();
			}

		}, 2000, 300);
	}

	public void clear() throws JDependException {
		this.bufferLogWriter.clear();
		log.setText(null);
	}

	private void refresh() {
		int logCount = this.bufferLogWriter.getCount();
		if (logCount > 0) {
			log.append(this.bufferLogWriter.getBuffer().toString());
			log.setCaretPosition(log.getText().length());
			this.bufferLogWriter.clear();
		}
	}
}
