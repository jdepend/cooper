package jdepend.knowledge.database;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.framework.util.MetricsFormat;
import jdepend.framework.util.StreamUtil;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisResultSummary;

public final class AnalysisResultRepository {

	private final static String IsFoundLocalSQL = "select count(*) as num from analysissummry where groupname = ? and commandname = ? and CN = ? and CC = ? and AC = ? and Ca = ? and Ce = ? and A = ? and I = ? and D = ? and Coupling = ? and Cohesion = ? and Balance = ? and Encapsulation = ? and OO = ? and UC = ? and client is null and username is null";

	private final static String IsFoundRemoteSQL = "select count(*) as num from analysissummry where groupname = ? and commandname = ? and CN = ? and CC = ? and AC = ? and Ca = ? and Ce = ? and A = ? and I = ? and D = ? and Coupling = ? and Cohesion = ? and Balance = ? and Encapsulation = ? and OO = ? and UC = ? and client = ? and username = ?";

	private final static String CreateLocalResultSUMMRYSQL = "insert into analysissummry(id, groupname, commandname, LC, CN, CC, AC, Ca, Ce, A, I, D, Coupling, Cohesion, Balance, Encapsulation, OO, UC, createdate) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, now())";

	private final static String CreateRemoteResultSUMMRYSQL = "insert into analysissummry(id, groupname, commandname, LC, CN, CC, AC, Ca, Ce, A, I, D, Coupling, Cohesion, Balance, Encapsulation, OO, UC, client, username, createdate) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, now())";

	private final static String FindLocalSummrySQL = "select * from analysissummry where client is null and username is null and groupname = ? and commandname = ?";

	private final static String FindLocalIDSQLByGroupCommand = "select id from analysissummry where client is null and username is null and groupname = ? and commandname = ?";

	private final static String FindLocalIDSQLByGroup = "select id from analysissummry where client is null and username is null and groupname = ?";

	private final static String DeleteSummrySQL = "delete from analysissummry where id = ?";

	// ResultDataSQL

	private final static String CreateLocalResultDATASQL = "insert into analysisdata values(?, ?)";

	private final static String FindResultSQL = "select result from analysisdata where id = ?";

	private final static String DeleteAnalysisDataSQL = "delete from analysisdata where id = ?";

