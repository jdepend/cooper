package jdepend.parse.impl;

import java.io.InputStream;

import jdepend.model.JavaClass;
import jdepend.parse.ParseJDependException;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.DescendingVisitor;

public class BCELClassFileParser extends AbstractParser {

	private Constant[] constantPool;

	public BCELClassFileParser(PackageFilter filter) {
		super(filter);
//		Attribute.addAttributeReader(name, r)
	}

	@Override
	protected JavaClass doParse(InputStream is) throws ParseJDependException {

		JavaClass jClass = null;
		try {

			ClassParser parser = new ClassParser(is, null);

			org.apache.bcel.classfile.JavaClass javaClass = parser.parse();

			jClass = new JavaClass("Unknown", true, javaClass.getAccessFlags());

			JDependClassFileVisitor visitor = new JDependClassFileVisitor(jClass);
			visitor.setParser(this);

			DescendingVisitor dvisitor = new DescendingVisitor(javaClass, visitor);

			dvisitor.visit();

			// this.createConstantPool(visitor);

			this.debug("");

			this.getWriter().flush();

			return jClass;
		} catch (Exception e) {
			if (jClass != null) {
				throw new ParseJDependException(jClass.getName(), e);
			} else {
				throw new ParseJDependException(e);
			}
		}
	}

	private void createConstantPool(JDependClassFileVisitor visitor) {

		ConstantPool cp = visitor.getConstantPool();

		org.apache.bcel.classfile.Constant[] constants = visitor.getConstantPool().getConstantPool();

		constantPool = new Constant[constants.length];

		for (int i = 0; i < constants.length; i++) {

			org.apache.bcel.classfile.Constant constant = constants[i];

			if (constant == null)
				continue;

			byte tag = constant.getTag();

			switch (tag) {

			case (AbstractParser.CONSTANT_CLASS):
				constantPool[i] = new Constant(tag, ((ConstantClass) constant).getNameIndex());
				continue;
			case (AbstractParser.CONSTANT_STRING):
				constantPool[i] = new Constant(tag, ((ConstantString) constant).getStringIndex());
				continue;
			case (AbstractParser.CONSTANT_FIELD):
				constantPool[i] = new Constant(tag, ((ConstantFieldref) constant).getClassIndex(),
						((ConstantFieldref) constant).getNameAndTypeIndex());
				continue;
			case (AbstractParser.CONSTANT_METHOD):
				constantPool[i] = new Constant(tag, ((ConstantMethodref) constant).getClassIndex(),
						((ConstantMethodref) constant).getNameAndTypeIndex());
				continue;
			case (AbstractParser.CONSTANT_INTERFACEMETHOD):
				constantPool[i] = new Constant(tag, ((ConstantInterfaceMethodref) constant).getClassIndex(),
						((ConstantInterfaceMethodref) constant).getNameAndTypeIndex());
				continue;
			case (AbstractParser.CONSTANT_NAMEANDTYPE):
				constantPool[i] = new Constant(tag, ((ConstantNameAndType) constant).getNameIndex(),
						((ConstantNameAndType) constant).getSignatureIndex());
				continue;
			case (AbstractParser.CONSTANT_INTEGER):
				constantPool[i] = new Constant(tag, ((ConstantInteger) constant).getConstantValue(cp));
				continue;
			case (AbstractParser.CONSTANT_FLOAT):
				constantPool[i] = new Constant(tag, ((ConstantFloat) constant).getConstantValue(cp));
				continue;
			case (AbstractParser.CONSTANT_LONG):
				constantPool[i] = new Constant(tag, ((ConstantLong) constant).getConstantValue(cp));
				continue;
			case (AbstractParser.CONSTANT_DOUBLE):
				constantPool[i] = new Constant(tag, ((ConstantDouble) constant).getConstantValue(cp));
				continue;
			case (AbstractParser.CONSTANT_UTF8):
				constantPool[i] = new Constant(tag, ((ConstantUtf8) constant).getBytes());
				continue;
			}
		}
	}

	@Override
	public Constant[] getConstantPool() {
		throw new RuntimeException("该方法未提供实现.");
		// return constantPool;
	}
}
