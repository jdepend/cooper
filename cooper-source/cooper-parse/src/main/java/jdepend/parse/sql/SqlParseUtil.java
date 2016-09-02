package jdepend.parse.sql;

import java.util.List;

import jdepend.metadata.TableInfo;

public class SqlParseUtil {
	
	private static SqlParser parser = new SqlParserSelf();
//	private static SqlParser parser = new SqlParserThird();
	
	public static boolean isSQL(String arg){
		return parser.isSQL(arg);
	}
	
	public static List<TableInfo> parserSql(String sql){
		return parser.parserSql(sql);
	}

}