	public static void save(AnalysisResult result) throws JDependException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);

			AnalysisResultSummary feature = result.getSummary();

			if (result.getRunningContext().isLocalRunning()) {
				ps = conn.prepareStatement(IsFoundLocalSQL);
			} else {
				ps = conn.prepareStatement(IsFoundRemoteSQL);
			}
			ps.setString(1, result.getRunningContext().getGroup());
			ps.setString(2, result.getRunningContext().getCommand());
			ps.setInt(3, feature.getClassCount());
			ps.setInt(4, feature.getConcreteClassCount());
			ps.setInt(5, feature.getAbstractClassCount());
			ps.setInt(6, feature.getAfferentCoupling());
			ps.setInt(7, feature.getEfferentCoupling());
			ps.setFloat(8, MetricsFormat.toFormattedMetrics(feature.getAbstractness()));
			ps.setFloat(9, MetricsFormat.toFormattedMetrics(feature.getStability()));
			ps.setFloat(10, MetricsFormat.toFormattedMetrics(feature.getDistance()));
			ps.setFloat(11, MetricsFormat.toFormattedMetrics(feature.getCoupling()));
			ps.setFloat(12, MetricsFormat.toFormattedMetrics(feature.getCohesion()));
			ps.setFloat(13, MetricsFormat.toFormattedMetrics(feature.getBalance()));
			ps.setFloat(14, MetricsFormat.toFormattedMetrics(feature.getEncapsulation()));
			ps.setFloat(15, 0F);
			ps.setInt(16, feature.getComponentCount());
			if (!result.getRunningContext().isLocalRunning()) {
				ps.setString(17, result.getRunningContext().getClient());
				ps.setString(18, result.getRunningContext().getUserName());
			}

			rs = ps.executeQuery();
			rs.next();
			if (rs.getInt("num") > 0) {
				return;
			}

			String id = UUID.randomUUID().toString();
			if (result.getRunningContext().isLocalRunning()) {
				ps = conn.prepareStatement(CreateLocalResultSUMMRYSQL);
			} else {
				ps = conn.prepareStatement(CreateRemoteResultSUMMRYSQL);
			}
			ps.setString(1, id);
			ps.setString(2, result.getRunningContext().getGroup());
			ps.setString(3, result.getRunningContext().getCommand());
			ps.setInt(4, feature.getLineCount());
			ps.setInt(5, feature.getClassCount());
			ps.setInt(6, feature.getConcreteClassCount());
			ps.setInt(7, feature.getAbstractClassCount());
			ps.setInt(8, feature.getAfferentCoupling());
			ps.setInt(9, feature.getEfferentCoupling());
			ps.setFloat(10, MetricsFormat.toFormattedMetrics(feature.getAbstractness()));
			ps.setFloat(11, MetricsFormat.toFormattedMetrics(feature.getStability()));
			ps.setFloat(12, MetricsFormat.toFormattedMetrics(feature.getDistance()));
			ps.setFloat(13, MetricsFormat.toFormattedMetrics(feature.getCoupling()));
			ps.setFloat(14, MetricsFormat.toFormattedMetrics(feature.getCohesion()));
			ps.setFloat(15, MetricsFormat.toFormattedMetrics(feature.getBalance()));
			ps.setFloat(16, MetricsFormat.toFormattedMetrics(feature.getEncapsulation()));
			ps.setFloat(17, 0F);
			ps.setInt(18, feature.getComponentCount());
			if (!result.getRunningContext().isLocalRunning()) {
				ps.setString(19, result.getRunningContext().getClient());
				ps.setString(20, result.getRunningContext().getUserName());
			}

			ps.execute();

			ps = conn.prepareStatement(CreateLocalResultDATASQL);
			ps.setString(1, id);
			ps.setBytes(2, result.sequence());
			ps.execute();

			LogUtil.getInstance(AnalysisResultRepository.class).systemLog(
					"group " + result.getRunningContext().getGroup() + " command "
							+ result.getRunningContext().getCommand() + " " + result.getComponents().size()
							+ " units is saved!");

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			LogUtil.getInstance(AnalysisResultRepository.class).systemError(
					"保存[" + result.getRunningContext().getGroup() + "." + result.getRunningContext().getCommand()
							+ "]结果失败");
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

	public static AnalysisResult getResult(String id) throws JDependException {

		InputStream inputstream = null;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		Blob result;
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(FindResultSQL);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getBlob("result");
				inputstream = result.getBinaryStream();
				return AnalysisResult.create(StreamUtil.getData(inputstream));
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("读取id[" + id + "]结果失败", e);
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

	public static List<ExecuteResultSummry> getResultSummrys(String group, String command) throws JDependException {

		List<ExecuteResultSummry> rtn = new ArrayList<ExecuteResultSummry>();
		ExecuteResultSummry executeSummry;
		AnalysisResultSummary analysisSummry;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(FindLocalSummrySQL);
			ps.setString(1, group);
			ps.setString(2, command);
			rs = ps.executeQuery();
			while (rs.next()) {
				executeSummry = new ExecuteResultSummry();
				executeSummry.setId(rs.getString("id"));
				executeSummry.setCreateDate(rs.getTimestamp("createdate"));

				analysisSummry = new AnalysisResultSummary();
				analysisSummry.setAbstractness(rs.getFloat("a"));
				analysisSummry.setLineCount(rs.getInt("lc"));
				analysisSummry.setAbstractClassCount(rs.getInt("ac"));
				analysisSummry.setAfferentCoupling(rs.getInt("ca"));
				analysisSummry.setConcreteClassCount(rs.getInt("cc"));
				analysisSummry.setEfferentCoupling(rs.getInt("ce"));
				analysisSummry.setClassCount(rs.getInt("cn"));
				analysisSummry.setCohesion(MetricsFormat.toFormattedMetrics(rs.getFloat("cohesion")));
				analysisSummry.setCoupling(MetricsFormat.toFormattedMetrics(rs.getFloat("coupling")));
				analysisSummry.setBalance(MetricsFormat.toFormattedMetrics(rs.getFloat("balance")));
				analysisSummry.setEncapsulation(MetricsFormat.toFormattedMetrics(rs.getFloat("encapsulation")));
				analysisSummry.setDistance(MetricsFormat.toFormattedMetrics(rs.getFloat("d")));
				analysisSummry.setStability(MetricsFormat.toFormattedMetrics(rs.getFloat("i")));
				analysisSummry.setComponentCount(rs.getInt("uc"));

				executeSummry.setSummry(analysisSummry);

				rtn.add(executeSummry);
			}
			return rtn;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("查询[" + group + "." + command + "]结果失败", e);
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

	public static int delete(String id) throws JDependException {

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);

			ps = conn.prepareStatement(DeleteAnalysisDataSQL);
			ps.setString(1, id);
			ps.executeUpdate();

			ps = conn.prepareStatement(DeleteSummrySQL);
			ps.setString(1, id);
			ps.executeUpdate();

			conn.commit();
			return 1;
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new JDependException("删除id[" + id + "]结果失败", e);
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

	public static int deleteAll(String group, String command) throws JDependException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rtn = 0;
		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);

			List<String> deleteSqls = new ArrayList<String>();

			ps = conn.prepareStatement(FindLocalIDSQLByGroupCommand);
			ps.setString(1, group);
			ps.setString(2, command);
			rs = ps.executeQuery();
			while (rs.next()) {
				deleteSqls.add("delete from analysisdata where id = '" + rs.getString("id") + "'");
				deleteSqls.add("delete from analysissummry where id = '" + rs.getString("id") + "'");
				rtn++;
			}
			Statement st = conn.createStatement();
			for (String deleteSql : deleteSqls) {
				st.addBatch(deleteSql);
			}
			st.executeBatch();
			conn.commit();
			return rtn;
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new JDependException("删除[" + group + "." + command + "]结果失败", e);
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

	public static int deleteAll(String group) throws JDependException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rtn = 0;
		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);

			List<String> deleteSqls = new ArrayList<String>();

			ps = conn.prepareStatement(FindLocalIDSQLByGroup);
			ps.setString(1, group);
			rs = ps.executeQuery();
			while (rs.next()) {
				deleteSqls.add("delete from analysisdata where id = '" + rs.getString("id") + "'");
				deleteSqls.add("delete from analysissummry where id = '" + rs.getString("id") + "'");
				rtn++;
			}
			Statement st = conn.createStatement();
			for (String deleteSql : deleteSqls) {
				st.addBatch(deleteSql);
			}
			st.executeBatch();
			conn.commit();
			return rtn;
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new JDependException("删除[" + group + "]结果失败", e);
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
