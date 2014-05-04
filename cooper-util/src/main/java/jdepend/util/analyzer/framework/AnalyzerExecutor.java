package jdepend.util.analyzer.framework;

import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;

public class AnalyzerExecutor {

	private Analyzer analyzer;

	public AnalyzerExecutor(Analyzer analyzer) {
		super();
		this.analyzer = analyzer;
	}

	public AnalyzerResult execute(AnalysisResult result) throws JDependException {

		AnalyzerWorker worker = new DefaultAnalyzerWorker();

		analyzer.setWorker(worker);
		analyzer.search(result);

		return new AnalyzerResult(worker.getInfo(), worker.getTableData(), worker.getTree(), worker.getGraphData());
	}

}
