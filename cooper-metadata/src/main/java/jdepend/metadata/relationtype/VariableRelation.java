package jdepend.metadata.relationtype;

public class VariableRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 491333186837514572L;

	public VariableRelation(float intensity) {
		super(JavaClassRelationTypes.Variable, intensity);
	}
	
	@Override
	public boolean canAbstraction() {
		return false;
	}
	
	@Override
	public boolean invokeRelated() {
		return true;
	}
}
