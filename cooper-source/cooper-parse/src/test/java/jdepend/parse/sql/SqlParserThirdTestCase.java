package jdepend.parse.sql;

import java.util.List;

import jdepend.metadata.TableInfo;
import junit.framework.TestCase;

public class SqlParserThirdTestCase extends TestCase{
	
	public void test(){
		String sql;

		// sql =
		// "select * from  tableA aa , tableD dd where select * from (select * from tableB  where select * from (select * from tableC))";
		// parseSql(sql);

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

		// sql =
		// "select m.* from share_share as m left join share_heat as mh on m.id=mh.id where m.deleted_flag = 0 order by mh.heat desc,m.last_modified_time desc limit :record_count offset :start_index";
		sql = "select m.* from share_share as m left join share_heat as mh on m.id=mh.id where m.deleted_flag = 0 order by mh.heat desc,m.last_modified_time desc";
		parseSql(sql);
	}

	private static void parseSql(String sql) {

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
