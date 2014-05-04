package jdepend.util.refactor;

import java.util.Collection;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClass;

public interface RefactorTool {

	public void moveClass(Collection<JavaClass> javaClasses, Component target) throws JDependException;

	public void uniteComponent(String name, int layer, Collection<String> components) throws JDependException;

	public void createComponent(String componentName, int componentLayer) throws JDependException;

	public void deleteComponent(String componentName) throws JDependException;

	public void clear() throws JDependException;

}
