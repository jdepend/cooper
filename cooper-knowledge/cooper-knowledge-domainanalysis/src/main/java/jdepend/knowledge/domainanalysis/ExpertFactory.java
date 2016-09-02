package jdepend.knowledge.domainanalysis;

public final class ExpertFactory {

	public Expert createExpert() {
		return new DefaultExpert();
	}

}
