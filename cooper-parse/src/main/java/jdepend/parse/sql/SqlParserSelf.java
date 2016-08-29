package jdepend.parse.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jdepend.framework.util.StringUtil;
import jdepend.metadata.TableInfo;

public class SqlParserSelf extends SqlParser{

	private static final String TABLE_VALUE = "([^()]+)";

	public List<TableInfo> parserSql(String sql) {
		List<TableInfo> tables = new ArrayList<TableInfo>();

		String[] tableInfo = null;
		try {
			List TableInfos = parserSelectSql(sql);
			if (TableInfos.size() != 0) {
				for (int i = 0; i < TableInfos.size(); i++) {
					tableInfo = (String[]) TableInfos.get(i);
					if (tableInfo != null && tableInfo.length > 0 && !StringUtil.isEmpty(tableInfo[0])) {
						tables.add(new TableInfo(tableInfo[0], TableInfo.Read));
					}
				}
				return tables;
			}

			TableInfos = parserInsertSql(sql);
			if (TableInfos.size() != 0) {
				for (int i = 0; i < TableInfos.size(); i++) {
					tableInfo = (String[]) TableInfos.get(i);
					if (tableInfo != null && tableInfo.length > 0 && !StringUtil.isEmpty(tableInfo[0])) {
						tables.add(new TableInfo(tableInfo[0], TableInfo.Create));
					}
				}
				return tables;
			}

			TableInfos = parserUpdateSql(sql);
			if (TableInfos.size() != 0) {
				for (int i = 0; i < TableInfos.size(); i++) {
					tableInfo = (String[]) TableInfos.get(i);
					if (tableInfo != null && tableInfo.length > 0 && !StringUtil.isEmpty(tableInfo[0])) {
						tables.add(new TableInfo(tableInfo[0], TableInfo.Update));
					}
				}
				return tables;
			}

			TableInfos = parserDeleteSql(sql);
			if (TableInfos.size() != 0) {
				for (int i = 0; i < TableInfos.size(); i++) {
					tableInfo = (String[]) TableInfos.get(i);
					if (tableInfo != null && tableInfo.length > 0 && !StringUtil.isEmpty(tableInfo[0])) {
						tables.add(new TableInfo(tableInfo[0], TableInfo.Delete));
					}
				}
				return tables;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tables;
	}

	public static List parserSelectSql(String sql) {
		List result = new ArrayList();
		String metaRxp = "(?i)select ([^&]+) (?i)from " + TABLE_VALUE;
//		String metaRxp = "(?i)select ([^from]+) (?i)from " + TABLE_VALUE;
		Pattern pattern = null;
		Matcher matcher = null;
		pattern = Pattern.compile(metaRxp);
		matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String result1 = matcher.group();
			result1 = parserTable(result1, metaRxp);
			// 处理是否有where的情况
			String regx = TABLE_VALUE + " (?i)where ([^;]+)";
			Pattern pattern1 = Pattern.compile(regx);
			Matcher matcher1 = pattern1.matcher(result1);
			if (matcher1.find()) {
				String result2 = matcher1.group();
				String tableName = parserTable(result2, regx);
				result.addAll(getTableResult(tableName));
			} else {
				// 这里有两种情况。一种是标准的另一种是join的
				String regx2 = TABLE_VALUE + " (?i)join " + "([^;]+)";
				Pattern pattern2 = Pattern.compile(regx2);
				Matcher matcher2 = pattern2.matcher(result1);
				if (matcher2.find()) {
					String result3 = matcher2.group();
					String table1 = parserTable(result3, regx2);
					String table2 = result3.substring(result3.toLowerCase().indexOf("join") + "join".length(), result3
							.toLowerCase().indexOf("on"));
					result.addAll(getTableResult(table1));
					result.addAll(getTableResult(table2));
				} else {
					result.addAll(getTableResult(result1));
				}
			}
		}
		return result;
	}

	private static List parserInsertSql(String sql) {
		List result = new ArrayList();
		String metaRxp = "(?i)insert([\\s]+)into([\\s]+)" + TABLE_VALUE + "(([\\s]+)values|\\(+)";
		Pattern pattern = null;
		Matcher matcher = null;
		pattern = Pattern.compile(metaRxp);
		matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String result1 = matcher.group();
			result1 = parserTable(result1, metaRxp);
			result.addAll(getTableResult(result1));
		}
		return result;
	}

	private static List parserDeleteSql(String sql) {
		List result = new ArrayList();
		String metaRxp = "(?i)delete([\\s]+)from([\\s]+)" + TABLE_VALUE;
		Pattern pattern = null;
		Matcher matcher = null;
		pattern = Pattern.compile(metaRxp);
		matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String result1 = matcher.group();
			result1 = parserTable(result1, metaRxp);
			result.addAll(getTableResult(result1));
		}
		return result;
	}

	private static List parserUpdateSql(String sql) {
		List result = new ArrayList();
		String metaRxp = "(?i)update([\\s]+)" + TABLE_VALUE + "([\\s]+)set";
		Pattern pattern = null;
		Matcher matcher = null;
		pattern = Pattern.compile(metaRxp);
		matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String result1 = matcher.group();
			result1 = parserTable(result1, metaRxp);
			result.addAll(getTableResult(result1));
		}
		return result;
	}

	/**
	 * 将解析出来的table的表名和别名分别存储
	 * 
	 * @param table
	 * @return
	 */
	private static List getTableResult(String table) {
		List result = new ArrayList();
		String[] tempTable = table.split(",");
		for (int i = 0; i < tempTable.length; i++) {
			table = tempTable[i].trim();
			String tableResult[] = new String[2];
			String regx = "([a-zA-Z0-9_]+)([\\s]+)([a-zA-Z0-9_]+)";
			Pattern pattern1 = Pattern.compile(regx);
			Matcher matcher1 = pattern1.matcher(table);
			if (matcher1.find()) {
				String[] temp = table.split("([\\s]+)");
				if (temp.length >= 2) {
					tableResult[0] = temp[0];
					tableResult[1] = temp[1];
				}
			} else {
				tableResult[0] = table;
			}
			result.add(tableResult);
		}
		return result;
	}

	/**
	 * 通过传入符合规则的sql语句去得到当前sql的table
	 * 
	 * @param sql
	 * @param metaRxp
	 * @return
	 */
	private static String parserTable(String sql, String metaRxp) {
		if (null == metaRxp || metaRxp.length() < 2) {
			return "";
		}
		int i = metaRxp.indexOf(TABLE_VALUE);
		if (i != -1) {
			String str1 = metaRxp.substring(0, i);
			String str2 = metaRxp.substring(i + TABLE_VALUE.length());
			String regex = str1 + TABLE_VALUE + str2;
			Pattern pattern = null;
			Matcher matcher = null;
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(sql);
			while (matcher.find()) {
				String functionMethod = matcher.group();
				if (functionMethod != null) {
					functionMethod = functionMethod.replaceAll(str1, "");
					functionMethod = functionMethod.replaceAll(str2, "");
					return functionMethod;
				}
			}
		}
		return null;
	}
}
