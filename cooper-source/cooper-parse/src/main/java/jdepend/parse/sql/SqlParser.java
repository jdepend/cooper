package jdepend.parse.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.metadata.TableInfo;

public abstract class SqlParser {

	private static Collection<String> sqlKeys;

	static {
		sqlKeys = new ArrayList<String>();

		sqlKeys.add("select ");
		sqlKeys.add("insert ");
		sqlKeys.add("update ");
		sqlKeys.add("delete ");
	}

	public static boolean isSQL(String arg) {
		if (arg == null) {
			return false;
		} else {
			if (arg.length() < 7) {
				return false;
			}
			arg = arg.toLowerCase();
			for (String sqlKey : sqlKeys) {
				if (arg.indexOf(sqlKey) != -1) {
					return true;
				}
			}
			return false;
		}
	};

	public abstract List<TableInfo> parserSql(String sql);

}
