package jdepend.knowledge.pattern.impl1;

import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.JavaClass;

public interface PatternIdentifyer {

	public Collection<PatternInfo> identify(Collection<JavaClass> javaClasses);

	public String getExplain();

}
