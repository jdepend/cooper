package jdepend.metadata.tree;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.relationtype.InheritRelation;

public class JavaClassCaTreeCreator extends JavaClassTreeCreator {

	public JavaClassCaTreeCreator() {
		super();
	}

	@Override
	protected Collection<JavaClass> getRelationClass(JavaClass javaClass) {

		Collection<JavaClass> javaClasses = new HashSet<JavaClass>();

		for (JavaClassRelationItem relationItem : javaClass.getCaItems()) {
			if (!(relationItem.getType() instanceof InheritRelation)) {
				javaClasses.add(relationItem.getSource());
			}
		}

		return javaClasses;
	}
}
