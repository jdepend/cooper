package jdepend.metadata.relationtype;

public class ParamRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8570057603783583605L;

	public ParamRelation(float intensity) {
		super(JavaClassRelationTypes.Param, intensity);
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
