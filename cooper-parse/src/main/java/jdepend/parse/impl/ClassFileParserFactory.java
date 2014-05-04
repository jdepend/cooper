package jdepend.parse.impl;

import java.lang.reflect.Constructor;


public class ClassFileParserFactory {
	
	public ClassFileParserFactory() {
	}

	public AbstractParser createParser(ParseConfigurator conf) {
		AbstractParser parser = null;
		String parserName = conf.getClassFileParser();
		if (parserName == null) {
			parserName = BCELClassFileParser.class.getName();
		}
		try {
			Constructor c = this.getClass().getClassLoader().loadClass(parserName)
					.getConstructor(new Class[] { PackageFilter.class });
			parser = (AbstractParser) c.newInstance(new Object[] { new PackageFilter(conf.getFilteredPackages(), conf
					.getNotFilteredPackages()) });
		} catch (Exception e) {
			e.printStackTrace();
			parser = new BCELClassFileParser(new PackageFilter(conf.getFilteredPackages(),
					conf.getNotFilteredPackages()));
		}

		return parser;
	}
}
