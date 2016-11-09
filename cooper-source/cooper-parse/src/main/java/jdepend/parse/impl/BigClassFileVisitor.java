package jdepend.parse.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jdepend.metadata.TableInfo;
import jdepend.parse.sql.ConfigParseMgr;
import jdepend.parse.sql.SqlParseUtil;
import jdepend.parse.sql.TableInfoItem;
import jdepend.parse.util.ParseTool;

import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.Method;

public class BigClassFileVisitor extends SmallClassFileVisitor {

	@Override
	public void visitJavaClass(org.apache.bcel.classfile.JavaClass obj) {
		super.visitJavaClass(obj);

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
	public void visitField(Field obj) {
		jdepend.metadata.Attribute attribute = new jdepend.metadata.Attribute(this.jClass, obj, true);
		this.jClass.getDetail().addAttribute(attribute);
		this.parser.debug("visitField: obj.getSignature() = " + attribute.getSignature());
	}

	@Override
	public void visitMethod(Method obj) {
		if (!obj.isSynthetic()) {// 不采集编译器生成的Method
			jdepend.metadata.Method method = new jdepend.metadata.Method(this.jClass, obj, true);
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

	@Override
	public void visitConstantString(ConstantString obj) {
		String name = obj.getBytes(this.cp);
		this.parser.debug("visitConstantString: obj.getBytes(this.cp) = " + name);
		List<TableInfo> tables = this.ParseTable(name);
		if (tables != null) {
			for (TableInfo table : tables) {
				this.jClass.getDetail().addTable(table);
				this.parser.debug("visitConstantString: variable type = " + table);
			}
		}
	}

	@Override
	public void visitConstantUtf8(ConstantUtf8 obj) {
		String name = obj.getBytes();
		this.parser.debug("visitConstantUtf8: obj.getBytes(this.cp) = " + name);
		if (SqlParseUtil.isSQL(name)) {
			List<TableInfo> tables = SqlParseUtil.parserSql(name);
			if (tables != null) {
				for (TableInfo table : tables) {
					this.jClass.getDetail().addTable(table);
				}
			}
		}
	}

	@Override
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

	private List<TableInfo> ParseTable(String constant) {
		if (constant != null) {
			if (SqlParseUtil.isSQL(constant)) {
				return SqlParseUtil.parserSql(constant);
			} else {
				Map<String, List<TableInfo>> tables = ConfigParseMgr.getInstance().getTheTables(TableInfoItem.KeyType);
				if (tables != null && tables.containsKey(constant)) {
					return tables.get(constant);
				} else {
					return null;
				}
			}
		}
		return null;
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
