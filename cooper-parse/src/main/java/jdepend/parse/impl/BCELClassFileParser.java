package jdepend.parse.impl;

import java.io.InputStream;

import jdepend.metadata.JavaClass;
import jdepend.parse.ParseConfigurator;
import jdepend.parse.util.ParseTool;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.DescendingVisitor;

public class BCELClassFileParser extends AbstractParser {

	public BCELClassFileParser(ParseConfigurator conf) {
		super(conf);
	}

	@Override
	protected JavaClass doParse(String place, InputStream is) throws ParseClassException {

		JavaClass jClass = null;
		try {

			ClassParser parser = new ClassParser(is, null);

			org.apache.bcel.classfile.JavaClass javaClass = parser.parse();

			jClass = new JavaClass("Unknown", true, javaClass.getAccessFlags());
			jClass.setPlace(place);

			JDependClassFileVisitor visitor = new JDependClassFileVisitor(jClass);
			visitor.setParser(this);

			DescendingVisitor dvisitor = new DescendingVisitor(javaClass, visitor);

			dvisitor.visit();

			this.calImportedPackages(jClass);

			this.debug("");

			this.getWriter().flush();

			return jClass;
		} catch (Exception e) {
			if (jClass != null) {
				throw new ParseClassException(jClass.getName(), e);
			} else {
				throw new ParseClassException(e);
			}
		}
	}

	public void calImportedPackages(JavaClass javaClass) {

		String packageName;

		if (javaClass.getDetail().getSuperClassName() != null) {
			packageName = ParseTool.getPackageName(javaClass.getDetail().getSuperClassName());
			javaClass.addImportedPackage(packageName);
		}

		for (String name : javaClass.getDetail().getInterfaceNames()) {
			packageName = ParseTool.getPackageName(name);
			javaClass.addImportedPackage(packageName);
		}

		for (String name : javaClass.getDetail().getAttributeTypes()) {
			packageName = ParseTool.getPackageName(name);
			javaClass.addImportedPackage(packageName);
		}

		for (String name : javaClass.getDetail().getParamTypes()) {
			packageName = ParseTool.getPackageName(name);
			javaClass.addImportedPackage(packageName);
		}

		for (String name : javaClass.getDetail().getVariableTypes()) {
			packageName = ParseTool.getPackageName(name);
			javaClass.addImportedPackage(packageName);
		}
	}
}
