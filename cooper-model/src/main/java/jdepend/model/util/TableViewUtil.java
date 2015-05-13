package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdepend.metadata.TableInfo;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;

public class TableViewUtil {

	public static List<TableViewInfo> view(AnalysisResult result) {
		List<TableViewInfo> tableInfos = new ArrayList<TableViewInfo>();
		TableViewInfo tableInfo;

		// 收集TableInfo
		for (JavaClassUnit javaClass : result.getClasses()) {
			for (TableInfo info : javaClass.getJavaClass().getTables()) {
				tableInfo = new TableViewInfo(info.getTableName(), info.getType(), javaClass.getName(), javaClass
						.getComponent().getName());
				if (!tableInfos.contains(tableInfo)) {
					tableInfos.add(tableInfo);
				}
			}
		}
		// 计算表名出现的次数
		int count;
		for (TableViewInfo info : tableInfos) {
			count = 0;
			for (TableViewInfo info1 : tableInfos) {
				if (info.getName().equals(info1.getName())) {
					count++;
				}
			}
			info.setCount(count);
		}
		Collections.sort(tableInfos);

		return tableInfos;

	}

}
