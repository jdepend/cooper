package jdepend.util.todolist;

import java.util.List;

public interface TODOIdentifyer {

	public List<TODOItem> identify(TODOIdentifyInfo info) throws TODOListException;

}
