package jdepend.metadata.annotation;

import java.io.Serializable;

public class AnnotationRefs implements Serializable {

	private static final long serialVersionUID = -3399277906618193523L;

	private Autowired autowired;

	private Qualifier qualifier;

	public Autowired getAutowired() {
		return autowired;
	}

	public void setAutowired(Autowired autowired) {
		this.autowired = (Autowired) AnnotationMgr.getInstance().getType(autowired);
	}

	public Qualifier getQualifier() {
		return qualifier;
	}

	public void setQualifier(Qualifier qualifier) {
		this.qualifier = (Qualifier) AnnotationMgr.getInstance().getType(qualifier);
	}
}
