package jdepend.parse.util;

import java.io.InputStream;

import jdepend.model.JavaClass;
import jdepend.parse.ParseJDependException;
import jdepend.parse.impl.BCELClassFileParser;
import jdepend.parse.impl.PackageFilter;
import jdepend.parse.impl.ParseConfigurator;

public final class ParseClassUtil {

	public static JavaClass parse(InputStream is) throws ParseJDependException {
		ParseConfigurator conf = new ParseConfigurator();
		PackageFilter filter = new PackageFilter(conf.getFilteredPackages(), conf.getNotFilteredPackages());
		BCELClassFileParser parser = new BCELClassFileParser(filter);
		return parser.parse(is);
	}

}
