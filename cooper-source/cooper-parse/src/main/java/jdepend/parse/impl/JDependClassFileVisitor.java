package jdepend.parse.impl;

import jdepend.metadata.JavaClass;

import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.EmptyVisitor;

public abstract class JDependClassFileVisitor extends EmptyVisitor {

	protected JavaClass jClass;

	protected AbstractParser parser;

	protected ConstantPool cp;

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

	public void setJavaClass(JavaClass jClass) {
		this.jClass = jClass;
	}

	public void calImportedPackages() {

	}

}
