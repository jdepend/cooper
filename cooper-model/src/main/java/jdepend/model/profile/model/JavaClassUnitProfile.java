package jdepend.model.profile.model;

import java.io.Serializable;

public class JavaClassUnitProfile implements Serializable {

	private static final long serialVersionUID = -4336618115325941714L;

	private boolean abstractClassQualificationConfirmer;

	private String abstractClassRule;

	private String stableRule;

	public boolean isAbstractClassQualificationConfirmer() {
		return abstractClassQualificationConfirmer;
	}

	public void setAbstractClassQualificationConfirmer(boolean abstractClassQualificationConfirmer) {
		this.abstractClassQualificationConfirmer = abstractClassQualificationConfirmer;
	}

	public String getAbstractClassRule() {
		return abstractClassRule;
	}

	public void setAbstractClassRule(String abstractClassRule) {
		this.abstractClassRule = abstractClassRule;
	}

	public String getStableRule() {
		return stableRule;
	}

	public void setStableRule(String stableRule) {
		this.stableRule = stableRule;
	}
}
