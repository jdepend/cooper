package jdepend.parse;

import jdepend.metadata.JavaClass;

/**
 * The <code>ParserListener</code> interface defines a listener notified upon
 * the completion of parsing events.
 * <p>
 * Implementers of this interface register for notification using the
 * <code>JDepend.addParseListener()</code> method.
 * 
 * @author <b>Abner</b>
 * 
 */

public interface ParseListener {

	/**
	 * Called whenever a Java class file is parsed into the specified
	 * <code>JavaClass</code> instance.
	 * 
	 * @param parsedClass
	 *            Parsed Java class.
	 */
	public void onParsedJavaClass(JavaClass parsedClass, int process);
}
