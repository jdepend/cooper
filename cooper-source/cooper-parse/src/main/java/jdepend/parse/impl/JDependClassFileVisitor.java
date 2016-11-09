package jdepend.parse.impl;

import jdepend.metadata.JavaClass;

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

	}

}
