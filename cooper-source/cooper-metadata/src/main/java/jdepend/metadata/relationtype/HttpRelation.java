package jdepend.metadata.relationtype;

public class HttpRelation extends BaseJavaClassRelationType {

	private static final long serialVersionUID = 8142224457447489950L;

	public HttpRelation(float intensity) {
		super(JavaClassRelationTypes.Http, intensity);
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
