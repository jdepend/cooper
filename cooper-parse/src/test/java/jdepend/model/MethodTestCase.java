package jdepend.model;

import java.util.Collection;

import jdepend.model.util.ClassSearchUtil;
import jdepend.model.util.JavaClassUtil;
import jdepend.parse.Parse;
import junit.framework.TestCase;

public final class MethodTestCase extends TestCase {

	protected Collection<JavaPackage> javaPackages;

	@Override
	protected void setUp() throws Exception {

		Parse jdepend = new Parse();

		for (String p : ClassSearchUtil.getSelfPath()) {
			jdepend.addDirectorys(p);
		}
		javaPackages = jdepend.execute();
	}

	// public void testIsConstruction() {
	// for (JavaClass javaClass : JavaClassUtil
	// .getClassesForJavaPackages(javaPackages)) {
	// for (Method method : javaClass.getMethods()) {
	// if (method.isConstruction()) {
	// System.out.println(method);
	// }
	// }
	//
	// }
	// }

	public void testGetReturnTypes() {
		for (JavaClass javaClass : JavaClassUtil.getClassesForJavaPackages(javaPackages)) {
			for (Method method : javaClass.getSelfMethods()) {
				if (method.getReturnTypes().contains("java.util.Collection")) {
					System.out.println(method.getInfo());
					System.out.println(method.getName());
					System.out.println(method.getSignature());
					System.out.println(method.getReturnTypes().iterator().next());
				}
			}

		}
	}

	// public void testGetArgumentTypes() {
	// for (JavaClass javaClass : JavaClassUtil
	// .getClassesForJavaPackages(javaPackages)) {
	// for (Method method : javaClass.getMethods()) {
	// if (method.getArgumentTypes().contains("java.util.Collection")) {
	// System.out.println(method.getInfo());
	// System.out.println(method.getName());
	// System.out.println(method.getSignature());
	// System.out.println(method.getArgumentTypes().size());
	// }
	// }
	//
	// }
	// }

}
