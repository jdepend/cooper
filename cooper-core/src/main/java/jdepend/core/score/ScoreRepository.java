package jdepend.core.score;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
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
import jdepend.framework.log.LogUtil;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.framework.util.StreamUtil;
import jdepend.model.result.AnalysisResult;

final class ScoreRepository {

	private static String insertSql = "insert into score(id, groupname, commandname, LC, componentCount, relationCount, Score, Distance, Balance, Encapsulation, Relation, cohesion, coupling, createdate) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

	private static String findSql = "select id from score where groupname = ? and commandname = ?";

	private static String findAllSql = "select * from score";

	private static String findSqlFromBeginDate = "select * from score where createdate >= ?";

	private static String findTheSql = "select * from score where id = ?";

	private static String deleteSql = "delete from score where id = ?";

	private final static String CreateScoreDataSQL = "insert into scoredata values(?, ?)";

	private final static String FindScoreDataSQL = "select result from scoredata where id = ?";

	private final static String FindAllScoreDataSQL = "select id, result from scoredata";

	private final static String DeleteScoreDataSQL = "delete from scoredata where id = ?";

	private final static String insertScoreExtSQL = "insert into scoreext(id, scoreid, itemname, score) values(?, ?, ?, ?)";

	private final static String deleteScoreExtSQL = "delete from scoreext where scoreid = ?";

	private static String findScoreExtSql = "select itemname, score from scoreext where scoreid = ?";

