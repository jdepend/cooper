package jdepend.parse.util;

import java.io.InputStream;

import jdepend.model.JavaClassUnit;
import jdepend.parse.ParseConfigurator;
import jdepend.parse.impl.BCELClassFileParser;
import jdepend.parse.impl.ParseJDependException;

public final class ParseClassUtil {

	public static JavaClassUnit parse(String place, InputStream is) throws ParseJDependException {
		ParseConfigurator conf = new ParseConfigurator();
		BCELClassFileParser parser = new BCELClassFileParser(conf);
		return parser.parse(place, is);
	}

}
