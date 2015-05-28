package jdepend.server.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.service.remote.JDependSession;
import jdepend.service.remote.JDependSessionService;
import jdepend.service.remote.RemoteServiceFactory;
import jdepend.service.remote.SessionListener;
import jdepend.service.remote.impl.JDependSessionMgr;

public class SessionMgrPanel extends JPanel implements SessionListener {

	private JDependServer server;

	private DefaultTableModel sessionListModel;

	private JDependSessionService sessionService;

	protected Long currentSession;

	public SessionMgrPanel(JDependServer server) {

		this.server = server;

		this.setLayout(new BorderLayout());

		JPanel workspace = new JPanel(new GridLayout(1, 1));

		JPanel clients = new JPanel(new BorderLayout());

		clients.add(BorderLayout.NORTH, new JLabel("连接的客户端列表："));

		JTable sessionsTable = this.initSessionList();

		clients.add(BorderLayout.CENTER, new JScrollPane(sessionsTable));

		workspace.add(clients);

		this.add(BorderLayout.CENTER, workspace);
	}

	private JTable initSessionList() {

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem removeItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		popupMenu.add(removeItem);

		sessionListModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(sessionListModel);

		JTable sessionsTable = new JTable(sorter);

		sessionsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				int currentRow = table.rowAtPoint(e.getPoint());
				if (currentRow >= 0) {
					table.setRowSelectionInterval(currentRow, currentRow);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				int currentRow = table.rowAtPoint(e.getPoint());
				if (currentRow >= 0) {
					table.setRowSelectionInterval(currentRow, currentRow);
				}
				currentSession = (Long) table.getValueAt(table.rowAtPoint(e.getPoint()), 0);
				if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});

		sorter.setTableHeader(sessionsTable.getTableHeader());

		sessionListModel.addColumn("会话ID");
		sessionListModel.addColumn("用户名");
		sessionListModel.addColumn("IP地址");
		sessionListModel.addColumn("创建时间");
		sessionListModel.addColumn("操作");
		sessionListModel.addColumn("注销时间");

		return sessionsTable;
	}

	private void remove() {
		JDependSessionMgr.getInstance().removeSession(currentSession);
	}

	private void refresh() {

	}

	@Override
	public void onCreateSession(JDependSession session) {
		Object[] row = new Object[6];
		row[0] = session.getId();
		row[1] = session.getUserName();
		row[2] = session.getClient();
		row[3] = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(session.getCreateTime());
		row[4] = "登陆";

		sessionListModel.addRow(row);
	}

	@Override
	public void onRemoveSession(JDependSession session) {

		for (int row = 0; row < sessionListModel.getRowCount(); row++) {
			if ((Long) sessionListModel.getValueAt(row, 0) == session.getId()) {
				sessionListModel.setValueAt("注销", row, 4);
				sessionListModel.setValueAt(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance()
						.getTime()), row, 5);
			}
		}
	}

	@Override
	public void onChangeState(JDependSession session) {
	}

	public void bindService() throws JDependException {
		try {
			this.sessionService = RemoteServiceFactory.createJDependSessionService();
			JDependSessionMgr.getInstance().addListener(this);
			Naming.rebind("rmi://localhost:1099/JDependSessionService", sessionService);
		} catch (Exception e) {
			throw new JDependException("绑定会话服务错误！", e);
		}
	}

	public void unbindService() throws JDependException {
		try {
			Naming.unbind("rmi://localhost:1099/JDependSessionService");
		} catch (Exception e) {
			throw new JDependException("解除会话服务绑定错误！", e);
		}
	}
}
