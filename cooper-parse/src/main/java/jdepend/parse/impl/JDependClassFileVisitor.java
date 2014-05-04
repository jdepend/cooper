package jdepend.parse.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.framework.util.ParseUtil;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;
import jdepend.model.TableInfo;
import jdepend.model.util.SignatureUtil;

import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.util.ByteSequence;

public class JDependClassFileVisitor extends EmptyVisitor {

	private JavaClass jClass;

	private AbstractParser parser;

	private ConstantPool cp;

	public JDependClassFileVisitor(JavaClass jClass) {
		this.jClass = jClass;
	}

	@Override
	public void visitConstantString(ConstantString obj) {
		this.parser.debug("visitConstantString: obj.getBytes(this.cp) = " + obj.getBytes(this.cp));
		List<TableInfo> tables = this.ParseTable(obj.getBytes(cp));
		if (tables != null) {
			for (TableInfo table : tables) {
				this.jClass.getDetail().addTable(table);
				this.parser.debug("visitConstantString: variable type = " + table);
			}
		}
	}

	@Override
	public void visitConstantClass(ConstantClass obj) {
		this.parser.debug("visitConstantClass: obj.getBytes(this.cp) = " + obj.getBytes(this.cp));
		String name = ParseUtil.slashesToDots(obj.getBytes(this.cp));
		name = ParseUtil.getType(name);
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
	public void visitConstantPool(ConstantPool obj) {
		this.cp = obj;
	}

	public ConstantPool getConstantPool() {
		return cp;
	}

	@Override
	public void visitField(Field obj) {
		this.jClass.getDetail().addAttribute(new jdepend.model.Attribute(obj));
		this.parser.debug("visitField: obj.getSignature() = " + SignatureUtil.getSignature(obj));
	}

	@Override
	public void visitJavaClass(org.apache.bcel.classfile.JavaClass obj) {

		// 基本信息
		jClass.setName(obj.getClassName());
		String packageName = ParseUtil.getPackageName(obj.getClassName());
		if (packageName != null) {
			jClass.setPackageName(packageName);
		}

		this.parser.debug("Parser: class name = " + jClass.getName());
		this.parser.debug("Parser: abstract = " + jClass.isAbstract());
		this.parser.debug("Parser: package name = " + jClass.getPackageName());

		jClass.setIncludeTransactionalAnnotation(this.searchTransactionalAnnotation(obj.getAttributes(), obj
				.getConstantPool()));

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

	}

	@Override
	public void visitLineNumber(LineNumber obj) {
		if (obj.getLineNumber() > this.jClass.getLineCount()) {
			this.jClass.setLineCount(obj.getLineNumber());
		}
	}
	
	@Override
	public void visitLocalVariable(LocalVariable obj) {
		this.parser.debug("visitLocalVariable: obj.getSignature() = " + obj.getSignature());
		Collection<String> types = ParseUtil.signatureToTypes(obj.getSignature());
		for (String name : types) {
			this.jClass.getDetail().addVariableType(name);
			this.parser.debug("visitLocalVariable: variable type = " + name);
		}

	}

	@Override
	public void visitMethod(Method obj) {

		if (!obj.getName().equals("<clinit>") && !obj.isSynthetic()) {
			jdepend.model.Method method = new jdepend.model.Method(this.jClass, obj);
			this.internalSearchForMethod(method, obj, parser.getFilter());
			method.setSelfLineCount(this.calLineCount(obj));
			method.setIncludeTransactionalAnnotation(this.searchTransactionalAnnotation(obj.getAttributes(), obj
					.getConstantPool()));
			this.jClass.getDetail().addMethod(method);
			this.parser.debug("visitMethod: method type = " + obj);
		}
	}

	@Override
	public void visitConstantUtf8(ConstantUtf8 obj) {
		this.parser.debug("visitConstantUtf8: obj.getBytes(this.cp) = " + obj.getBytes());
		if (obj.getBytes().equals("Ljavax/persistence/Table;")) {
			Constant[] constants = cp.getConstantPool();
			// 计算索引
			int index;
			for (index = 1; index < constants.length; index++) {
				if (constants[index] != null && constants[index].equals(obj)) {
					break;
				}
			}
			// 搜索tableName
			String tableName = null;
			while (++index < constants.length && constants[index] instanceof ConstantUtf8) {
				tableName = ((ConstantUtf8) constants[index]).getBytes();
				if (!tableName.equals("name")) {
					break;
				}
			}
			if (tableName != null) {
				this.jClass.getDetail().addTable(new TableInfo(tableName, TableInfo.Define));
			}

		} else if (SqlParserUtil.isSQL(obj.getBytes())) {// 处理Annotation
			List<TableInfo> tables = SqlParserUtil.parserSql(obj.getBytes());
			if (tables != null) {
				for (TableInfo table : tables) {
					this.jClass.getDetail().addTable(table);
				}
			}
		}
	}

	public void setParser(AbstractParser parser) {
		this.parser = parser;
	}

	private List<TableInfo> ParseTable(String constant) {
		if (constant != null) {
			if (SqlParserUtil.isSQL(constant)) {
				return SqlParserUtil.parserSql(constant);
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

	private void internalSearchForMethod(jdepend.model.Method method, org.apache.bcel.classfile.Method obj,
			PackageFilter filter) {

		InvokeItem item;
		Code codeType = obj.getCode();
		if (codeType != null) {
			byte[] code = codeType.getCode();
			ByteSequence stream = new ByteSequence(code);
			String info;
			String[] infos;
			int pos;
			String calledName;
			String calledPackageName;
			String calledMethod;
			String callType;
			int index;
			String fieldName;

			try {
				while (stream.available() > 0) {
					info = Utility.codeToString(stream, obj.getConstantPool(), true);

					if (info.startsWith("invoke")) {
						infos = info.split("\\s+");
						if (infos.length > 1) {
							pos = infos[1].lastIndexOf('.');
							if (pos != -1) {
								callType = infos[0].substring(6);
								calledName = infos[1].substring(0, pos);
								calledMethod = infos[1].substring(pos + 1);
								// 得到包名
								index = calledName.lastIndexOf(".");
								if (index > 0) {
									calledPackageName = calledName.substring(0, index);
								} else {
									calledPackageName = JavaPackage.Default;
								}
								if (filter.accept(calledPackageName)) {
									item = new InvokeItem(callType, calledName, calledMethod, infos[2]);
									method.addInvokeItem(item);
								}
							}
						}
					} else if (info.startsWith("getfield")) {
						fieldName = this.getFieldName(info);
						if (fieldName != null) {
							method.addReadField(fieldName);
						}
					} else if (info.startsWith("putfield")) {
						fieldName = this.getFieldName(info);
						if (fieldName != null) {
							method.addWriteField(fieldName);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private String getFieldName(String info) {
		String[] infos = info.split("\\s+");
		if (infos.length > 1) {
			int pos = infos[1].lastIndexOf('.');
			if (pos != -1) {
				return infos[1].substring(pos + 1);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private int calLineCount(org.apache.bcel.classfile.Method method) {
		LineNumberTable lt = method.getLineNumberTable();
		if (lt != null) {
			if (lt.getTableLength() == 1) {
				return 1;
			} else {
				return lt.getLineNumberTable()[lt.getLineNumberTable().length - 1].getLineNumber()
						- lt.getLineNumberTable()[0].getLineNumber();
			}
		} else {
			return 0;
		}
	}

	private boolean searchTransactionalAnnotation(Attribute[] attributes, ConstantPool constantPool) {

		boolean isIncludeTransactionalAnnotation = false;
		org.apache.bcel.classfile.Constant constant;
		Unknown unknown;
		String name;
		int indexInt;
		L: for (org.apache.bcel.classfile.Attribute attribute : attributes) {
			if (attribute instanceof Unknown) {
				unknown = (Unknown) attribute;
				if (unknown.getName().equals("RuntimeVisibleAnnotations")) {
					for (byte index : unknown.getBytes()) {
						indexInt = (int) index;
						if (indexInt != 0 && indexInt != 1 && indexInt < constantPool.getLength()) {
							if (indexInt < 0) {
								indexInt = 256 + indexInt;
							}
							constant = constantPool.getConstant(indexInt);
							if (constant != null && constant instanceof ConstantUtf8) {
								name = ((ConstantUtf8) constant).getBytes();
								if (name != null
										&& name.indexOf("org/springframework/transaction/annotation/Transactional") != -1) {
									isIncludeTransactionalAnnotation = true;
									break L;
								}
							}
						}
					}
				}

			}
		}
		return isIncludeTransactionalAnnotation;
	}
}
