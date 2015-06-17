package jdepend.metadata.annotation;

import java.io.Serializable;

import jdepend.metadata.RequestMapping;

public class Annotations implements Serializable {

	private static final long serialVersionUID = 8673294114544812064L;

	private boolean isIncludeTransactionalAnnotation = false;

	private RequestMapping requestMapping;

	public boolean isIncludeTransactionalAnnotation() {
		return isIncludeTransactionalAnnotation;
	}

	public void setIncludeTransactionalAnnotation(boolean isIncludeTransactionalAnnotation) {
		this.isIncludeTransactionalAnnotation = isIncludeTransactionalAnnotation;
	}

	public RequestMapping getRequestMapping() {
		return requestMapping;
	}

	public void setRequestMapping(RequestMapping requestMapping) {
		this.requestMapping = requestMapping;
	}
}
