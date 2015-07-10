package jdepend.model.profile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jdepend.metadata.relationtype.JavaClassRelationTypes;

public class JavaClassRelationItemProfile implements Serializable {

	private static final long serialVersionUID = 6254286808982233198L;

	private Map<String, Float> types;

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

	public String getExplain() {
		StringBuilder info = new StringBuilder();

		info.append("类关系的强度与类型有关，不同的关系类型有不同的强度。类关系的强度是构成组件关系强度的依据。\n\n");
		info.append("当某一类关系类型的强度被设置为0时，系统将不采集该类关系。\n\n");

		return info.toString();
	}
}
