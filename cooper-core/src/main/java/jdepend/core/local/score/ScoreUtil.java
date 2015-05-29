package jdepend.core.local.score;

import jdepend.framework.exception.JDependException;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;

public final class ScoreUtil {

	public final static String ScoreAndResult = "ScoreAndResult";
	public final static String OnlyScoreMode = "OnlyScoreMode";

	public static void saveScore(AnalysisResult result, String mode) throws JDependException {
		ScoreInfo score = new ScoreInfo();

		score.group = result.getRunningContext().getGroup();
		score.command = result.getRunningContext().getCommand();
		score.lc = result.getSummary().getLineCount();
		score.componentCount = result.getSummary().getComponentCount();
		score.relationCount = result.getSummary().getRelationCount();
		score.score = result.getScore();
		score.distance = result.getDistance();
		score.balance = result.getBalance();
		score.relation = result.getRelationRationality();
		score.encapsulation = result.getEncapsulation();
		score.cohesion = result.getSummary().getCohesion();
		score.coupling = result.getSummary().getCoupling();

		if (ScoreAndResult.equals(mode)) {
			ScoreRepository.save(score, result);
		} else {
			ScoreRepository.save(score);
		}
	}

	public static void saveScore(String mode) throws JDependException {
		saveScore(JDependUnitMgr.getInstance().getResult(), mode);
	}

}
