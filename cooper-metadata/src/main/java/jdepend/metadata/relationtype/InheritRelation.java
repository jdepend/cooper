package jdepend.metadata.relationtype;

import jdepend.metadata.JavaClass;

public class InheritRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6683354612788682609L;

	public InheritRelation(float intensity) {
		super(JavaClassRelationTypeMgr.Inherit, intensity);
	}

	public float getRationality(JavaClass target, JavaClass source, String direction) {
		if (target.isAbstract()) {
			return 0.4F;
		} else {
			return super.getRationality(target, source, direction);
		}
	}

	@Override
	public boolean canAbstraction() {
		return false;
	}

}
