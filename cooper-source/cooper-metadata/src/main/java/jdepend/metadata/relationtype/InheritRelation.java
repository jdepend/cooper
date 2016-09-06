package jdepend.metadata.relationtype;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassDetail;
import jdepend.metadata.util.JavaClassCollection;

public class InheritRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6683354612788682609L;

	public InheritRelation(float intensity) {
		super(JavaClassRelationTypes.Inherit, intensity);
	}

	@Override
	public boolean canAbstraction() {
		return false;
	}

	@Override
	public boolean invokeRelated() {
		return false;
	}

	@Override
	public boolean create(JavaClass javaClass, JavaClassCollection javaClasses) {
		boolean isCreate = false;
		JavaClassDetail info = javaClass.getDetail();
		if (info.getSuperClass() != null) {
			if (setDependInfo(javaClass, info.getSuperClass(), this)) {
				isCreate = true;
			}
		}
		if (info.getInterfaceNames().size() != 0) {
			for (JavaClass interfaceClass : info.getInterfaces()) {
				if (setDependInfo(javaClass, interfaceClass, this)) {
					isCreate = true;
				}
			}
		}
		return isCreate;
	}
}
