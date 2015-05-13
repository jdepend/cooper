package jdepend.metadata;

import java.io.Serializable;

public final class TableInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4931159558391933510L;

	private String tableName;

	private String type;

	public static final String Delete = "Delete";

	public static final String Read = "Read";

	public static final String Update = "Update";

	public static final String Create = "Create";

	public static final String Define = "Define";// 当前Class是一个Po

	public TableInfo(String tableName, String type) {
		super();
		this.tableName = tableName.toUpperCase();
		this.type = type;
	}

	public TableInfo(TableInfo info) {
		this(info.tableName, info.type);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isWrite() {
		return !this.isRead() && !this.isDefine();
	}

	public boolean isRead() {
		return this.type.equals(Read);
	}

	public boolean isDefine() {
		return this.type.equals(Define);
	}

	@Override
	public String toString() {
		return "TableRelationInfo [tableName=" + tableName + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		TableInfo other = (TableInfo) obj;
		if (!tableName.equalsIgnoreCase(other.tableName))
			return false;
		if (!type.equals(other.type))
			return false;
		return true;
	}

}
