package jdepend.parse.impl;

import java.io.InputStream;

import jdepend.metadata.JavaClass;
import jdepend.parse.ParseConfigurator;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.DescendingVisitor;

public class BCELClassFileParser extends AbstractParser {

	public BCELClassFileParser(ParseConfigurator conf) {
		super(conf);
	}

	@Override
	protected JavaClass doParse(String place, InputStream is, String model) throws ParseClassException {

		JavaClass jClass = null;
		try {

			ClassParser parser = new ClassParser(is, null);

			org.apache.bcel.classfile.JavaClass javaClass = parser.parse();

			jClass = new JavaClass("Unknown", true, javaClass.getAccessFlags());
			jClass.setPlace(place);

			JDependClassFileVisitor visitor = this.createVisitor(model);
			visitor.setJavaClass(jClass);
			visitor.setParser(this);

			DescendingVisitor dvisitor = new DescendingVisitor(javaClass, visitor);

			dvisitor.visit();

			visitor.calImportedPackages();

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

	private JDependClassFileVisitor createVisitor(String model) {
		if (model.equals(Model_Big)) {
			return new BigClassFileVisitor();
		} else {
			return new SmallClassFileVisitor();
		}
	}

}
