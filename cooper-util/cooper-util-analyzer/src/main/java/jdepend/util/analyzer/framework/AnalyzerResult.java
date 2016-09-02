package jdepend.util.analyzer.framework;

import java.io.Serializable;
import java.util.List;

import jdepend.metadata.tree.Node;

public class AnalyzerResult implements Serializable {

	private String info;

	private TwoDimensionData tableData;

	private Node tree;

	private List<GraphItemData> graphData;

	public AnalyzerResult(String info, TwoDimensionData tableData, Node tree, List<GraphItemData> graphData) {

		super();

		this.info = info;
		this.tableData = tableData;
		this.tree = tree;
		this.graphData = graphData;
	}

	public boolean existResult() {
		return this.info != null || this.existTwoDimensionData() || this.tree != null || this.graphData != null;
	}

	public String getInfo() {
		return info;
	}

	public TwoDimensionData getTwoDimensionData() {
		return tableData;
	}

	public boolean existTwoDimensionData() {
		return this.tableData != null && this.tableData.existData();
	}

	public Node getTree() {
		return this.tree;
	}

	public List<GraphItemData> getGraphData() {
		return this.graphData;
	}
}
