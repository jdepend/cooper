package jdepend.service.avertcheat.framework;

import java.util.ArrayList;
import java.util.List;

import jdepend.service.avertcheat.abstractClassQualificationConfirmer.AbstractClassQualificationConfirmer;
import jdepend.service.avertcheat.stabilityClassIdentifyer.StabilityClassIdentifyer;

public final class AvertCheatMgr {

	private static AvertCheatMgr inst = new AvertCheatMgr();

	private List<AvertCheat> avertCheats;

	public AvertCheatMgr() {
		this.init();
	}

	public static AvertCheatMgr getInstance() {
		return inst;
	}

	private void init() {

		avertCheats = new ArrayList<AvertCheat>();

		avertCheats.add(new StabilityClassIdentifyer());
		avertCheats.add(new AbstractClassQualificationConfirmer());
	}

	public List<AvertCheat> getAvertCheats() {
		return this.avertCheats;
	}
}
