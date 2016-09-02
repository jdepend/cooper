package jdepend.parse.sql;

import java.util.List;

import jdepend.metadata.TableInfo;

public abstract class SqlParser {
	
	public boolean isSQL(String arg){
		if (arg == null) {
			return false;
		} else {
			arg = arg.toLowerCase();
			return arg.indexOf("select ") != -1 || arg.indexOf("insert ") != -1 || arg.indexOf("update ") != -1
					|| arg.indexOf("delete ") != -1;

		}
	};
	
	public abstract List<TableInfo> parserSql(String sql);

}
