package jdepend.parse.impl;

import java.io.InputStream;

import jdepend.model.JavaClassUnit;
import jdepend.parse.ParseConfigurator;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.DescendingVisitor;

public class BCELClassFileParser extends AbstractParser {

	public BCELClassFileParser(ParseConfigurator conf) {
		super(conf);
	}

	@Override
	protected JavaClassUnit doParse(String place, InputStream is) throws ParseJDependException {

		JavaClassUnit jClass = null;
		try {

			ClassParser parser = new ClassParser(is, null);

			org.apache.bcel.classfile.JavaClass javaClass = parser.parse();

			jClass = new JavaClassUnit("Unknown", true, javaClass.getAccessFlags());
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
