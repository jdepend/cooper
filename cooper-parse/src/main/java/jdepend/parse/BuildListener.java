package jdepend.parse;

import java.util.Collection;

import jdepend.metadata.JavaClass;

/**
 * The <code>BuildListener</code> interface defines a listener notified upon the
 * completion of building events.
 * <p>
 * Implementers of this interface register for notification using the
 * <code>JDepend.addBuildListener()</code> method.
 * 
 * @author <b>Abner</b>
 * 
 */

public interface BuildListener {
	/**
	 * 当 JavaClass 全部解析完后触发的事件
	 * 
	 * @param parsedClasses
	 */
	public void onBuildJavaClasses(Collection<JavaClass> javaClasses);

}
