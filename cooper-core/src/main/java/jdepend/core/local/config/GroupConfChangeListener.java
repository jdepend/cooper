package jdepend.core.local.config;

import jdepend.framework.exception.JDependException;

public interface GroupConfChangeListener {

	public void onRefresh() throws JDependException;

	public void onDelete(String group) throws JDependException;

	public void onCreate(String group) throws JDependException;

	public void onUpdate(String group) throws JDependException;

}
