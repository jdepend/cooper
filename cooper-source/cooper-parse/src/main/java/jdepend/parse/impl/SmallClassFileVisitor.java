package jdepend.parse.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jdepend.metadata.JavaClass;
import jdepend.metadata.TableInfo;
import jdepend.metadata.util.ParseUtil;
import jdepend.parse.sql.ConfigParseMgr;
import jdepend.parse.sql.TableInfoItem;
import jdepend.parse.util.ParseTool;

import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.Method;

public class SmallClassFileVisitor extends JDependClassFileVisitor {

	public SmallClassFileVisitor(JavaClass jClass) {
		super(jClass);
	}

	@Override
	public void visitConstantClass(ConstantClass obj) {
		String name1 = obj.getBytes(this.cp);
		this.parser.debug("visitConstantClass: obj.getBytes(this.cp) = " + name1);
		String name = ParseTool.slashesToDots(name1);
		name = ParseTool.getType(name);
		if (name != null && name.length() > 0 && !jClass.getDetail().getSupers().contains(name)) {
			jClass.getDetail().addVariableType(name);
			this.parser.debug("visitConstantClass: type = " + name);
		}
	}

	@Override
	public void visitConstantFieldref(ConstantFieldref obj) {
		String name = obj.getClass(this.cp);
		jClass.getDetail().addVariableType(name);
		this.parser.debug("visitConstantFieldref: variable type = " + name);
	}

	@Override
	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) {
		String name = obj.getClass(this.cp);
		jClass.getDetail().addVariableType(name);
		this.parser.debug("visitConstantInterfaceMethodref: variable type = " + name);
	}

	@Override
	public void visitConstantMethodref(ConstantMethodref obj) {
		String name = obj.getClass(this.cp);
		String variableType = null;
		if (name.startsWith("[")) {
			if (name.startsWith("[L")) {
				variableType = name.substring(2, name.length() - 1);
			} else {
				return;
			}
		} else {
			variableType = name;
		}
		if (!jClass.getDetail().getSupers().contains(variableType)) {
			jClass.getDetail().addVariableType(variableType);
			this.parser.debug("visitConstantMethodref: variable type = " + variableType);
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

		this.parser.debug("Parser: class name = " + jClass.getName());
		this.parser.debug("Parser: abstract = " + jClass.isAbstract());
		this.parser.debug("Parser: package name = " + jClass.getPackageName());

		// 处理父类
		if (!obj.getSuperclassName().equals("java.lang.Object")) {
			this.jClass.getDetail().setSuperClassName(obj.getSuperclassName());
			this.parser
					.debug("ParserSuperClassName: super class type = " + this.jClass.getDetail().getSuperClassName());
		}
		// 处理接口
		for (String interfaceName : obj.getInterfaceNames()) {
			this.jClass.getDetail().addInterfaceName(interfaceName);
			this.parser.debug("ParserInterfaceNames: interface type = " + interfaceName);
		}

		// 处理表信息
		Map<String, List<TableInfo>> tables = ConfigParseMgr.getInstance().getTheTables(TableInfoItem.ClassNameType);
		if (tables.containsKey(jClass.getName())) {
			for (TableInfo tableInfo : tables.get(jClass.getName())) {
				jClass.getDetail().addTable(tableInfo);
			}
		}

		jClass.getDetail().parseAnnotation(obj);
	}

	@Override
	public void visitLineNumber(LineNumber obj) {
		if (obj.getLineNumber() > this.jClass.getLineCount()) {
			this.jClass.setLineCount(obj.getLineNumber());
		}
	}
	
	@Override
	public void visitField(Field obj) {
		jdepend.metadata.Attribute attribute = new jdepend.metadata.Attribute(this.jClass, obj);
		this.jClass.getDetail().addAttribute(attribute);
		this.parser.debug("visitField: obj.getSignature() = " + attribute.getSignature());
	}

	@Override
	public void visitLocalVariable(LocalVariable obj) {
		String name1 = obj.getSignature();
		this.parser.debug("visitLocalVariable: obj.getSignature() = " + name1);
		Collection<String> types = ParseUtil.signatureToTypes(name1);
		for (String name : types) {
			this.jClass.getDetail().addVariableType(name);
			this.parser.debug("visitLocalVariable: variable type = " + name);
		}

	}

	@Override
	public void visitMethod(Method obj) {
		if (!obj.isSynthetic()) {// 不采集编译器生成的Method
			jdepend.metadata.Method method = new jdepend.metadata.Method(this.jClass, obj);
			if (!obj.getName().equals("<clinit>")) {

				new GeneralMethodReader(method, parser.getConf().getPackageFilter()).read(obj);
				new HttpInvokeMethodReader(method, parser.getConf()).read(obj);

				method.setSelfLineCount(this.calLineCount(obj));

				this.jClass.getDetail().addMethod(method);

				this.parser.debug("visitMethod: method type = " + obj);
			} else {
				new ClInitMethodReader(method).read(obj);
			}
		}
	}

	private int calLineCount(org.apache.bcel.classfile.Method method) {
		if (method.getCode() != null && method.getCode().getCode().length == 1) {
			return 0;
		} else {
			LineNumberTable lt = method.getLineNumberTable();
			if (lt != null) {
				if (lt.getTableLength() == 1) {
					return 1;
				} else {
					int length = lt.getLineNumberTable().length;
					if (length > 1) {
						List<Integer> lineNumbers = new ArrayList<Integer>(lt.getLineNumberTable().length);
						for (LineNumber lineNumber : lt.getLineNumberTable()) {
							lineNumbers.add(lineNumber.getLineNumber());
						}
						Collections.sort(lineNumbers);
						return lineNumbers.get(lineNumbers.size() - 1) - lineNumbers.get(0);
					} else {
						return 0;
					}
				}
			} else {
				return 0;
			}
		}
	}
}
