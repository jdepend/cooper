package jdepend.util.analyzer.framework;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.TableCallBack;
import jdepend.framework.ui.graph.TableData;
import jdepend.model.tree.JavaClassTree;
import jdepend.model.tree.Node;

public class DefaultAnalyzerWorker implements AnalyzerWorker {

	private Analyzer analyzer;

	private StringBuilder info = new StringBuilder();

	private TableData tableData = new TableData();

	private Node tree;

	private GraphData graphData;

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
	public TableData getTableData() {
		return tableData;
	}

	@Override
	public void setTableData(String key, Object value) {
		tableData.setData(key, value);
	}

	@Override
	public void addTableCallBack(TableCallBack callBack) {
		tableData.addCallBack(callBack);
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
	public GraphData getGraphData() {
		return this.graphData;
	}

	@Override
	public void setGraphData(GraphData graphData) {
		this.graphData = graphData;
	}

}
