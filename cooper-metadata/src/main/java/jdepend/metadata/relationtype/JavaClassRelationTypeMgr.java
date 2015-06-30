package jdepend.metadata.relationtype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.domain.PersistentBean;
import jdepend.framework.exception.JDependException;
import jdepend.metadata.JavaClassRelationType;

/**
 * 关系类型管理器
 * 
 * @author <b>Abner</b>
 * 
 */
public class JavaClassRelationTypeMgr extends PersistentBean {

	private static final long serialVersionUID = -2391394434052695666L;

	private static JavaClassRelationTypeMgr mgr;

	private transient Map<String, JavaClassRelationType> types = new HashMap<String, JavaClassRelationType>();

	private Collection<String> ignoreTables;// 实际忽略的表集合

	private Boolean ignoreTable;// 是否忽略由指定的数据库表建立的组件间关系

	private transient JavaClassRelationTypes javaClassRelationTypes;

	private JavaClassRelationTypeMgr() {

		super("类关系管理器", "类关系管理器", PropertyConfigurator.DEFAULT_PROPERTY_DIR);

		if (ignoreTables == null) {
			ignoreTables = new ArrayList<String>();
		}
		if (ignoreTable == null) {
			ignoreTable = true;
		}

		types.put(JavaClassRelationTypes.Inherit, new InheritRelation(1F));
		types.put(JavaClassRelationTypes.Field, new FieldRelation(0.8F));
		types.put(JavaClassRelationTypes.Param, new ParamRelation(0.5F));
		types.put(JavaClassRelationTypes.Variable, new VariableRelation(0.3F));
		types.put(JavaClassRelationTypes.Table, new TableRelation(0.1F));
		types.put(JavaClassRelationTypes.Http, new HttpRelation(0.1F));
	}

	public static JavaClassRelationTypeMgr getInstance() {
		if (mgr == null) {
			mgr = new JavaClassRelationTypeMgr();
		}
		return mgr;
	}

	public JavaClassRelationTypes getJavaClassRelationTypes() {

		if (javaClassRelationTypes == null) {
			javaClassRelationTypes = new JavaClassRelationTypes();

			javaClassRelationTypes.setTypes(types);
			javaClassRelationTypes.setIgnoreTable(ignoreTable);
			javaClassRelationTypes.setIgnoreTables(ignoreTables);
		}

		return javaClassRelationTypes;
	}

	public JavaClassRelationType getType(String name) {
		return types.get(name);
	}

	public void registType(JavaClassRelationType type) throws JDependException {

		if (type instanceof FieldRelation) {
			this.types.put(JavaClassRelationTypes.Field, type);
		} else if (type instanceof ParamRelation) {
			this.types.put(JavaClassRelationTypes.Param, type);
		} else if (type instanceof VariableRelation) {
			this.types.put(JavaClassRelationTypes.Variable, type);
		} else if (type instanceof InheritRelation) {
			this.types.put(JavaClassRelationTypes.Inherit, type);
		} else if (type instanceof TableRelation) {
			this.types.put(JavaClassRelationTypes.Table, type);
		} else if (type instanceof HttpRelation) {
			this.types.put(JavaClassRelationTypes.Http, type);
		} else {
			if (types.containsKey(type.getName()))
				throw new JDependException("类型[" + type.getName() + "]已经注册了。");

			this.types.put(type.getName(), type);
		}
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
