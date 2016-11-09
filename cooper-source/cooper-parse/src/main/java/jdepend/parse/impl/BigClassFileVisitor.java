package jdepend.parse.impl;

import java.util.List;
import java.util.Map;

import jdepend.metadata.JavaClass;
import jdepend.metadata.TableInfo;
import jdepend.parse.sql.ConfigParseMgr;
import jdepend.parse.sql.SqlParseUtil;
import jdepend.parse.sql.TableInfoItem;
import jdepend.parse.util.ParseTool;

import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;

public class BigClassFileVisitor extends SmallClassFileVisitor {

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

}
