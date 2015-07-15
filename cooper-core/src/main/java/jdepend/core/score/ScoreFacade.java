package jdepend.core.score;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;

public final class ScoreFacade {

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

	public static List<ScoreInfo> getScoreList() throws JDependException {
		return ScoreRepository.getScoreList();
	}

	public static List<ScoreInfo> getScoreList(Date begin) throws JDependException {
		return ScoreRepository.getScoreList(begin);
	}

	public static AnalysisResult getTheResult(String id) throws JDependException {
		return ScoreRepository.getTheResult(ScoreRepository.getTheScoreInfo(id));
	}

	public static AnalysisResult getTheResult(ScoreInfo scoreInfo) throws JDependException {
		return ScoreRepository.getTheResult(scoreInfo);
	}

	public static void delete(String id) throws JDependException {
		ScoreRepository.delete(id);
	}

	public static void sort(List<ScoreInfo> scorelist, String itemName) {
		Collections.sort(scorelist, new ScoreByItemComparator(itemName));
	}

}
