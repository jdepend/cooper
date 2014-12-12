package jdepend.model.relationtype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.domain.PersistentBean;
import jdepend.framework.domain.notPersistent;
import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClassRelationType;
import jdepend.model.TableInfo;

/**
 * 关系类型管理器
 * 
 * @author <b>Abner</b>
 * 
 */
public class JavaClassRelationTypeMgr extends PersistentBean {

	private static final long serialVersionUID = -2391394434052695666L;

	public static final String Inherit = "Inherit";
	public static final String Field = "Field";
	public static final String Param = "Param";
	public static final String Variable = "Variable";
	public static final String Table = "Table";
	public static final String REST = "REST";

	private static JavaClassRelationTypeMgr mgr;

	private transient Map<String, JavaClassRelationType> types = new HashMap<String, JavaClassRelationType>();

	private Collection<String> ignoreTables;// 实际忽略的表集合

	private Boolean ignoreTable;// 是否忽略由指定的数据库表建立的组件间关系

	private JavaClassRelationTypeMgr() {

		super("类关系管理器", "类关系管理器", PropertyConfigurator.DEFAULT_PROPERTY_DIR);

		if (ignoreTables == null) {
			ignoreTables = new ArrayList<String>();
		}
		if (ignoreTable == null) {
			ignoreTable = true;
		}

		types.put(Inherit, new InheritRelation(1F));
		types.put(Field, new FieldRelation(0.8F));
		types.put(Param, new ParamRelation(0.5F));
		types.put(Variable, new VariableRelation(0.3F));
		types.put(Table, new TableRelation(0.1F));
		types.put(REST, new RESTRelation(0.1F));
	}

	public static JavaClassRelationTypeMgr getInstance() {
		if (mgr == null) {
			mgr = new JavaClassRelationTypeMgr();
		}
		return mgr;
	}

	public List<JavaClassRelationType> getTypes() {
		return new ArrayList<JavaClassRelationType>(types.values());
	}

	private JavaClassRelationType getType(String name) {
		return types.get(name);
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

	public void registType(JavaClassRelationType type) throws JDependException {

		if (type instanceof FieldRelation) {
			JavaClassRelationTypeMgr.getInstance().setFieldRelation((FieldRelation) type);
		} else if (type instanceof ParamRelation) {
			JavaClassRelationTypeMgr.getInstance().setParamRelation((ParamRelation) type);
		} else if (type instanceof VariableRelation) {
			JavaClassRelationTypeMgr.getInstance().setVariableRelation((VariableRelation) type);
		} else if (type instanceof InheritRelation) {
			JavaClassRelationTypeMgr.getInstance().setInheritRelation((InheritRelation) type);
		} else if (type instanceof TableRelation) {
			JavaClassRelationTypeMgr.getInstance().setTableRelation((TableRelation) type);
		} else if (type instanceof RESTRelation) {
			JavaClassRelationTypeMgr.getInstance().setRESTRelation((RESTRelation) type);
		} else {
			if (types.containsKey(type.getName()))
				throw new JDependException("类型[" + type.getName() + "]已经注册了。");

			this.types.put(type.getName(), type);
		}
	}

	@notPersistent
	public InheritRelation getInheritRelation() {
		return (InheritRelation) getType(Inherit);
	}

	@notPersistent
	public FieldRelation getFieldRelation() {
		return (FieldRelation) getType(Field);
	}

	@notPersistent
	public ParamRelation getParamRelation() {
		return (ParamRelation) getType(Param);
	}

	@notPersistent
	public VariableRelation getVariableRelation() {
		return (VariableRelation) getType(Variable);
	}

	@notPersistent
	public TableRelation getTableRelation() {
		return (TableRelation) getType(Table);
	}

	@notPersistent
	public TableRelation getRESTRelation() {
		return (TableRelation) getType(REST);
	}

	@notPersistent
	public void setInheritRelation(InheritRelation type) {
		if (type != null)
			types.put(Inherit, type);
	}

	@notPersistent
	public void setFieldRelation(FieldRelation type) {
		if (type != null)
			types.put(Field, type);
	}

	@notPersistent
	public void setParamRelation(ParamRelation type) {
		if (type != null)
			types.put(Param, type);
	}

	@notPersistent
	public void setVariableRelation(VariableRelation type) {
		if (type != null)
			types.put(Variable, type);
	}

	@notPersistent
	public void setTableRelation(TableRelation type) {
		if (type != null)
			types.put(Table, type);
	}

	@notPersistent
	public void setRESTRelation(RESTRelation type) {
		if (type != null)
			types.put(REST, type);
	}

	public Collection<String> getIgnoreTables() {
		return ignoreTables;
	}

	public void setIgnoreTables(Collection<String> ignoreTables) {
		this.ignoreTables = ignoreTables;
	}

	public boolean getIgnoreTable() {
		return ignoreTable;
	}

	public void setIgnoreTable(boolean ignoreTable) {
		this.ignoreTable = ignoreTable;
	}
}
