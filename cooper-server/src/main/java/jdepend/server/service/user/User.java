package jdepend.server.service.user;

import jdepend.framework.exception.JDependException;

public interface User {

	public abstract String getName();

	public abstract String getDept();

	public abstract Integer getIntegral();

	public abstract void setIntegral(Integer integral);

	public abstract void changeIntegral(Integer change);

	public abstract boolean isValid();

	public abstract void save() throws JDependException;

}