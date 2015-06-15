package jdepend.statistics.persistent;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jdepend.framework.context.JDependContext;
import jdepend.framework.persistent.ConnectionProvider;

public final class StatisticsConnectionProvider implements ConnectionProvider {

	@Override
	public Connection getConnection() throws SQLException {
		String dbPath = File.separator + "clientdb" + File.separator + "clientdb";
		return DriverManager.getConnection("jdbc:hsqldb:file:" + JDependContext.getWorkspacePath() + dbPath, "sa", "");
	}

}
