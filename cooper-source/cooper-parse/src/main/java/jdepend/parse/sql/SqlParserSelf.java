package jdepend.parse.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jdepend.metadata.TableInfo;

public class SqlParserSelf extends SqlParser {

	private static final String TABLE_VALUE = "([^()]+)";

	public List<TableInfo> parserSql(String sql) {
		try {
			List<TableInfo> TableInfos = parserSelectSql(sql);
			if (TableInfos.size() != 0) {
				return TableInfos;
			}

			TableInfos = parserInsertSql(sql);
			if (TableInfos.size() != 0) {
				return TableInfos;
			}

			TableInfos = parserUpdateSql(sql);
			if (TableInfos.size() != 0) {
				return TableInfos;
			}

			TableInfos = parserDeleteSql(sql);
			if (TableInfos.size() != 0) {
				return TableInfos;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<TableInfo>();
	}

	public static List<TableInfo> parserSelectSql(String sql) {
		List<TableInfo> result = new ArrayList<TableInfo>();
		String metaRxp = "(?i)select ([^&]+) (?i)from " + TABLE_VALUE;
		// String metaRxp = "(?i)select ([^from]+) (?i)from " + TABLE_VALUE;
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
				result.addAll(getTableResult(tableName, TableInfo.Read));
			} else {
				// 这里有两种情况。一种是标准的另一种是join的
				String regx2 = TABLE_VALUE + " (?i)join " + "([^;]+)";
				Pattern pattern2 = Pattern.compile(regx2);
				Matcher matcher2 = pattern2.matcher(result1);
				if (matcher2.find()) {
					String result3 = matcher2.group();
					String table1 = parserTable(result3, regx2);
					result.addAll(getTableResult(table1, TableInfo.Read));

					String result3x = result3.toLowerCase();
					int start = result3x.indexOf("join") + 4;
					String table2 = result3.substring(start, result3x.indexOf("on", start));
					result.addAll(getTableResult(table2, TableInfo.Read));
				} else {
					result.addAll(getTableResult(result1, TableInfo.Read));
				}
			}
		}
		return result;
	}

	private static List<TableInfo> parserInsertSql(String sql) {
		List<TableInfo> result = new ArrayList<TableInfo>();
		String metaRxp = "(?i)insert([\\s]+)into([\\s]+)" + TABLE_VALUE + "(([\\s]+)values|\\(+)";
		Pattern pattern = null;
		Matcher matcher = null;
		pattern = Pattern.compile(metaRxp);
		matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String result1 = matcher.group();
			result1 = parserTable(result1, metaRxp);
			result.addAll(getTableResult(result1, TableInfo.Create));
		}
		return result;
	}

	private static List<TableInfo> parserDeleteSql(String sql) {
		List<TableInfo> result = new ArrayList<TableInfo>();
		String metaRxp = "(?i)delete([\\s]+)from([\\s]+)" + TABLE_VALUE;
		Pattern pattern = null;
		Matcher matcher = null;
		pattern = Pattern.compile(metaRxp);
		matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String result1 = matcher.group();
			result1 = parserTable(result1, metaRxp);
			result.addAll(getTableResult(result1, TableInfo.Delete));
		}
		return result;
	}

	private static List<TableInfo> parserUpdateSql(String sql) {
		List<TableInfo> result = new ArrayList<TableInfo>();
		String metaRxp = "(?i)update([\\s]+)" + TABLE_VALUE + "([\\s]+)set";
		Pattern pattern = null;
		Matcher matcher = null;
		pattern = Pattern.compile(metaRxp);
		matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String result1 = matcher.group();
			result1 = parserTable(result1, metaRxp);
			result.addAll(getTableResult(result1, TableInfo.Update));
		}
		return result;
	}

	/**
	 * 将解析出来的table的表名和别名分别存储
	 * 
	 * @param table
	 * @return
	 */
	private static List<TableInfo> getTableResult(String table, String operarion) {
		List<TableInfo> result = new ArrayList<TableInfo>();
		String tableResult;
		String[] tempTable = table.split(",");
		for (int i = 0; i < tempTable.length; i++) {
			tableResult = null;
			table = tempTable[i].trim();
			String regx = "([a-zA-Z0-9_]+)([\\s]+)([a-zA-Z0-9_]+)";
			Pattern pattern1 = Pattern.compile(regx);
			Matcher matcher1 = pattern1.matcher(table);
			if (matcher1.find()) {
				String[] temp = table.split("([\\s]+)");
				if (temp.length >= 2) {
					tableResult = temp[0];
				}
			} else {
				tableResult = table;
			}
			if (tableResult != null && tableResult.length() > 0) {
				result.add(new TableInfo(tableResult, operarion));
			}
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
