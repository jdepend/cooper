package jdepend.client.core.remote.analyzer;

final class AnalyzerClassLoader extends ClassLoader {

	private byte[] def;

	public AnalyzerClassLoader(byte[] def) {
		this.def = def;
	}

	public AnalyzerClassLoader(ClassLoader parent, byte[] def) {
		super(parent);
		this.def = def;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class theClass = defineClass(name, def, 0, def.length);
		if (theClass == null)
			throw new ClassFormatError();
		return theClass;

	}

}
