package jdepend.parse.impl;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.TableInfo;

public class TableInfoItem {

	private String name;

	private String type;

	private List<TableInfo> tableInfos = new ArrayList<TableInfo>();

	public static final String KeyType = "KeyType";
	public static final String ClassNameType = "ClassNameType";

	public TableInfoItem(String name, String type, List<TableInfo> tableInfos) {
		super();
		this.name = name;
		this.type = type;
		this.tableInfos = tableInfos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<TableInfo> getTableInfos() {
		return tableInfos;
	}

	public void setTableInfos(List<TableInfo> tableInfos) {
		this.tableInfos = tableInfos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableInfoItem other = (TableInfoItem) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
