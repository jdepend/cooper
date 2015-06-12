package jdepend.client.ui.circle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.SocketException;
import java.text.ParseException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.log.LogUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.circle.domain.DataPacket;
import jdepend.client.ui.circle.domain.IpMsgConstant;
import jdepend.client.ui.circle.domain.SocketManage;
import jdepend.client.ui.circle.domain.SystemVar;
import jdepend.client.ui.circle.domain.UsersVo;
import jdepend.client.ui.circle.gui.IUserListGui;
import jdepend.client.ui.circle.thread.DataPacketHandler;
import jdepend.client.ui.circle.thread.RecvPacketDaemon;
import jdepend.client.ui.circle.util.GuiUtil;
import jdepend.client.ui.circle.util.NetUtil;

public class CirclePanel extends JPanel implements IUserListGui {

	private JDependCooper frame;

	private JTable userListTable;

	private String currentIP;

	private boolean hidden;

	private boolean start;

	public CirclePanel(JDependCooper frame) {
		this.frame = frame;

		this.setLayout(new BorderLayout());

		this.add(BorderLayout.CENTER, initUserListTable());

		this.start = false;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
		if (!this.hidden && !this.start) {
			this.start();
			// 记录已经启动服务的状态
			this.start = true;
		}
	}

	public void start() {
		// 系统启动检查
		preCheck();
		// 初始化系统参数
		SystemVar.init();
		SystemVar.setUserListGui(this);
		// 启动后台线程
		new Thread(new RecvPacketDaemon()).start();
		// 启动协议解析线程
		new Thread(new DataPacketHandler(frame)).start();
		// 系统登录
		login();
		// 记录日志
		LogUtil.getInstance(CirclePanel.class).systemLog("启动了CircleService");

	}

	public void close() throws SocketException {
		if (this.start) {
			SocketManage.getInstance().getUdpSocket().close();
			// 记录日志
			LogUtil.getInstance(CirclePanel.class).systemLog("关闭了CircleService");
		}
	}

	/**
	 * 登录IpMsg系统
	 */
	private void login() {
		// 发送登录信息包
		DataPacket dp = new DataPacket(IpMsgConstant.IPMSG_BR_ENTRY);
		dp.setAdditional(SystemVar.USER_NAME + '\0' + "");
		dp.setIp(NetUtil.getLocalHostIp());
		UsersVo user = UsersVo.changeDataPacket(dp);
		SystemVar.addUsers(user);
		addUserVo(user);
		updateUserNum(1);
		NetUtil.broadcastUdpPacket(dp);

	}

	/**
	 * 检查程序是否满足运行条件
	 */
	private void preCheck() {
		if (!NetUtil.checkPort()) {
			GuiUtil.showNotice(this, "端口已经被占用！");
			System.exit(0);
		}
	}

	private JComponent initUserListTable() {

		userListTable = new JTable();
		userListTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "用户名", "工作组", "主机名", "IP地址" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class,
					java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});

		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem sendMessageItem = new JMenuItem("发送消息");
		sendMessageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSendMessage(currentIP, null);
			}
		});
		popupMenu.add(sendMessageItem);

		popupMenu.addSeparator();

		JMenuItem sendResultItem = new JMenuItem("发送结果");
		sendResultItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResultListDialog d = new ResultListDialog(frame, currentIP);
				d.setModal(true);
				d.setVisible(true);

			}
		});
		popupMenu.add(sendResultItem);
		JMenuItem sendCurrentResultItem = new JMenuItem("发送当前结果");
		sendCurrentResultItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnalysisResult result = JDependUnitMgr.getInstance().getResult();
				if (result == null) {
					JOptionPane.showMessageDialog(frame, "您还没有执行命令", "提示", JOptionPane.CLOSED_OPTION);
					return;
				}
				try {
					byte[] resultData = result.getBytes();
					IpMsgService.sendResult(resultData, new String[] { currentIP });
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showConfirmDialog(frame, e1.getMessage(), "错误", JOptionPane.CLOSED_OPTION);
				}

			}
		});
		popupMenu.add(sendCurrentResultItem);

		userListTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				int currentRow = table.rowAtPoint(e.getPoint());
				if (currentRow >= 0) {
					table.setRowSelectionInterval(currentRow, currentRow);
					currentIP = (String) table.getValueAt(currentRow, 3);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}

		});

		JScrollPane pane = new JScrollPane(userListTable);
		pane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					final JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem hiddenItem = new JMenuItem("隐藏");
					hiddenItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							frame.hiddenCirclePanel();
						}
					});
					popupMenu.add(hiddenItem);

					popupMenu.addSeparator();

					JMenuItem refreshItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Refresh));
					refreshItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							SystemVar.clearUsers();
							clearTable();
							login();
						}
					});
					popupMenu.add(refreshItem);

					JMenuItem addOtherItem = new JMenuItem("增加其它IP");
					addOtherItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								AddIPDialog d = new AddIPDialog(frame);
								d.setModal(true);
								d.setVisible(true);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
						}
					});
					popupMenu.add(addOtherItem);

					popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}
		});

		return pane;
	}

	public void updateUserList(List<UsersVo> userList) {
		int rowCount = userListTable.getRowCount();
		DefaultTableModel model = (DefaultTableModel) userListTable.getModel();
		for (int i = 0; i < userList.size(); i++) {
			UsersVo user = userList.get(i);
			if (i >= rowCount) // 如果表格行数不够用，增加行数
				model.addRow(user.toArray());
			else {
				model.setValueAt(user.getAlias(), i, 0);
				model.setValueAt(user.getGroupName(), i, 1);
				model.setValueAt(user.getHostName(), i, 2);
				model.setValueAt(user.getIp(), i, 3);
			}
		}
	}

	private void openSendMessage(String ip, String content) {
		SendMessageDialog sendMessageDialog = new SendMessageDialog(frame, ip, content);
		sendMessageDialog.setModal(true);
		sendMessageDialog.setVisible(true);
	}

	public void addUserVo(UsersVo user) {
		DefaultTableModel model = (DefaultTableModel) userListTable.getModel();
		model.addRow(user.toArray());
	}

	public void rebackMsg(int rowIdex, String content) {
		String ip = (String) userListTable.getValueAt(rowIdex, 3);
		openSendMessage(ip, content);
	}

	@Override
	public void updateUserNum(int num) {
		// TODO Auto-generated method stub

	}

	/**
	 * 清空JTable控件的值
	 */
	private void clearTable() {
		int rowCount = userListTable.getRowCount();
		DefaultTableModel model = (DefaultTableModel) userListTable.getModel();
		for (int i = 0; i < rowCount; i++) {
			model.removeRow(0);
		}
	}

}
