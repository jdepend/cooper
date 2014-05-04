package jdepend.service.remote.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.persistent.ConnectionFactory;

public final class UserRepository {

	private static final String FindAll = "select name, dept, integral, valid from user";

	private static final String FindTheUser = "select name, password, dept, integral from user where name = ? and valid='Y'";

	private static final String UpdateTheUser = "update user set dept = ?, integral = ?, valid = ? where name = ?";

	private static final String InsertTheUser = "insert into user(name, dept, integral, valid) values(?, ?, ?, 'Y')";

	public UserRepository() {
		super();
	}

	public static List<User> findUsers() throws JDependException {

		List<User> users = new ArrayList<User>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(FindAll);
			rs = ps.executeQuery();
			while (rs.next()) {
				users.add(new UserImpl(rs.getString("name"), rs.getString("dept"), (Integer) rs.getObject("integral"),
						"Y".equals(rs.getString("valid"))));
			}
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("读取注册用户列表失败", e);
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

	public static User findTheUser(String name, String password) throws JDependException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(FindTheUser);
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (validate(rs.getString("password"), password)) {
					return new UserImpl(rs.getString("name"), rs.getString("dept"), (Integer) rs.getObject("integral"));
				} else {
					throw new JDependException("用户[" + name + "]口令错误");
				}
			}
			throw new JDependException("用户[" + name + "]不存在");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JDependException("读取用户[" + name + "]失败", e);
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

	public static User findTheUser(String name) throws JDependException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(FindTheUser);
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				return new UserImpl(rs.getString("name"), rs.getString("dept"), (Integer) rs.getObject("integral"));
			}
			throw new JDependException("用户[" + name + "]不存在");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JDependException("读取用户[" + name + "]失败", e);
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

	public static void save(User user) throws JDependException {
		boolean isUpdate;
		if (findTheUser(user.getName()) == null) {
			isUpdate = false;
		} else {
			isUpdate = true;
		}
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionFactory.getConnection();
			if (isUpdate) {
				ps = conn.prepareStatement(UpdateTheUser);
				ps.setString(1, user.getDept());
				ps.setObject(2, user.getIntegral());
				ps.setString(3, user.isValid() ? "Y" : "N");
				ps.setString(4, user.getName());
			} else {
				ps = conn.prepareStatement(InsertTheUser);
				ps.setString(1, user.getName());
				ps.setString(2, user.getDept());
				ps.setObject(3, user.getIntegral());
				ps.setString(4, user.isValid() ? "Y" : "N");
			}
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("保存用户信息失败", e);
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

	private static boolean validate(String password, String inputPassword) {
		if ((password == null || password.length() == 0) && (inputPassword == null || inputPassword.length() == 0)) {
			return true;
		}
		return inputPassword.equals(password);
	}
}
