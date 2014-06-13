package jdepend.parse.impl;

import java.lang.reflect.Constructor;

import jdepend.parse.ParseConfigurator;

public class ClassFileParserFactory {

	public ClassFileParserFactory() {
	}

	public AbstractParser createParser(ParseConfigurator conf) {
		AbstractParser parser = null;
		String parserName = conf.getClassFileParser();
		if (parserName == null) {
			return new BCELClassFileParser(conf);
		}
		try {
			Constructor c = this.getClass().getClassLoader().loadClass(parserName)
					.getConstructor(new Class[] { PackageFilter.class });
			parser = (AbstractParser) c.newInstance(new Object[] { conf });
		} catch (Exception e) {
			e.printStackTrace();
			parser = new BCELClassFileParser(conf);
		}

		return parser;
	}
}
