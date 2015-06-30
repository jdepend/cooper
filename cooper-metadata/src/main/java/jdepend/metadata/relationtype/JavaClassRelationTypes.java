package jdepend.metadata.relationtype;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.metadata.JavaClassRelationType;
import jdepend.metadata.TableInfo;

public class JavaClassRelationTypes implements Serializable {

	private static final long serialVersionUID = 8570801070895879746L;

	public static final String Inherit = "Inherit";
	public static final String Field = "Field";
	public static final String Param = "Param";
	public static final String Variable = "Variable";
	public static final String Table = "Table";
	public static final String Http = "Http";

	private Map<String, JavaClassRelationType> types = new HashMap<String, JavaClassRelationType>();

	private Collection<String> ignoreTables;// 实际忽略的表集合

	private Boolean ignoreTable;// 是否忽略由指定的数据库表建立的组件间关系

	public Map<String, JavaClassRelationType> getTypes() {
		return types;
	}

	private JavaClassRelationType getType(String name) {
		return types.get(name);
	}

	public void setTypes(Map<String, JavaClassRelationType> types) {
		this.types = types;
	}

	public InheritRelation getInheritRelation() {
		return (InheritRelation) getType(Inherit);
	}

	public FieldRelation getFieldRelation() {
		return (FieldRelation) getType(Field);
	}

	public ParamRelation getParamRelation() {
		return (ParamRelation) getType(Param);
	}

	public VariableRelation getVariableRelation() {
		return (VariableRelation) getType(Variable);
	}

	public TableRelation getTableRelation() {
		return (TableRelation) getType(Table);
	}

	public HttpRelation getHttpRelation() {
		return (HttpRelation) getType(Http);
	}

	public void setInheritRelation(InheritRelation type) {
		if (type != null)
			types.put(Inherit, type);
	}

	public void setFieldRelation(FieldRelation type) {
		if (type != null)
			types.put(Field, type);
	}

	public void setParamRelation(ParamRelation type) {
		if (type != null)
			types.put(Param, type);
	}

	public void setVariableRelation(VariableRelation type) {
		if (type != null)
			types.put(Variable, type);
	}

	public void setTableRelation(TableRelation type) {
		if (type != null)
			types.put(Table, type);
	}

	public void setHttpRelation(HttpRelation type) {
		if (type != null)
			types.put(Http, type);
	}

	public Collection<String> getIgnoreTables() {
		return ignoreTables;
	}

	public void setIgnoreTables(Collection<String> ignoreTables) {
		this.ignoreTables = ignoreTables;
	}

	public Boolean getIgnoreTable() {
		return ignoreTable;
	}

	public void setIgnoreTable(Boolean ignoreTable) {
		this.ignoreTable = ignoreTable;
	}

	public boolean isIgnoreTableInfo(TableInfo tableInfo) {
		if (ignoreTable) {
			return this.ignoreTables.contains(tableInfo.getTableName());
		} else {
			return false;
		}
	}

	public void addIgnoreTables(String ignoreTable) {
		this.ignoreTables.add(ignoreTable);
	}
}
