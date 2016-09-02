package jdepend.parse.sql;

import java.util.List;

import jdepend.metadata.TableInfo;
import junit.framework.TestCase;

public class SqlParserSelfTestCase extends TestCase{
	
	public void test(){
		String sql;
		SqlParserSelf engine = new SqlParserSelf();
		List tempList;
		
//		sql = "select * from  tableA aa , tableD dd where * from (select * from tableB  where * from (select * from tableC))";
//		tempList = engine.parserSelectSql(sql);
//		for (int i = 0; i < tempList.size(); i++) {
//			String[] result = (String[]) tempList.get(i);
//			System.out.println("表名 ：" + result[0]);
//			System.out.println("别名 ：" + result[1]);
//			System.out.println("==========================================");
//		}
//
		sql = "select tip, m, dd from analyzer";
		tempList = engine.parserSelectSql(sql);
		for (int i = 0; i < tempList.size(); i++) {
			String[] result = (String[]) tempList.get(i);
			System.out.println("表名 ：" + result[0]);
			System.out.println("别名 ：" + result[1]);
			System.out.println("==========================================");
		}
//
//		sql = "select classname, name, tip, bigtip, type, username, createdate from analyzer";
//		tempList = engine.parserSelectSql(sql);
//		for (int i = 0; i < tempList.size(); i++) {
//			String[] result = (String[]) tempList.get(i);
//			System.out.println("表名 ：" + result[0]);
//			System.out.println("别名 ：" + result[1]);
//			System.out.println("==========================================");
//		}
//
//		sql = "insert  into analysisdata  values(?, ?)";
//		tempList = engine.parserInsertSql(sql);
//		for (int i = 0; i < tempList.size(); i++) {
//			String[] result = (String[]) tempList.get(i);
//			System.out.println("表名 ：" + result[0]);
//			System.out.println("别名 ：" + result[1]);
//			System.out.println("==========================================");
//		}
//
//		sql = "insert into analyzer(classname, name, tip, bigtip, type, defaultdata, def, username, createdate) values(?, ?, ?, ?, ?, ?, ?, ?, now())";
//		tempList = engine.parserInsertSql(sql);
//		for (int i = 0; i < tempList.size(); i++) {
//			String[] result = (String[]) tempList.get(i);
//			System.out.println("表名 ：" + result[0]);
//			System.out.println("别名 ：" + result[1]);
//			System.out.println("==========================================");
//		}
//
//		sql = "delete  from analysisdata where id = ?";
//		tempList = engine.parserDeleteSql(sql);
//		for (int i = 0; i < tempList.size(); i++) {
//			String[] result = (String[]) tempList.get(i);
//			System.out.println("表名 ：" + result[0]);
//			System.out.println("别名 ：" + result[1]);
//			System.out.println("==========================================");
//		}

//		sql = "select m.* from share_share as m left join share_heat as mh on m.id=mh.id where m.deleted_flag = 0 order by mh.heat desc,m.last_modified_time desc limit :record_count offset :start_index";
//		engine = new SqlParserSelf();
//		tempList = engine.parserSelectSql(sql);
//		for (int i = 0; i < tempList.size(); i++) {
//			String[] result = (String[]) tempList.get(i);
//			System.out.println("表名 ：" + result[0]);
//			System.out.println("别名 ：" + result[1]);
//			System.out.println("==========================================");
//		}
		
		sql = "select c_oid,c_name,c_unitname,zwms from CthrEmployeePO";
		engine = new SqlParserSelf();
		tempList = engine.parserSelectSql(sql);
		for (int i = 0; i < tempList.size(); i++) {
			String[] result = (String[]) tempList.get(i);
			System.out.println("表名 ：" + result[0]);
			System.out.println("别名 ：" + result[1]);
			System.out.println("==========================================");
		}
	}	
}
