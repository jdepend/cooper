package jdepend.util.refactor;

import java.util.Collection;

import jdepend.model.Component;
import jdepend.model.JavaClassUnit;

public interface RefactorTool {

	public void moveClass(Collection<JavaClassUnit> javaClasses, Component target) throws RefactorException;

	public void uniteComponent(String name, int layer, Collection<String> components) throws RefactorException;

	public void createComponent(String componentName, int componentLayer) throws RefactorException;

	public void deleteComponent(String componentName) throws RefactorException;

}
