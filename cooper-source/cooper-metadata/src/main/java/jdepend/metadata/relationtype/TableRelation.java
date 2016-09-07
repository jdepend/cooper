package jdepend.metadata.relationtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassDetail;
import jdepend.metadata.TableInfo;
import jdepend.metadata.util.JavaClassCollection;

public class TableRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4281750498692868958L;

	private String tableName;

	public TableRelation(float intensity) {
		super(JavaClassRelationTypes.Table, intensity);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public TableRelation clone(String tableName) {
		TableRelation tableRelation = new TableRelation(this.getIntensity());
		tableRelation.setTableName(tableName);
		return tableRelation;
	}

	@Override
	public boolean canAbstraction() {
		return false;
	}

	@Override
	public boolean invokeRelated() {
		return false;
	}

	@Override
	public String toString() {
		return "JavaClassRelationType[intensity=" + getIntensity() + ", name=" + getName() + ",tableName=" + tableName
				+ "]";
	}

	@Override
	public void init(JavaClassCollection javaClasses) {

		Map<String, String> entryMapTableName = new HashMap<String, String>();
		String littleClassName;

		for (JavaClass javaClass : javaClasses.getJavaClasses()) {
			// 收集Entry和TableName对应关系信息
			L: for (TableInfo tableInfo : javaClass.getDetail().getTables()) {
				if (tableInfo.isDefine()) {
					littleClassName = javaClass.getName().substring(javaClass.getName().lastIndexOf('.') + 1)
							.toUpperCase();
					entryMapTableName.put(littleClassName, tableInfo.getTableName());

					break L;
				}
			}
		}
		// 更新TableName
		if (entryMapTableName.size() > 0) {
			for (JavaClass javaClass : javaClasses.getJavaClasses()) {
				for (TableInfo tableInfo : javaClass.getDetail().getTables()) {
					if (!tableInfo.isDefine()) {
						if (entryMapTableName.containsKey(tableInfo.getTableName())) {
							tableInfo.setTableName(entryMapTableName.get(tableInfo.getTableName()));
						}
					}
				}
			}
		}
	}

	@Override
	public boolean create(JavaClass javaClass, JavaClassCollection javaClasses) {
		boolean isCreate = false;
		JavaClassDetail info = javaClass.getDetail();
		if (info.getTables().size() != 0) {
			for (TableInfo tableInfo : info.getTables()) {
				if (!tableInfo.isDefine()) {
					// 判断是否忽略指定表的关系建立
					if (!this.getTypes().isIgnoreTableInfo(tableInfo)) {
						List<JavaClass> dependJavaClasses = getWriteAndDefineToTableClasses(tableInfo, javaClasses);
						for (JavaClass dependJavaClass1 : dependJavaClasses) {
							if (setDependInfo(javaClass, dependJavaClass1, this.clone(tableInfo.getTableName()))) {
								isCreate = true;
							}
						}
					}
				}
			}
		}
		return isCreate;
	}

	private List<JavaClass> getWriteAndDefineToTableClasses(TableInfo tableInfo, JavaClassCollection javaClasses) {

		List<JavaClass> rtn = new ArrayList<JavaClass>();
		for (JavaClass javaClass : javaClasses.getJavaClasses()) {
			for (TableInfo currentTableInfo : javaClass.getDetail().getTables()) {
				if (currentTableInfo.getTableName().equalsIgnoreCase(tableInfo.getTableName())
						&& (currentTableInfo.isWrite()// 目标为写
						|| currentTableInfo.isDefine())// 目标为定义
				) {
					rtn.add(javaClass);
				}
			}
		}
		return rtn;
	}

}
