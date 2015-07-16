package test.jdepend.parse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import test.common.TestConfigUtil;

import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.Attribute;
import jdepend.metadata.InvokeItem;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.JavaPackage;
import jdepend.metadata.Method;
import jdepend.metadata.RemoteInvokeItem;
import jdepend.metadata.relationtype.TableRelation;
import jdepend.metadata.tree.JavaClassFieldTreesCreator;
import jdepend.metadata.tree.JavaClassInheritTreesCreator;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.metadata.tree.JavaPackageNode;
import jdepend.metadata.tree.JavaPackageTreeCreator;
import jdepend.metadata.util.JavaClassUtil;
import jdepend.parse.Parse;
import junit.framework.TestCase;

public final class ParseTest extends TestCase {

	private Collection<JavaPackage> javaPackages;
	private Collection<JavaClass> javaClasses;

	@Override
	protected void setUp() throws Exception {

		Parse parse = new Parse();

		for (String p : TestConfigUtil.getSelfPath()) {
			parse.addDirectorys(p);
		}
		javaPackages = parse.execute();
		this.javaClasses = JavaClassUtil.getClassesForJavaPackages(javaPackages);
	}

	public void testAbstractClass() {
		for (JavaClass javaClass : this.javaClasses) {
			if (javaClass.isAbstract()) {
				System.out.println("abstract javaClass :" + javaClass.getName());
			}
		}
	}

	public void testClassType() {
		for (JavaClass javaClass : this.javaClasses) {
			if (javaClass.getClassType().equals(JavaClass.Service_TYPE)) {
				System.out.println("Service :" + javaClass.getName());
			}
		}
	}

	public void testIsAbstractAttribute() {
		for (JavaClass javaClass : this.javaClasses) {
			for (Attribute attibute : javaClass.getAttributes()) {
				if (attibute.isAbstract()) {
					System.out.println("abstract attibute :" + attibute.getName());
				}
			}
		}
	}

	public void testIsConstruction() {
		for (JavaClass javaClass : this.javaClasses) {
			for (Method method : javaClass.getMethods()) {
				if (method.isConstruction()) {
					System.out.println("Construction Method :" + method);
				}
			}
		}
	}

	public void testGetReturnTypes() {

		System.out.println("the Method of ReturnType is java.util.Collection :");

		for (JavaClass javaClass : this.javaClasses) {
			for (Method method : javaClass.getSelfMethods()) {
				if (method.getReturnTypes().contains("java.util.Collection")) {
					System.out.println("Info :" + method.getInfo());
					System.out.println("Signature :" + method.getSignature());
				}
			}
		}
	}

	public void testGetArgumentTypes() {

		System.out.println("the Method of ArgumentType is java.util.Collection :");

		for (JavaClass javaClass : this.javaClasses) {
			for (Method method : javaClass.getMethods()) {
				if (method.getArgumentTypes().contains("java.util.Collection")) {
					System.out.println("Info :" + method.getInfo());
					System.out.println("Signature :" + method.getSignature());
					System.out.println("ArgumentType Size :" + method.getArgumentTypes().size());
				}
			}
		}
	}

	public void testSubClass() {
		for (JavaClass javaClass : this.javaClasses) {
			if (javaClass.getSubClasses().size() > 0) {
				System.out.println("the javaClass of have SubClass :" + javaClass.getName());
			}
		}
	}

	public void testJavaClassRelationItem() {

		for (JavaClass javaClass : this.javaClasses) {
			for (JavaClassRelationItem item : javaClass.getCaItems()) {
				if (item.getType() instanceof TableRelation) {
					System.out.println("TableRelation :" + item);
				}
			}
		}
	}

	public void testJavaClassRelationItemDetail() {

		if (this.javaClasses.size() > 0) {
			JavaClass javaClass = this.javaClasses.iterator().next();
			for (JavaClassRelationItem item : javaClass.getCaItems()) {
				if (item.getRelationIntensity() > 0) {
					System.out.println("Source :" + item.getSource().getName());
					System.out.println("Target :" + item.getTarget().getName());
					System.out.println("Type :" + item.getType().getName());
					System.out.println("RelationIntensity :"
							+ MetricsFormat.toFormattedMetrics(item.getRelationIntensity()));
				}
			}

			for (JavaClassRelationItem item : javaClass.getCeItems()) {
				if (item.getRelationIntensity() > 0) {
					System.out.println("Source :" + item.getSource().getName());
					System.out.println("Target :" + item.getTarget().getName());
					System.out.println("Type :" + item.getType().getName());
					System.out.println("RelationIntensity :"
							+ MetricsFormat.toFormattedMetrics(item.getRelationIntensity()));
				}
			}
		}
	}

	public void testRemoteInvokeItem() {
		for (JavaClass javaClass : this.javaClasses) {
			for (Method method : javaClass.getMethods()) {
				for (InvokeItem item : method.getInvokedItems()) {
					if (item instanceof RemoteInvokeItem) {
						System.out.println("RemoteInvokeItem :" + item);
					}
				}
			}
		}
	}

	public void testRequestMapping() {
		for (JavaClass javaClass : this.javaClasses) {
			for (Method method : javaClass.getMethods()) {
				if (method.getRequestMapping() != null) {
					System.out.println("the Method of have RequestMapping :" + method.getName() + " RequestMapping :"
							+ method.getRequestMapping());
				}
			}
		}
	}

	public void testJavaClassInheritTreeCreator() {
		JavaClassInheritTreesCreator creator = new JavaClassInheritTreesCreator();
		List<JavaClassTree> trees = creator.create(this.javaClasses);
		Collections.sort(trees);

		if (trees.size() > 0) {
			System.out.println("max InheritTree :\n" + trees.get(0).toString());
		}
	}

	public void testJavaClassFieldTreeCreator() {
		JavaClassFieldTreesCreator creator = new JavaClassFieldTreesCreator();
		List<JavaClassTree> trees = creator.create(this.javaClasses);
		Collections.sort(trees);

		if (trees.size() > 0) {
			System.out.println("max FieldTree :\n" + trees.get(0).toString());
		}
	}

	public void testJavaPackageTreeCreator() {
		JavaPackageTreeCreator creator = new JavaPackageTreeCreator();
		JavaPackageNode root = creator.createTree(javaPackages);

		System.out.println("JavaPackageTree :\n" + root.getTree());
	}

}
