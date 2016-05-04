package jdepend.parse.sql;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.log.LogUtil;
import jdepend.metadata.TableInfo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class SqlParserThird extends SqlParser{

	public List<TableInfo> parserSql(String sql) {
		List<TableInfo> tables = new ArrayList<TableInfo>();

		TableInfo tableInfo = null;
		Statement statement;
		List<String> tableNames = null; 
		String operation = null;
		try {
			statement = CCJSqlParserUtil.parse(sql);
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			
			if(statement instanceof Select){
				tableNames = tablesNamesFinder.getTableList((Select)statement);
				operation = TableInfo.Read;
			}else if(statement instanceof Insert){
				tableNames = tablesNamesFinder.getTableList((Insert)statement);
				operation = TableInfo.Create;
			}else if(statement instanceof Delete){
				tableNames = tablesNamesFinder.getTableList((Delete)statement);
				operation = TableInfo.Delete;
			}else if(statement instanceof Update){
				tableNames = tablesNamesFinder.getTableList((Update)statement);
				operation = TableInfo.Update;
			}
		    if(tableNames != null){
	        for(String tableName : tableNames){
	        	tableInfo = new TableInfo(tableName, operation);
	        	tables.add(tableInfo);
	        }
		    }
	        return tables;
	        
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.getInstance(SqlParserThird.class).systemError("解析sql出错[" + sql + "]");
			
			return null;
		}
	        
		
	}

	

	public static void main(String[] args) {
		String sql;
		
//		sql = "select * from  tableA aa , tableD dd where select * from (select * from tableB  where select * from (select * from tableC))";
//		parseSql(sql);
		
		sql = "select tip, m, dd from analyzer";
		parseSql(sql);

		sql = "select classname, name, tip, bigtip, type, username, createdate from analyzer";
		parseSql(sql);
		
		sql = "insert  into analysisdata  values(?, ?)";
		parseSql(sql);
		
		sql = "insert into analyzer(classname, name, tip, bigtip, type, defaultdata, def, username, createdate) values(?, ?, ?, ?, ?, ?, ?, ?, now())";
		parseSql(sql);

		sql = "delete  from analysisdata where id = ?";
		parseSql(sql);

		//sql = "select m.* from share_share as m left join share_heat as mh on m.id=mh.id where m.deleted_flag = 0 order by mh.heat desc,m.last_modified_time desc limit :record_count offset :start_index";
		sql = "select m.* from share_share as m left join share_heat as mh on m.id=mh.id where m.deleted_flag = 0 order by mh.heat desc,m.last_modified_time desc";
		parseSql(sql);
	}
	
	private static void parseSql(String sql){
		
		SqlParserThird engine = new SqlParserThird();
		List tempList;
		
		engine = new SqlParserThird();
		tempList = engine.parserSql(sql);
		for (int i = 0; i < tempList.size(); i++) {
			TableInfo result = (TableInfo) tempList.get(i);
			System.out.println("表名 ：" + result.getTableName());
			System.out.println("操作 ：" + result.getType());
			System.out.println("==========================================");
		}
	}
}
