package jdepend.core.persistent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jdepend.framework.context.JDependContext;
import jdepend.framework.persistent.ConnectionProvider;

public final class ClientConnectionProvider implements ConnectionProvider {

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:file:" + JDependContext.getWorkspacePath()
				+ "/clientdb/clientdb", "sa", "");
	}

}
