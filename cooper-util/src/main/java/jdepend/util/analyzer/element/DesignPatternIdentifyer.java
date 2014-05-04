package jdepend.util.analyzer.element;

import java.util.Collection;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.knowledge.pattern.PatternIdentifyerMgr;
import jdepend.knowledge.pattern.PatternIdentifyerMgrFactory;
import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;

public final class DesignPatternIdentifyer extends AbstractAnalyzer {

	private static final long serialVersionUID = -8962600511528065011L;

	private transient PatternIdentifyerMgr mgr;

	public DesignPatternIdentifyer() {
		super("设计模式识别器", Attention, "识别设计模式");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		StringBuilder classInfos;
		Collection<PatternInfo> items;
		Map<String, Collection<PatternInfo>> patterns = this.getIdentifyerMgr().identify(result);
		for (String identifyerName : patterns.keySet()) {
			this.printTable("模式名称", identifyerName);
			items = patterns.get(identifyerName);
			if (items != null && items.size() > 0) {
				classInfos = new StringBuilder(1000);
				for (PatternInfo item : items) {
					classInfos.append(item);
					classInfos.append("\n");
				}
				classInfos.delete(classInfos.length() - 1, classInfos.length());
				this.printTable("类列表", classInfos.toString());
			} else {
				this.printTable("类列表", "无");
			}
		}
	}

	private PatternIdentifyerMgr getIdentifyerMgr() {
		if (this.mgr == null) {
			this.mgr = new PatternIdentifyerMgrFactory().create();
		}
		return this.mgr;
	}

	@Override
	public String getExplain() {
		return this.getIdentifyerMgr().getExplain();
	}
}