	public static void save(ScoreInfo score) throws JDependException {

		if (score.group == null || score.command == null) {
			throw new JDependException("组名或命令名不能为空。");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(findSql);
			ps.setString(1, score.group);
			ps.setString(2, score.command);
			rs = ps.executeQuery();
			if (rs.next()) {
				String id = rs.getString("id");
				// 删除分数主表
				ps = conn.prepareStatement(deleteSql);
				ps.setString(1, id);
				ps.executeUpdate();
				// 删除分数扩展表
				ps = conn.prepareStatement(deleteScoreExtSQL);
				ps.setString(1, id);
				ps.executeUpdate();
			}
			ps = conn.prepareStatement(insertSql);
			String id = assemblePreparedStatement(ps, score);
			ps.execute();

			conn.commit();
			LogUtil.getInstance(ScoreRepository.class).systemLog("保存了[" + score.group + "." + score.command + "]的分数");

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new JDependException("保存[" + score.group + "." + score.command + "]的分数失败", e);
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

	public static void save(ScoreInfo score, AnalysisResult result) throws JDependException {

		if (score.group == null || score.command == null) {
			throw new JDependException("组名或命令名不能为空。");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		byte[] data = null;

		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(findSql);
			ps.setString(1, score.group);
			ps.setString(2, score.command);
			rs = ps.executeQuery();
			if (rs.next()) {
				String id = rs.getString("id");
				// 删除分数主表
				ps = conn.prepareStatement(deleteSql);
				ps.setString(1, id);
				ps.executeUpdate();
				// 删除分数数据
				ps = conn.prepareStatement(DeleteScoreDataSQL);
				ps.setString(1, id);
				ps.executeUpdate();
				// 删除分数扩展表
				ps = conn.prepareStatement(deleteScoreExtSQL);
				ps.setString(1, id);
				ps.executeUpdate();
			}

			ps = conn.prepareStatement(insertSql);
			String id = assemblePreparedStatement(ps, score);
			ps.execute();

			data = result.sequence();
			ps = conn.prepareStatement(CreateScoreDataSQL);
			ps.setString(1, id);
			ps.setBytes(2, data);
			ps.execute();

			conn.commit();
			LogUtil.getInstance(ScoreRepository.class)
					.systemLog("保存了[" + score.group + "." + score.command + "]的分数和结果");

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new JDependException("保存[" + score.group + "." + score.command + "]的分数失败", e);
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

	public static List<ScoreInfo> getScoreList() throws JDependException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String itemname;

		List<ScoreInfo> scoreList = new ArrayList<ScoreInfo>();
		ScoreInfo score;
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(findAllSql);
			rs = ps.executeQuery();
			while (rs.next()) {
				score = getScoreInfo(rs);

				ps1 = conn.prepareStatement(findScoreExtSql);
				ps1.setString(1, score.id);
				rs1 = ps1.executeQuery();
				while (rs1.next()) {
					itemname = rs1.getString("itemname");
					// if (itemname.equals(Encapsulation)) {
					// score.encapsulation = rs1.getFloat("score");
					// }
				}

				scoreList.add(score);
			}
			return scoreList;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new JDependException(e);
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

	public static List<ScoreInfo> getScoreList(Date begin) throws JDependException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String itemname;

		List<ScoreInfo> scoreList = new ArrayList<ScoreInfo>();
		ScoreInfo score;
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(findSqlFromBeginDate);
			ps.setTimestamp(1, new Timestamp(begin.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				score = getScoreInfo(rs);

				ps1 = conn.prepareStatement(findScoreExtSql);
				ps1.setString(1, score.id);
				rs1 = ps1.executeQuery();
				while (rs1.next()) {
					itemname = rs1.getString("itemname");
					// if (itemname.equals(Encapsulation)) {
					// score.encapsulation = rs1.getFloat("score");
					// }
				}

				scoreList.add(score);
			}
			return scoreList;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new JDependException(e);
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

	public static AnalysisResult getTheResult(ScoreInfo scoreInfo) throws JDependException {
		InputStream inputstream = null;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		Blob result;
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(FindScoreDataSQL);
			ps.setString(1, scoreInfo.id);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getBlob("result");
				inputstream = result.getBinaryStream();
				return AnalysisResult.create(StreamUtil.getData(inputstream));
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("读取id[" + scoreInfo.id + "]group=[" + scoreInfo.group + "]command=["
					+ scoreInfo.command + "]分数结果失败", e);
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

	private static List<AnalysisResult> getAllResult() throws JDependException {
		InputStream inputstream = null;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String id;
		Blob result;
		List<AnalysisResult> results = new ArrayList<AnalysisResult>();
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(FindAllScoreDataSQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getString("id");
				result = rs.getBlob("result");
				inputstream = result.getBinaryStream();
				try {
					results.add(AnalysisResult.create(StreamUtil.getData(inputstream)));
				} catch (IOException e) {
					e.printStackTrace();
					if (id != null)
						LogUtil.getInstance(ScoreRepository.class).systemError("读取ID[" + id + "]分数结果失败");
				}
			}
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("读取分数结果失败", e);
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

	public static ScoreInfo getTheScoreInfo(String id) throws JDependException {
		if (id == null) {
			throw new JDependException("ID不能为空。");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(findTheSql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				return getScoreInfo(rs);
			} else {
				throw new JDependException("id=[" + id + "]没有对应的分数信息");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new JDependException(e);
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

	public static void delete(String id) throws JDependException {

		if (id == null) {
			throw new JDependException("ID不能为空。");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			// 删除分数主表
			ps = conn.prepareStatement(deleteSql);
			ps.setString(1, id);
			ps.executeUpdate();
			// 删除分数数据
			ps = conn.prepareStatement(DeleteScoreDataSQL);
			ps.setString(1, id);
			ps.executeUpdate();
			// 删除分数扩展表
			ps = conn.prepareStatement(deleteScoreExtSQL);
			ps.setString(1, id);
			ps.executeUpdate();

			conn.commit();
			LogUtil.getInstance(ScoreRepository.class).systemLog("删除了ID为[" + id + "]的分数");
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new JDependException("删除ID为[" + id + "]的分数失败", e);
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

	private static ScoreInfo getScoreInfo(ResultSet rs) throws SQLException {
		ScoreInfo score = new ScoreInfo();
		score.id = rs.getString("id");
		score.group = rs.getString("groupname");
		score.command = rs.getString("commandname");
		score.lc = rs.getInt("lc");
		score.componentCount = rs.getInt("componentCount");
		score.relationCount = rs.getInt("relationCount");
		score.score = rs.getFloat("score");
		score.distance = rs.getFloat("distance");
		score.balance = rs.getFloat("balance");
		score.encapsulation = rs.getFloat("encapsulation");
		score.relation = rs.getFloat("relation");
		score.cohesion = rs.getFloat("cohesion");
		score.coupling = rs.getFloat("coupling");
		score.createDate = rs.getTimestamp("createDate");

		return score;
	}

	private static String assemblePreparedStatement(PreparedStatement ps, ScoreInfo score) throws SQLException {

		String id = UUID.randomUUID().toString();
		ps.setString(1, id);
		ps.setString(2, score.group);
		ps.setString(3, score.command);
		ps.setInt(4, score.lc);
		ps.setInt(5, score.componentCount);
		ps.setInt(6, score.relationCount);
		ps.setFloat(7, score.score);
		ps.setFloat(8, score.distance);
		ps.setFloat(9, score.balance);
		ps.setFloat(10, score.encapsulation);
		ps.setFloat(11, score.relation);
		ps.setFloat(12, score.cohesion);
		ps.setFloat(13, score.coupling);

		return id;
	}
}
