package jdepend.model.relationtype;

import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;

public class InheritRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6683354612788682609L;

	public InheritRelation(float intensity) {
		super(JavaClassRelationTypeMgr.Inherit, intensity);
	}

	public float getRationality(JavaClass depend, JavaClass current, String direction) {
		if (direction.equals(JavaClassRelationItem.CE_DIRECTION) && depend.isAbstract()
				|| direction.equals(JavaClassRelationItem.CA_DIRECTION) && current.isAbstract()) {
			return 0.4F;
		} else {
			return super.getRationality(depend, current, direction);
		}
	}
	
	@Override
	public boolean canAbstraction() {
		return false;
	}

}
