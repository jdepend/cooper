package jdepend.knowledge.architectpattern;

public abstract class AbstractArchitectPatternIdentifyer implements ArchitectPatternIdentifyer {

	private ArchitectPatternWorker worker;

	public ArchitectPatternWorker getWorker() {
		return worker;
	}

	public void setWorker(ArchitectPatternWorker worker) {
		this.worker = worker;
	}

}
