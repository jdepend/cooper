package jdepend.knowledge;

public final class ExpertFactory {

	public Expert createExpert() {
		return new DefaultExpert();
	}

}
