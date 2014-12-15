package jdepend.model.relationtype;

public class HttpRelation extends BaseJavaClassRelationType {

	private static final long serialVersionUID = 8142224457447489950L;

	public HttpRelation(float intensity) {
		super(JavaClassRelationTypeMgr.Http, intensity);
	}
}
