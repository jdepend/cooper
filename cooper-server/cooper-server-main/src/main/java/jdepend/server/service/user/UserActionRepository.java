package jdepend.server.service.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jdepend.framework.exception.JDependException;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.server.service.user.UserActionItem;

public final class UserActionRepository {

	public static void insertUserActions(List<UserActionItem> items) throws JDependException {
		String sql = "insert into useraction(id, username,  operation, ip, createdate, gartherdate) values (?, ?, ?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for (UserActionItem item : items) {
				ps.setString(1, UUID.randomUUID().toString());
				ps.setString(2, item.username);
				ps.setString(3, item.operation);
				ps.setString(4, item.ip);
				ps.setTimestamp(5, new Timestamp(item.createdate.getTime()));
				ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

				ps.execute();
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new JDependException("保存业务日志信息失败", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static List<UserActionItem> getAllUserActions() throws JDependException {
		String sql = "select username,  operation, ip, createdate, gartherdate from useraction";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		UserActionItem item;
		List<UserActionItem> items = new ArrayList<UserActionItem>();
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				item = new UserActionItem();
				item.username = rs.getString("username");
				item.ip = rs.getString("ip");
				item.operation = rs.getString("operation");
				item.createdate = rs.getTimestamp("createdate");
				item.gartherdate = rs.getTimestamp("gartherdate");

				items.add(item);
			}
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new JDependException("查询业务日志信息失败", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static List<UserActionItem> getTheUserActions(String username) throws JDependException {
		String sql = "select username,  operation, ip, createdate, gartherdate from useraction where username = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		UserActionItem item;
		List<UserActionItem> items = new ArrayList<UserActionItem>();
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);

			rs = ps.executeQuery();

			while (rs.next()) {
				item = new UserActionItem();
				item.username = rs.getString("username");
				item.ip = rs.getString("ip");
				item.operation = rs.getString("operation");
				item.createdate = rs.getTimestamp("createdate");
				item.gartherdate = rs.getTimestamp("gartherdate");

				items.add(item);
			}
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new JDependException("查询业务日志信息失败", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
