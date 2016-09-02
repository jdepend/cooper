package jdepend.util.analyzer.framework;

import java.util.List;

import jdepend.metadata.tree.Node;

public interface AnalyzerWorker {

	public String getInfo();

	public void setInfo(String info);

	public TwoDimensionData getTwoDimensionData();

	public void setTwoDimensionData(String key, Object value);

	public void setAnalyzer(Analyzer analyzer);

	public void isPrintTab(boolean tab);

	public void setTree(Node root);

	public Node getTree();

	public void setGraphData(List<GraphItemData> items);

	public List<GraphItemData> getGraphData();
}
