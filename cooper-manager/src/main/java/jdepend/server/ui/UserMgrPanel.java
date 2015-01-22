package jdepend.server.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.Naming;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.service.ServiceFactory;
import jdepend.service.remote.user.User;
import jdepend.service.remote.user.UserDomainService;
import jdepend.service.remote.user.UserRemoteService;
import jdepend.service.remote.user.UserStateChangeListener;

public final class UserMgrPanel extends JPanel implements UserStateChangeListener {

	private JDependServer server;

	private DefaultTableModel model;

	private String currentUserName;

	private UserRemoteService userRemoteService;
	
	private UserDomainService userDomainService;

	private int stateChangeCount = 0;

	private final static int stateChangeNum = 50;

	public UserMgrPanel(final JDependServer server) {

		this.server = server;

		this.setLayout(new BorderLayout());

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem viewUserActionItem = new JMenuItem("查看用户操作");
		viewUserActionItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					viewUserActions();
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showConfirmDialog(server, e1.getMessage(), "提示", JOptionPane.CLOSED_OPTION);
				}
			}
		});
		popupMenu.add(viewUserActionItem);

		JTable table = new JTable(model);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				int currentRow = table.rowAtPoint(e.getPoint());
				if (currentRow >= 0) {
					table.setRowSelectionInterval(currentRow, currentRow);
					currentUserName = (String) table.getValueAt(currentRow, 0);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					JTable table = (JTable) e.getSource();
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});

		model.addColumn("姓名");
		model.addColumn("部门");
		model.addColumn("积分");
		model.addColumn("是否有效");

		final JScrollPane pane = new JScrollPane(table);

		this.add(BorderLayout.CENTER, pane);

		JPanel buttonBar = new JPanel();
		buttonBar.setLayout(new FlowLayout());

		JButton refreshButton = new JButton(BundleUtil.getString(BundleUtil.Command_Refresh));
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(UserMgrPanel.this.server, e1.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonBar.add(refreshButton);

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	private void viewUserActions() throws JDependException {
		UserActionListDialog d = new UserActionListDialog(currentUserName);
		d.setModal(true);
		d.setVisible(true);

	}

	private void refresh() throws JDependException {

		model.setRowCount(0);

		Object[] row;
		for (User user : userDomainService.findUsers()) {
			row = new Object[4];
			row[0] = user.getName();
			row[1] = user.getDept();
			row[2] = user.getIntegral();
			row[3] = user.isValid() ? "有效" : "无效";

			model.addRow(row);
		}
		stateChangeCount = 0;
	}

	public void bindService() throws JDependException {
		try {
			this.userRemoteService = ServiceFactory.createUserRemoteService();
			this.userRemoteService.setUserStateChangeListener(this);
			Naming.rebind("rmi://localhost:1099/UserRemoteService", userRemoteService);
		} catch (Exception e) {
			throw new JDependException("绑定用户服务错误！", e);
		}
	}

	public void unbindService() throws JDependException {
		try {
			Naming.unbind("rmi://localhost:1099/UserRemoteService");
		} catch (Exception e) {
			throw new JDependException("解除用户服务绑定错误！", e);
		}
	}

	@Override
	public void onChange(User user) {
		stateChangeCount++;
		if (stateChangeCount >= stateChangeNum) {
			try {
				this.refresh();
			} catch (JDependException e) {
				e.printStackTrace();
			}
		}
	}
}
