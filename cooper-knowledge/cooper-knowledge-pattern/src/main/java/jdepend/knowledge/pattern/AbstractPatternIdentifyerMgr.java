package jdepend.knowledge.pattern;

import java.util.Collection;
import java.util.Map;

import jdepend.framework.log.LogUtil;
import jdepend.model.result.AnalysisResult;

public abstract class AbstractPatternIdentifyerMgr implements PatternIdentifyerMgr {

	@Override
	public final Map<String, Collection<PatternInfo>> identify(AnalysisResult result) {
		long start = System.currentTimeMillis();
		Map<String, Collection<PatternInfo>> patternInfos = this.doIdentify(result);
		LogUtil.getInstance(AbstractPatternIdentifyerMgr.class).systemLog(
				"PatternIdentify:" + (System.currentTimeMillis() - start));
		return patternInfos;
	}

	protected abstract Map<String, Collection<PatternInfo>> doIdentify(AnalysisResult result);

}
