package jdepend.parse.impl;

import java.io.InputStream;

import jdepend.model.JavaClass;
import jdepend.parse.ParseConfigurator;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.DescendingVisitor;

public class BCELClassFileParser extends AbstractParser {

	public BCELClassFileParser(ParseConfigurator conf) {
		super(conf);
	}

	@Override
	protected JavaClass doParse(String place, InputStream is) throws ParseJDependException {

		JavaClass jClass = null;
		try {

			ClassParser parser = new ClassParser(is, null);

			org.apache.bcel.classfile.JavaClass javaClass = parser.parse();

			jClass = new JavaClass("Unknown", true, javaClass.getAccessFlags());
			jClass.setPlace(place);

			JDependClassFileVisitor visitor = new JDependClassFileVisitor(jClass);
			visitor.setParser(this);

			DescendingVisitor dvisitor = new DescendingVisitor(javaClass, visitor);

			dvisitor.visit();
			
			jClass.calImportedPackages();

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
}
