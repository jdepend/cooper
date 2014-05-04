package jdepend.framework.persistent;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionFactory {

	private static ConnectionProvider provider;

	public static Connection getConnection() throws SQLException {
		return provider.getConnection();
	}

	public static void setProvider(ConnectionProvider provider) {
		ConnectionFactory.provider = provider;
	}
}
