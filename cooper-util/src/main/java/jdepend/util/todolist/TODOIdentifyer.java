package jdepend.util.todolist;

import java.util.List;

import jdepend.framework.exception.JDependException;

public interface TODOIdentifyer {

	public List<TODOItem> identify(TODOIdentifyInfo info) throws JDependException;

}
