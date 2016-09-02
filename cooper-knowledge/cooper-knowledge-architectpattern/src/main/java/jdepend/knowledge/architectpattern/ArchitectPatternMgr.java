package jdepend.knowledge.architectpattern;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.knowledge.architectpattern.identifyer.CommonalityLayerIdentifyer;
import jdepend.knowledge.architectpattern.identifyer.CoreComponentIdentifyer;
import jdepend.model.result.AnalysisResult;

public class ArchitectPatternMgr {

	private List<ArchitectPatternIdentifyer> identifyers;

	private static ArchitectPatternMgr mgr = new ArchitectPatternMgr();

	private ArchitectPatternMgr() {
		this.identifyers = new ArrayList<ArchitectPatternIdentifyer>();
		this.identifyers.add(new CoreComponentIdentifyer());
		this.identifyers.add(new CommonalityLayerIdentifyer());
	}

	public static ArchitectPatternMgr getInstance() {
		return mgr;
	}

	public ArchitectPatternResult identify(AnalysisResult result) throws JDependException {
		ArchitectPatternWorker worker = new ArchitectPatternWorker();

		for (ArchitectPatternIdentifyer identifyer : this.identifyers) {
			identifyer.setWorker(worker);
			identifyer.identify(result);
		}

		return new ArchitectPatternResult(worker.getCores(), worker.getLayers());
	}

}
