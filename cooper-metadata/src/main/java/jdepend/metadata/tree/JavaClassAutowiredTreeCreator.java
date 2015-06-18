package jdepend.metadata.tree;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.Attribute;
import jdepend.metadata.JavaClass;
import jdepend.metadata.annotation.Service;

public class JavaClassAutowiredTreeCreator extends JavaClassTreeCreator {

	public JavaClassAutowiredTreeCreator() {
		super();
	}

	@Override
	protected Collection<JavaClass> getRelationClass(JavaClass javaClass) {

		Collection<JavaClass> javaClasses = new HashSet<JavaClass>();

		for (Attribute attribute : javaClass.getAttributes()) {
			if (attribute.getAutowired() != null || attribute.getQualifier() != null) {
				for (JavaClass attributeClass : attribute.getTypeClasses()) {
					if (attributeClass.isAbstract()) {
						Collection<JavaClass> subClasses = new HashSet<JavaClass>();
						for (JavaClass subClass : attributeClass.getSubClasses()) {
							if (!subClass.isAbstract()) {
								subClasses.add(subClass);
							}
						}
						if (subClasses.size() == 0) {
							javaClasses.add(attributeClass);
						} else if (subClasses.size() == 1) {
							javaClasses.add(subClasses.iterator().next());
						} else {
							for (JavaClass subClass : subClasses) {
								Service service = subClass.getDetail().getService();
								if (attribute.getQualifier() != null && attribute.getQualifier().direct(service)) {
									javaClasses.add(subClass);
								}
							}
						}
					} else {
						javaClasses.add(attributeClass);
					}
				}
			}
		}
		return javaClasses;
	}
}
