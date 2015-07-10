package jdepend.model.profile.model.defaultvalue;

import java.util.HashMap;
import java.util.Map;

import jdepend.metadata.relationtype.JavaClassRelationTypes;
import jdepend.model.profile.model.JavaClassRelationItemProfile;

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
	}
}
