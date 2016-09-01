package jdepend.server.service.persistent;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jdepend.framework.context.JDependContext;
import jdepend.framework.persistent.ConnectionProvider;

public final class ServerConnectionProvider implements ConnectionProvider {

	@Override
	public Connection getConnection() throws SQLException {
		String dbPath = File.separator + "knowledge" + File.separator + "db" + File.separator + "knowledge";
		return DriverManager.getConnection("jdbc:hsqldb:file:" + JDependContext.getWorkspacePath() + dbPath, "sa", "");
	}

}
