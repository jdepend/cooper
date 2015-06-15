package jdepend.server.service.score;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.service.remote.score.ScoreDTO;

public final class ScoreListRepository {

	private static String insertSql = "insert into scorelist(id, ip, username, groupname, commandname, LC, componentCount, relationCount, Score, Distance, Balance, Encapsulation, Relation, cohesion, coupling, createdate, uploaddate) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

	private static String findAllSql = "select * from scorelist";

	private static String deleteSql = "delete from scorelist where id = ?";

	public void save(List<ScoreDTO> scores) throws JDependException {

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			for (ScoreDTO score : scores) {
				ps = conn.prepareStatement(insertSql);
				ps.setString(1, UUID.randomUUID().toString());
				ps.setString(2, score.ip);
				ps.setString(3, score.user);
				ps.setString(4, score.group);
				ps.setString(5, score.command);
				ps.setInt(6, score.lc);
				ps.setInt(7, score.componentCount);
				ps.setInt(8, score.relationCount);
				ps.setFloat(9, score.score);
				ps.setFloat(10, score.d);
				ps.setFloat(11, score.balance);
				ps.setFloat(12, score.encapsulation);
				ps.setFloat(13, score.relation);
				ps.setFloat(14, score.cohesion);
				ps.setFloat(15, score.coupling);
				ps.setTimestamp(16, new java.sql.Timestamp(score.createDate.getTime()));

				ps.execute();
			}

			conn.commit();
			LogUtil.getInstance(ScoreListRepository.class).systemLog("收集了" + scores.size() + "条分数信息");
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new JDependException("保存分数失败", e);
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

	public static List<ScoreDTO> getList() throws JDependException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ScoreDTO> scoreList = new ArrayList<ScoreDTO>();
		ScoreDTO score;
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(findAllSql);
			rs = ps.executeQuery();
			while (rs.next()) {
				score = new ScoreDTO();
				score.id = rs.getString("id");
				score.ip = rs.getString("ip");
				score.user = rs.getString("username");
				score.group = rs.getString("groupname");
				score.command = rs.getString("commandname");
				score.lc = rs.getInt("lc");
				score.componentCount = rs.getInt("componentCount");
				score.relationCount = rs.getInt("relationCount");
				score.score = rs.getFloat("score");
				score.d = rs.getFloat("d");
				score.balance = rs.getFloat("balance");
				score.encapsulation = rs.getFloat("encapsulation");
				score.relation = rs.getFloat("relation");
				score.cohesion = rs.getFloat("cohesion");
				score.coupling = rs.getFloat("coupling");
				score.createDate = rs.getTimestamp("createDate");
				score.uploadDate = rs.getTimestamp("uploadDate");

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

	public static void delete(String id) throws JDependException {
		if (id == null) {
			throw new JDependException("ID不能为空。");
		}

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(deleteSql);
			ps.setString(1, id);
			ps.executeUpdate();

			LogUtil.getInstance(ScoreListRepository.class).systemLog("删除了ID为[" + id + "]的分数");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JDependException("删除ID为[" + id + "]的分数失败", e);
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
