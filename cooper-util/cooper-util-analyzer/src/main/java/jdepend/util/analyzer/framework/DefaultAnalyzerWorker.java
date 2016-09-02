package jdepend.util.analyzer.framework;

import java.util.List;

import jdepend.metadata.tree.Node;

public final class DefaultAnalyzerWorker implements AnalyzerWorker {

	private Analyzer analyzer;

	private StringBuilder info = new StringBuilder();

	private TwoDimensionData tableData = new TwoDimensionData();

	private Node tree;

	private List<GraphItemData> items;

	private boolean isPrintTab = true;

	@Override
	public String getInfo() {
		if (this.info.length() == 0) {
			return null;
		} else {
			StringBuilder rtnInfo = new StringBuilder(100);
			rtnInfo.append(this.analyzer.getName());
			rtnInfo.append("：\n");
			rtnInfo.append(info);
			return rtnInfo.toString();
		}
	}

	@Override
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	@Override
	public void setInfo(String info) {
		if (this.isPrintTab) {
			this.info.append(tab());
		}
		this.info.append(info);
	}

	private String tab() {
		return "	";
	}

	@Override
	public void isPrintTab(boolean tab) {
		isPrintTab = tab;
	}

	@Override
	public TwoDimensionData getTwoDimensionData() {
		return tableData;
	}

	@Override
	public void setTwoDimensionData(String key, Object value) {
		tableData.setData(key, value);
	}

	@Override
	public Node getTree() {
		return tree;
	}

	@Override
	public void setTree(Node tree) {
		this.tree = tree;
	}

	@Override
	public List<GraphItemData> getGraphData() {
		return this.items;
	}

	@Override
	public void setGraphData(List<GraphItemData> items) {
		this.items = items;
	}

}
