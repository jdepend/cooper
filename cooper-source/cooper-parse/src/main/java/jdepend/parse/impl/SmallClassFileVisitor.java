package jdepend.parse.impl;

import java.util.Collection;

import jdepend.metadata.util.ParseUtil;
import jdepend.parse.util.ParseTool;

import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.Method;

public class SmallClassFileVisitor extends JDependClassFileVisitor {

	@Override
	public void visitConstantClass(ConstantClass obj) {
		String name1 = obj.getBytes(this.cp);
		if (this.parser.isDebug()) {
			this.parser.debug("visitConstantClass: obj.getBytes(this.cp) = " + name1);
		}
		String name = ParseTool.slashesToDots(name1);
		name = ParseTool.getType(name);
		if (name != null && name.length() > 0 && !jClass.getDetail().getSupers().contains(name)) {
			jClass.getDetail().addVariableType(name);
			if (this.parser.isDebug()) {
				this.parser.debug("visitConstantClass: type = " + name);
			}
		}
	}

	@Override
	public void visitConstantFieldref(ConstantFieldref obj) {
		String name = obj.getClass(this.cp);
		jClass.getDetail().addVariableType(name);
		if (this.parser.isDebug()) {
			this.parser.debug("visitConstantFieldref: variable type = " + name);
		}
	}

	@Override
	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) {
		String name = obj.getClass(this.cp);
		jClass.getDetail().addVariableType(name);
		if (this.parser.isDebug()) {
			this.parser.debug("visitConstantInterfaceMethodref: variable type = " + name);
		}
	}

	@Override
	public void visitConstantMethodref(ConstantMethodref obj) {
		String name = obj.getClass(this.cp);
		String variableType = ParseTool.getType2(name);
		if (variableType != null && !jClass.getDetail().getSupers().contains(variableType)) {
			jClass.getDetail().addVariableType(variableType);
			if (this.parser.isDebug()) {
				this.parser.debug("visitConstantMethodref: variable type = " + variableType);
			}
		}
	}

	@Override
	public void visitJavaClass(org.apache.bcel.classfile.JavaClass obj) {

		// 基本信息
		jClass.setName(obj.getClassName());
		String packageName = ParseTool.getPackageName(obj.getClassName());
		if (packageName != null) {
			jClass.setPackageName(packageName);
		}
		if (this.parser.isDebug()) {
			this.parser.debug("Parser: class name = " + jClass.getName());
			this.parser.debug("Parser: abstract = " + jClass.isAbstract());
			this.parser.debug("Parser: package name = " + jClass.getPackageName());
		}

		// 处理父类
		if (!obj.getSuperclassName().equals("java.lang.Object")) {
			this.jClass.getDetail().setSuperClassName(obj.getSuperclassName());
			if (this.parser.isDebug()) {
				this.parser.debug("ParserSuperClassName: super class type = "
						+ this.jClass.getDetail().getSuperClassName());
			}
		}
		// 处理接口
		for (String interfaceName : obj.getInterfaceNames()) {
			this.jClass.getDetail().addInterfaceName(interfaceName);
			if (this.parser.isDebug()) {
				this.parser.debug("ParserInterfaceNames: interface type = " + interfaceName);
			}
		}
	}

	@Override
	public void visitLineNumber(LineNumber obj) {
		if (obj.getLineNumber() > this.jClass.getLineCount()) {
			this.jClass.setLineCount(obj.getLineNumber());
		}
	}

	@Override
	public void visitField(Field obj) {
		jdepend.metadata.Attribute attribute = new jdepend.metadata.Attribute(this.jClass, obj, false);
		this.jClass.getDetail().addAttribute(attribute);
		if (this.parser.isDebug()) {
			this.parser.debug("visitField: obj.getSignature() = " + attribute.getSignature());
		}
	}

	@Override
	public void visitLocalVariable(LocalVariable obj) {
		String name1 = obj.getSignature();
		if (this.parser.isDebug()) {
			this.parser.debug("visitLocalVariable: obj.getSignature() = " + name1);
			Collection<String> types = ParseUtil.signatureToTypes(name1);
			for (String name : types) {
				this.jClass.getDetail().addVariableType(name);
				if (this.parser.isDebug()) {
					this.parser.debug("visitLocalVariable: variable type = " + name);
				}
			}
		}
	}

	@Override
	public void visitMethod(Method obj) {
		if (!obj.isSynthetic()) {// 不采集编译器生成的Method
			jdepend.metadata.Method method = new jdepend.metadata.Method(this.jClass, obj, false);
			if (!obj.getName().equals("<clinit>")) {
				this.jClass.getDetail().addMethod(method);
				if (this.parser.isDebug()) {
					this.parser.debug("visitMethod: method type = " + obj);
				}
			}
		}
	}

}
