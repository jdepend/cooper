package jdepend.knowledge.pattern.impl2;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.JavaClass;

public abstract class AbstractFeature implements Feature {

	private List<Identifyer> identifyers;

	private JavaClass current;

	private String patternInfo;

	public AbstractFeature() {
		this.identifyers = new ArrayList<Identifyer>();
	}

	public void addIdentifyer(Identifyer identifyer) {
		if (!this.identifyers.contains(identifyer)) {
			this.identifyers.add(identifyer);
			identifyer.registFeature(this.getName());
		}
	}

	public void check(FeatureCheckContext context) {

		this.current = context.getCurrent();
		if (this.have(context)) {
			this.onHave();
		}
		this.clear();
	}

	protected abstract boolean have(FeatureCheckContext context);

	private void onHave() {
		for (Identifyer identifyer : this.identifyers) {
			identifyer.accumulate(this);
		}
	}

	public void clear() {
		this.patternInfo = null;
	}

	@Override
	public JavaClass getCurrent() {
		return this.current;
	}

	public String getPatternInfo() {
		return patternInfo;
	}

	public void setPatternInfo(String patternInfo) {
		this.patternInfo = patternInfo;
	}

}
