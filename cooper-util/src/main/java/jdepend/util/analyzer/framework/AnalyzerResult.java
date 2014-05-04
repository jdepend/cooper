package jdepend.util.analyzer.framework;

import java.io.Serializable;

import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.TableData;
import jdepend.model.tree.Node;

public class AnalyzerResult implements Serializable {

	private String info;

	private TableData tableData;

	private Node tree;

	private GraphData graphData;

	public AnalyzerResult(String info, TableData tableData, Node tree, GraphData graphData) {

		super();

		this.info = info;
		this.tableData = tableData;
		this.tree = tree;
		this.graphData = graphData;
	}

	public boolean existResult() {
		return this.info != null || this.existTableData() || this.tree != null || this.graphData != null;
	}

	public String getInfo() {
		return info;
	}

	public TableData getTableData() {
		return tableData;
	}

	public boolean existTableData() {
		return this.tableData != null && this.tableData.existData();
	}

	public Node getTree() {
		return this.tree;
	}

	public GraphData getGraphData() {
		return this.graphData;
	}
}
