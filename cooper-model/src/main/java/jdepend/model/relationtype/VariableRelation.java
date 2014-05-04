package jdepend.model.relationtype;

public class VariableRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 491333186837514572L;

	public VariableRelation(float intensity) {
		super(JavaClassRelationTypeMgr.Variable, intensity);
	}
}
