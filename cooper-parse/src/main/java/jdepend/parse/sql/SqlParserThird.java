package jdepend.parse.sql;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.log.LogUtil;
import jdepend.framework.util.StringUtil;
import jdepend.metadata.TableInfo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class SqlParserThird extends SqlParser {

	public List<TableInfo> parserSql(String sql) {
		List<TableInfo> tables = new ArrayList<TableInfo>();

		TableInfo tableInfo = null;
		Statement statement;
		List<String> tableNames = null;
		String operation = null;
		try {
			statement = CCJSqlParserUtil.parse(sql);
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();

			if (statement instanceof Select) {
				tableNames = tablesNamesFinder.getTableList((Select) statement);
				operation = TableInfo.Read;
			} else if (statement instanceof Insert) {
				tableNames = tablesNamesFinder.getTableList((Insert) statement);
				operation = TableInfo.Create;
			} else if (statement instanceof Delete) {
				tableNames = tablesNamesFinder.getTableList((Delete) statement);
				operation = TableInfo.Delete;
			} else if (statement instanceof Update) {
				tableNames = tablesNamesFinder.getTableList((Update) statement);
				operation = TableInfo.Update;
			}
			if (tableNames != null) {
				for (String tableName : tableNames) {
					if (!StringUtil.isEmpty(tableName)) {
						tableInfo = new TableInfo(tableName, operation);
						tables.add(tableInfo);
					}
				}
			}
			return tables;

		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.getInstance(SqlParserThird.class).systemError(
					"解析sql出错[" + sql + "]");

			return null;
		}

	}
}
