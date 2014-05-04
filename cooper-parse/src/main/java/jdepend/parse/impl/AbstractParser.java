package jdepend.parse.impl;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import jdepend.model.JavaClass;
import jdepend.parse.ParseJDependException;
import jdepend.parse.ParseListener;

/**
 * The <code>AbstractParser</code> class is the base class for classes capable
 * of parsing files to create a <code>JavaClass</code> instance.
 * 
 * @author <b>Abner</b>
 * 
 */

public abstract class AbstractParser {

	public static final int CONSTANT_UTF8 = 1;
	public static final int CONSTANT_UNICODE = 2;
	public static final int CONSTANT_INTEGER = 3;
	public static final int CONSTANT_FLOAT = 4;
	public static final int CONSTANT_LONG = 5;
	public static final int CONSTANT_DOUBLE = 6;
	public static final int CONSTANT_CLASS = 7;
	public static final int CONSTANT_STRING = 8;
	public static final int CONSTANT_FIELD = 9;
	public static final int CONSTANT_METHOD = 10;
	public static final int CONSTANT_INTERFACEMETHOD = 11;
	public static final int CONSTANT_NAMEANDTYPE = 12;

	private ArrayList<ParseListener> parseListeners;
	private PackageFilter filter;
	public static boolean DEBUG = (new ParseConfigurator()).getParseDebug();

	private PrintWriter writer = new PrintWriter(System.err);

	/**
	 * 设置日志输出的对象
	 * 
	 * @param writer
	 */
	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public AbstractParser() {
		parseListeners = new ArrayList<ParseListener>();
	}

	public AbstractParser(PackageFilter filter) {
		this.filter = filter;
		parseListeners = new ArrayList<ParseListener>();
	}

	public void addParseListener(ParseListener listener) {
		parseListeners.add(listener);
	}

	/**
	 * Registered parser listeners are informed that the resulting
	 * <code>JavaClass</code> was parsed.
	 */
	public final JavaClass parse(InputStream is) throws ParseJDependException {
		JavaClass jClass = null;
		try {
			jClass = this.doParse(is);
			return jClass;
		} finally {
			this.onParsedJavaClass(jClass);
		}
	}

	protected abstract JavaClass doParse(InputStream is) throws ParseJDependException;

	public abstract Constant[] getConstantPool();

	/**
	 * Informs registered parser listeners that the specified
	 * <code>JavaClass</code> was parsed.
	 * 
	 * @param jClass
	 *            Parsed Java class.
	 */
	protected void onParsedJavaClass(JavaClass jClass) {
		for (ParseListener listener : parseListeners) {
			listener.onParsedJavaClass(jClass, 1);
		}
	}

	public PackageFilter getFilter() {
		return filter;
	}

	public void setFilter(PackageFilter filter) {
		this.filter = filter;
	}

	protected void debug(String message) {
		if (DEBUG) {
			this.writer.println(message);
		}
	}
}
