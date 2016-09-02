package jdepend.knowledge.pattern.impl1;

import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.JavaClassUnit;

public interface PatternIdentifyer {

	public Collection<PatternInfo> identify(Collection<JavaClassUnit> javaClasses);

	public String getExplain();

}
