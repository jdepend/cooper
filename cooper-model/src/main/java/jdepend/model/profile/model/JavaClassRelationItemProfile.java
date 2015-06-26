package jdepend.model.profile.model;

import java.io.Serializable;
import java.util.Map;

public class JavaClassRelationItemProfile implements Serializable {

	private static final long serialVersionUID = 6254286808982233198L;

	private Map<String, Float> types;

	public Map<String, Float> getTypes() {
		return types;
	}

	public void setTypes(Map<String, Float> types) {
		this.types = types;
	}
}
