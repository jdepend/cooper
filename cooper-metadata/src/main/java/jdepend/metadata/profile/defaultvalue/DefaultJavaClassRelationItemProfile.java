package jdepend.metadata.profile.defaultvalue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jdepend.metadata.profile.JavaClassRelationItemProfile;
import jdepend.metadata.relationtype.JavaClassRelationTypes;

public class DefaultJavaClassRelationItemProfile extends JavaClassRelationItemProfile {

	public DefaultJavaClassRelationItemProfile() {

		Map<String, Float> types = new HashMap<String, Float>();

		types.put(JavaClassRelationTypes.Inherit, 1F);
		types.put(JavaClassRelationTypes.Field, 0.8F);
		types.put(JavaClassRelationTypes.Param, 0.5F);
		types.put(JavaClassRelationTypes.Variable, 0.3F);
		types.put(JavaClassRelationTypes.Table, 0.1F);
		types.put(JavaClassRelationTypes.Http, 0.1F);

		this.setTypes(types);

		this.setIgnoreTables(new ArrayList<String>());
	}
}
