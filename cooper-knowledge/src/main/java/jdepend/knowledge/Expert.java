package jdepend.knowledge;

import jdepend.framework.exception.JDependException;

public interface Expert {

	public AdviseInfo advise(Structure structure) throws JDependException;

}
