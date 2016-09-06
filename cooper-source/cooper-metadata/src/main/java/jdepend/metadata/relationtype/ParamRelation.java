package jdepend.metadata.relationtype;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassDetail;
import jdepend.metadata.util.JavaClassCollection;

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

	@Override
	public boolean create(JavaClass javaClass, JavaClassCollection javaClasses) {
		boolean isCreate = false;
		JavaClassDetail info = javaClass.getDetail();

		if (info.getParamTypes().size() != 0) {
			for (String paramType : info.getParamTypes()) {
				JavaClass dependJavaClass = javaClasses.getTheClass(javaClass.getPlace(), paramType);
				if (setDependInfo(javaClass, dependJavaClass, this)) {
					isCreate = true;
				}
			}
		}
		return isCreate;
	}
}
