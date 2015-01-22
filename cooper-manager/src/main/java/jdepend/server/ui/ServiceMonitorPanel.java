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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.StatusField;
import jdepend.framework.ui.TableSorter;
import jdepend.service.ServiceFactory;
import jdepend.service.remote.JDependRemoteService;
import jdepend.service.remote.JDependRequest;
import jdepend.service.remote.JDependSession;
import jdepend.service.remote.ServiceMonitor;
import jdepend.service.remote.impl.JDependSessionMgr;

public class ServiceMonitorPanel extends JPanel implements ServiceMonitor {

	private JDependServer server;

	private JDependRemoteService service;

	private DefaultTableModel requestListModel;

	private JLabel statusLabel;

	private JButton start;

	private JButton stop;

	private boolean status;

	private final static String STOP = "未启动";
	private final static String RUN = "正在运行";

	public ServiceMonitorPanel(JDependServer server) {

		this.server = server;

		this.setLayout(new BorderLayout());

		this.add(BorderLayout.NORTH, this.createMenuPanel());

		JPanel workspace = new JPanel(new GridLayout(1, 1));

		JPanel tasks = new JPanel(new BorderLayout());

		tasks.add(BorderLayout.NORTH, new JLabel("执行的任务列表："));

		JTable requestsTable = this.initRequestList();

		tasks.add(BorderLayout.CENTER, new JScrollPane(requestsTable));

		workspace.add(tasks);

		this.add(BorderLayout.CENTER, workspace);
	}

	private JTable initRequestList() {

		requestListModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(requestListModel);

		JTable requestsTable = new JTable(sorter);

		sorter.setTableHeader(requestsTable.getTableHeader());

		requestListModel.addColumn("执行时间");
		requestListModel.addColumn("会话ID");
		requestListModel.addColumn("客户端IP");
		requestListModel.addColumn("用户名");
		requestListModel.addColumn("组名称");
		requestListModel.addColumn("命令名称");

		return requestsTable;
	}

	private void refresh() {

	}

	private JComponent createMenuPanel() {

		JPanel statusPanel = new JPanel();
		statusLabel = new JLabel();
		statusPanel.add(statusLabel);
		start = new JButton("启动");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					start();
				} catch (JDependException e1) {
					e1.printStackTrace();
					server.showStatusError(e1.getMessage());
				}
			}
		});
		statusPanel.add(start);
		stop = new JButton("停止");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					stop();
				} catch (JDependException e1) {
					e1.printStackTrace();
					server.showStatusError(e1.getMessage());
				}
			}
		});
		statusPanel.add(stop);

		changeStatusSkin();

		JPanel menuPanel = new JPanel(new BorderLayout());

		menuPanel.add(BorderLayout.CENTER, statusPanel);

		JPanel settingPanel = new JPanel();
		JButton settingButton = new JButton("设置");
		settingButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ServiceSettingDialog d = new ServiceSettingDialog(server);
				d.setModal(true);
				d.setVisible(true);
			}
		});
		settingPanel.add(settingButton);

		menuPanel.add(BorderLayout.EAST, settingPanel);

		return menuPanel;
	}

	private void changeStatusSkin() {
		if (this.status) {
			statusLabel.setText(RUN);
			start.setEnabled(false);
			stop.setEnabled(true);
			server.getStatusField().setText("服务已启动", StatusField.Center);
		} else {
			statusLabel.setText(STOP);
			start.setEnabled(true);
			stop.setEnabled(false);
			server.getStatusField().setText("服务已停止", StatusField.Center);
		}
	}

	public void onAnalyse(JDependRequest request) throws JDependException {

		JDependSession session = JDependSessionMgr.getInstance().getSession(request);

		Object[] row = new Object[6];
		row[0] = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(Calendar.getInstance().getTime());
		row[1] = request.getSessionId();
		row[2] = session.getClient();
		row[3] = session.getUserName();
		row[4] = request.getGroupName();
		row[5] = request.getCommandName();

		requestListModel.addRow(row);
	}

	public void start() throws JDependException {
		if (!this.status) {
			this.bindService();
			this.status = true;
			changeStatusSkin();
		}
	}

	public void stop() throws JDependException {
		if (this.status) {
			this.unbindService();
			this.status = false;
			changeStatusSkin();
		}
	}

	private void bindService() throws JDependException {
		try {
			this.service = ServiceFactory.createJDependRemoteService();
			this.service.addMonitor(this);
			Naming.rebind("rmi://localhost:1099/JDependRemoteService", service);
		} catch (Exception e) {
			throw new JDependException("绑定解析服务错误！", e);
		}
	}

	private void unbindService() throws JDependException {
		try {
			Naming.unbind("rmi://localhost:1099/JDependRemoteService");
		} catch (Exception e) {
			throw new JDependException("解除解析服务绑定错误！", e);
		}
	}

}
