package jdepend.server.service.analyzer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.framework.util.StreamUtil;
import jdepend.server.service.analyzer.AnalyzerInfo;
import jdepend.server.service.analyzer.AnalyzerSummaryDTO;

public final class AnalyzerRepository {

	private final static String IsFoundAnalyzerSQL = "select count(*) as num from analyzer where classname = ?";

	private final static String CreateAnalyzerSQL = "insert into analyzer(classname, name, tip, bigtip, type, defaultdata, def, username, createdate) values(?, ?, ?, ?, ?, ?, ?, ?, now())";

	private final static String FindAnalyzerAllSQL = "select classname, name, tip, bigtip, type, username, createdate from analyzer";

	private final static String FindAnalyzerByTypeSQL = "select classname, name, tip, bigtip, type, username, createdate from analyzer where type = ?";

	private final static String FindAnalyzerByClassNameSQL = "select classname, name, tip, bigtip, type, username, createdate, defaultdata, def from analyzer where classname = ?";

	private final static String DeleteAnalyzerSQL = "delete from analyzer where classname = ?";

	public void save(AnalyzerInfo info) throws JDependException {

		if (info.getClassName() == null || info.getClassName().length() == 0) {
			throw new JDependException("保存的分析器类名不能为空");
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(IsFoundAnalyzerSQL);
			ps.setString(1, info.getClassName());
			rs = ps.executeQuery();
			rs.next();
			if (rs.getInt("num") > 0) {
				throw new JDependException("保存的分析器类名[" + info.getClassName() + "]已经存在，请修改类名后保存");
			}

			ps = conn.prepareStatement(CreateAnalyzerSQL);
			ps.setString(1, info.getClassName());
			ps.setString(2, info.getName());
			ps.setString(3, info.getTip());
			ps.setString(4, info.getBigTip());
			ps.setString(5, info.getType());
			ps.setBytes(6, info.getDefaultData());
			ps.setBytes(7, info.getDef());
			ps.setString(8, info.getUserName());
			ps.execute();

			LogUtil.getInstance(AnalyzerRepository.class).systemLog(
					"用户[" + info.getUserName() + "]在IP为[" + info.getClient() + "]的机器上保存了类名为[" + info.getClassName()
							+ "]的分析器");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new JDependException("保存分析器[" + info.getClassName() + "]失败", e);
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

	public List<AnalyzerSummaryDTO> queryAll() throws JDependException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AnalyzerSummaryDTO> infos = new ArrayList<AnalyzerSummaryDTO>();
		AnalyzerInfo info;

		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(FindAnalyzerAllSQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				info = new AnalyzerInfo();
				info.setClassName(rs.getString("classname"));
				info.setName(rs.getString("name"));
				info.setTip(rs.getString("tip"));
				info.setBigTip(rs.getString("bigtip"));
				info.setType(rs.getString("type"));
				info.setUserName(rs.getString("username"));
				info.setCreateDate(rs.getTimestamp("createdate"));

				infos.add(info);
			}
			return infos;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new JDependException("查询分析器失败", e);
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

	public List<AnalyzerSummaryDTO> queryByType(String type) throws JDependException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AnalyzerSummaryDTO> infos = new ArrayList<AnalyzerSummaryDTO>();
		AnalyzerSummaryDTO info;

		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(FindAnalyzerByTypeSQL);
			ps.setString(1, type);
			rs = ps.executeQuery();
			while (rs.next()) {
				info = new AnalyzerSummaryDTO();
				info.setClassName(rs.getString("classname"));
				info.setName(rs.getString("name"));
				info.setTip(rs.getString("tip"));
				info.setBigTip(rs.getString("bigtip"));
				info.setType(rs.getString("type"));
				info.setUserName(rs.getString("username"));
				info.setCreateDate(rs.getTimestamp("createdate"));

				infos.add(info);
			}
			return infos;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new JDependException("查询分析器失败", e);
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

	public AnalyzerInfo getTheAnalyzer(String className) throws JDependException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		InputStream inputstream = null;
		Blob result;
		byte[] data;

		AnalyzerInfo info = new AnalyzerInfo();
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(FindAnalyzerByClassNameSQL);
			ps.setString(1, className);
			rs = ps.executeQuery();
			if (rs.next()) {
				info = new AnalyzerInfo();
				info.setClassName(rs.getString("classname"));
				info.setName(rs.getString("name"));
				info.setTip(rs.getString("tip"));
				info.setBigTip(rs.getString("bigtip"));
				info.setType(rs.getString("type"));
				info.setUserName(rs.getString("username"));
				info.setCreateDate(rs.getTimestamp("createdate"));

				result = rs.getBlob("defaultdata");
				inputstream = result.getBinaryStream();
				data = StreamUtil.getData(inputstream);
				inputstream.close();
				info.setDefaultData(data);

				result = rs.getBlob("def");
				inputstream = result.getBinaryStream();
				data = StreamUtil.getData(inputstream);
				inputstream.close();
				info.setDef(data);
			}
			return info;

		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("查询分析器失败", e);
		} finally {
			if (inputstream != null) {
				try {
					inputstream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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

	public void delete(String className) throws JDependException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionFactory.getConnection();
			ps = conn.prepareStatement(DeleteAnalyzerSQL);
			ps.setString(1, className);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("删除className[" + className + "]的分析器", e);
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
