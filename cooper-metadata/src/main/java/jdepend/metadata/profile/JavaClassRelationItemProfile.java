package jdepend.metadata.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.metadata.JavaClassRelationType;
import jdepend.metadata.relationtype.FieldRelation;
import jdepend.metadata.relationtype.HttpRelation;
import jdepend.metadata.relationtype.InheritRelation;
import jdepend.metadata.relationtype.JavaClassRelationTypes;
import jdepend.metadata.relationtype.ParamRelation;
import jdepend.metadata.relationtype.TableRelation;
import jdepend.metadata.relationtype.VariableRelation;

public class JavaClassRelationItemProfile implements Serializable {

	private static final long serialVersionUID = 6254286808982233198L;

	private Map<String, Float> types;

	private List<String> ignoreTables;// 实际忽略的表集合

	private transient JavaClassRelationTypes javaClassRelationTypes;

	public static List<String> getAllTypes() {

		List<String> allTypes = new ArrayList<String>();

		allTypes.add(JavaClassRelationTypes.Inherit);
		allTypes.add(JavaClassRelationTypes.Field);
		allTypes.add(JavaClassRelationTypes.Param);
		allTypes.add(JavaClassRelationTypes.Variable);
		allTypes.add(JavaClassRelationTypes.Table);
		allTypes.add(JavaClassRelationTypes.Http);

		return allTypes;
	}

	public Map<String, Float> getTypes() {
		return types;
	}

	public void setTypes(Map<String, Float> types) {
		this.types = types;
	}

	public Float getType(String name) {
		return this.types.get(name);
	}

	public List<String> getIgnoreTables() {
		return ignoreTables;
	}

	public void setIgnoreTables(List<String> ignoreTables) {
		this.ignoreTables = ignoreTables;
	}

	public JavaClassRelationTypes getJavaClassRelationTypes() {

		if (javaClassRelationTypes == null) {
			javaClassRelationTypes = new JavaClassRelationTypes();

			Map<String, JavaClassRelationType> types = new HashMap<String, JavaClassRelationType>();

			types.put(JavaClassRelationTypes.Inherit, new InheritRelation(this.getType(JavaClassRelationTypes.Inherit)));
			types.put(JavaClassRelationTypes.Field, new FieldRelation(this.getType(JavaClassRelationTypes.Field)));
			types.put(JavaClassRelationTypes.Param, new ParamRelation(this.getType(JavaClassRelationTypes.Param)));
			types.put(JavaClassRelationTypes.Variable,
					new VariableRelation(this.getType(JavaClassRelationTypes.Variable)));
			types.put(JavaClassRelationTypes.Table, new TableRelation(this.getType(JavaClassRelationTypes.Table)));
			types.put(JavaClassRelationTypes.Http, new HttpRelation(this.getType(JavaClassRelationTypes.Http)));

			javaClassRelationTypes.setTypes(types);
		}

		return javaClassRelationTypes;
	}

	public String getExplain() {
		StringBuilder info = new StringBuilder();

		info.append("类关系的强度与类型有关，不同的关系类型有不同的强度。类关系的强度是构成组件关系强度的依据。\n\n");
		info.append("当某一类关系类型的强度被设置为0时，系统将不采集该类关系。\n\n");

		return info.toString();
	}
}
