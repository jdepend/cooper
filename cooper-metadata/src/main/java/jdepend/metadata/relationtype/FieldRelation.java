package jdepend.metadata.relationtype;

public class FieldRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2342635441277588070L;

	public FieldRelation(float intensity) {
		super(JavaClassRelationTypeMgr.Field, intensity);
	}

	@Override
	public boolean canAbstraction() {
		return true;
	}
	
	@Override
	public boolean invokeRelated() {
		return true;
	}
}
