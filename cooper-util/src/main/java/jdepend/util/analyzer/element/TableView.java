package jdepend.util.analyzer.element;

import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.TableViewInfo;
import jdepend.model.util.TableViewUtil;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public final class TableView extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4983515130981308482L;

	public TableView() {
		super("Table浏览", Analyzer.Attention, "Table浏览");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		for (TableViewInfo info : TableViewUtil.view(result)) {
			this.printTable("表名", info.name);
			this.printTable("出现次数", info.count);
			this.printTable("操作", info.type);
			this.printTable("所属组件", info.Component);
			this.printTable("类名", info.javaClass);
		}

	}
}
