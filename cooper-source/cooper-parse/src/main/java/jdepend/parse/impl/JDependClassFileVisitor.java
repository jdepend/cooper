package jdepend.parse.impl;

import jdepend.metadata.JavaClass;
import jdepend.parse.util.ParseTool;

import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.EmptyVisitor;

public abstract class JDependClassFileVisitor extends EmptyVisitor {

	protected JavaClass jClass;

	protected AbstractParser parser;

	protected ConstantPool cp;

	public JDependClassFileVisitor(JavaClass jClass) {
		this.jClass = jClass;
	}

	@Override
	public void visitConstantPool(ConstantPool obj) {
		this.cp = obj;
	}

	public ConstantPool getConstantPool() {
		return cp;
	}

	public void setParser(AbstractParser parser) {
		this.parser = parser;
	}

	public void calImportedPackages() {

		String packageName;

		if (this.jClass.getDetail().getSuperClassName() != null) {
			packageName = ParseTool.getPackageName(this.jClass.getDetail().getSuperClassName());
			this.jClass.addImportedPackage(packageName);
		}

		for (String name : this.jClass.getDetail().getInterfaceNames()) {
			packageName = ParseTool.getPackageName(name);
			this.jClass.addImportedPackage(packageName);
		}

		for (String name : this.jClass.getDetail().getAttributeTypes()) {
			packageName = ParseTool.getPackageName(name);
			this.jClass.addImportedPackage(packageName);
		}

		for (String name : this.jClass.getDetail().getParamTypes()) {
			packageName = ParseTool.getPackageName(name);
			this.jClass.addImportedPackage(packageName);
		}

		for (String name : this.jClass.getDetail().getVariableTypes()) {
			packageName = ParseTool.getPackageName(name);
			this.jClass.addImportedPackage(packageName);
		}
	}

}
