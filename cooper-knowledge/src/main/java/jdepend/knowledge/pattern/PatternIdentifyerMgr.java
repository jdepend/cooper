package jdepend.knowledge.pattern;

import java.util.Collection;
import java.util.Map;

import jdepend.model.result.AnalysisResult;

public interface PatternIdentifyerMgr {

	public Map<String, Collection<PatternInfo>> identify(AnalysisResult result);

	public String getExplain();
}
