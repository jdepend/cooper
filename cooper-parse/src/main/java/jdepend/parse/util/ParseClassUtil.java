package jdepend.parse.util;

import java.io.InputStream;

import jdepend.model.JavaClass;
import jdepend.parse.ParseJDependException;
import jdepend.parse.impl.BCELClassFileParser;
import jdepend.parse.impl.ParseConfigurator;

public final class ParseClassUtil {

	public static JavaClass parse(InputStream is) throws ParseJDependException {
		ParseConfigurator conf = new ParseConfigurator();
		BCELClassFileParser parser = new BCELClassFileParser(conf);
		return parser.parse(is);
	}

}
