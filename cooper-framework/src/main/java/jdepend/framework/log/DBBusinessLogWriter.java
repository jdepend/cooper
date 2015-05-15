package jdepend.framework.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jdepend.framework.exception.JDependException;
import jdepend.framework.persistent.ConnectionFactory;

public final class DBBusinessLogWriter extends AbstractBusinessLogWriter {

	@Override
	protected String doBusinessLog(String userName, Operation operation) throws JDependException {
		return saveLog(userName, operation);
	}

	public static List<BusiLogItem> getAllLogItems() throws JDependException {
		return getAllLogItems(false);
	}

	/**
	 * 
	 * @param limit
	 *            是否限制条数
	 * @return
	 * @throws JDependException
	 */
	public static List<BusiLogItem> getAllLogItems(boolean limit) throws JDependException {
		String sql;
		if (limit) {
			sql = "select id, username, operation, createdate from busilog order by createdate desc limit 20";
		} else {
			sql = "select id, username, operation, createdate from busilog order by createdate desc";
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusiLogItem> items = new ArrayList<BusiLogItem>();
		BusiLogItem item;

		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				item = new BusiLogItem();
				item.username = rs.getString("username");
				item.operation = rs.getString("operation");
				item.createdate = rs.getTimestamp("createdate");

				items.add(item);
			}
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("查询业务日志信息失败", e);
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

	public static List<BusiLogItem> getLogItems(Date begin) throws JDependException {
		String sql = "select id, username, operation, createdate from  busilog where createdate >= ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusiLogItem> items = new ArrayList<BusiLogItem>();
		BusiLogItem item;

		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setTimestamp(1, new Timestamp(begin.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				item = new BusiLogItem();
				item.username = rs.getString("username");
				item.operation = rs.getString("operation");
				item.createdate = rs.getTimestamp("createdate");

				items.add(item);
			}
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("查询业务日志信息失败", e);
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

	private static String saveLog(String userName, Operation operation) throws JDependException {
		String sql = "insert into busilog(id, username, operation, createdate) values (?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement ps = null;
		String id;
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(sql);
			id = UUID.randomUUID().toString();
			ps.setString(1, id);
			ps.setObject(2, userName);
			ps.setString(3, operation.name());
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

			ps.execute();

			return id;
		} catch (Exception e) {
			e.printStackTrace();
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

	public static void deleteAll() throws JDependException {
		String sql = "delete from busilog";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(sql);

			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("删除业务日志信息失败", e);
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
}
