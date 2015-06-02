package jdepend.util.analyzer.framework;

import jdepend.framework.domain.PersistentBean;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.TableCallBack;
import jdepend.metadata.tree.Node;
import jdepend.model.result.AnalysisResult;

public abstract class AbstractAnalyzer extends PersistentBean implements Analyzer {

	private transient AnalyzerWorker worker;

	private String type;

	private int heat;

	private transient boolean isChicked = false;

	private transient AnalyzerExecutorListener listener;

	public AbstractAnalyzer() {
		super();
		this.setWorker(new DefaultAnalyzerWorker());
	}

	public AbstractAnalyzer(String name, String type, String tip) {
		super(name, tip, "analyzerData");
		this.type = type;
		this.setWorker(new DefaultAnalyzerWorker());
	}

	public void search(final AnalysisResult result) throws AnalyzerException {
		this.heat++;
		this.isChicked = true;

		Long start = System.currentTimeMillis();
		doSearch(result);
		LogUtil.getInstance(this.getClass()).systemLog(
				"分析器[" + this.getName() + "]执行用时：" + (System.currentTimeMillis() - start));

	}

	public int getMaxProgress(AnalysisResult result) {
		return 0;
	}

	protected void progress() {
		if (this.listener != null) {
			listener.onExecute(AbstractAnalyzer.this);
		}
	}

	public void setListener(AnalyzerExecutorListener listener) {
		this.listener = listener;
	}

	protected abstract void doSearch(AnalysisResult result) throws AnalyzerException;

	public boolean needSave() {
		return this.isChicked;
	}

	@Override
	public void init() throws AnalyzerException {

	}

	@Override
	public void release() throws AnalyzerException {
	}

	public void setWorker(AnalyzerWorker worker) {
		this.worker = worker;
		this.worker.setAnalyzer(this);
	}

	protected void print(String info) {
		this.worker.setInfo(info);
	}

	public void isPrintTab(boolean tab) {
		this.worker.isPrintTab(tab);
	}

	protected void printTab() {
		this.print("    ");
	}

	protected void printTab(int n) {
		for (int i = 0; i < n; i++) {
			printTab();
		}
	}

	protected void printTable(String key, Object info) {
		this.worker.setTableData(key, info);
	}

	protected void addTableCallBack(TableCallBack callBack) {
		this.worker.addTableCallBack(callBack);
	}

	protected void printTree(Node root) {
		this.worker.setTree(root);
	}

	protected void printGraphData(GraphData graphData) {
		this.worker.setGraphData(graphData);
	}

	public String getExplain() {
		return null;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getHeat() {
		return heat;
	}

	public void setHeat(int heat) {
		this.heat = heat;
	}

	@Override
	public int compareTo(Analyzer o) {
		int rtn = this.type.compareTo(o.getType());
		if (rtn != 0) {
			return rtn;
		} else {
			return new Integer(o.getHeat()).compareTo(this.heat);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractAnalyzer other = (AbstractAnalyzer) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
