package jdepend.parse.util;

import java.io.InputStream;

import jdepend.metadata.JavaClass;
import jdepend.parse.ParseConfigurator;
import jdepend.parse.impl.BCELClassFileParser;
import jdepend.parse.impl.ParseClassException;

public final class ParseClassUtil {

	public static JavaClass parse(String place, InputStream is) throws ParseClassException {
		ParseConfigurator conf = new ParseConfigurator();
		BCELClassFileParser parser = new BCELClassFileParser(conf);
		return parser.parse(place, is);
	}

}
