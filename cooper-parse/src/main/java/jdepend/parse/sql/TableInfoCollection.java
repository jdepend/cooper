package jdepend.parse.sql;

import java.util.ArrayList;
import java.util.List;

import jdepend.metadata.TableInfo;

public final class TableInfoCollection {

	private List<TableInfoItem> tableInfos = new ArrayList<TableInfoItem>();

	public void addItem(String name, String type, List<TableInfo> tableInfos) {
		TableInfoItem item = new TableInfoItem(name, type, tableInfos);
		if (!tableInfos.contains(item)) {
			this.tableInfos.add(item);
		}
	}

	public List<TableInfoItem> getTableInfos() {
		return tableInfos;
	}

}
