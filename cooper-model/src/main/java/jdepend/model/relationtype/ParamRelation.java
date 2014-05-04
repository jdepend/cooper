package jdepend.model.relationtype;

public class ParamRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8570057603783583605L;

	public ParamRelation(float intensity) {
		super(JavaClassRelationTypeMgr.Param, intensity);
	}
}
