package jdepend.webserver.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.model.Element;
import jdepend.model.Relation;
import jdepend.report.util.IntensitySizeCalculator;
import net.sf.json.JSONArray;


public class WebRelationGraphUtil {

	static RelationGraphData getGraphData(Collection<Relation> relations) {

		Collection<Element> elements = Relation.calElements(relations);

		Map<Relation, Integer> relationSizes = IntensitySizeCalculator.calRelationSize(relations);
		Map<Element, Integer> elementSizes = IntensitySizeCalculator.calElementSize(elements);

		List<Node> nodes = new ArrayList<Node>();
		Node node;
		for (Element element : elements) {
			node = new Node();
			node.setCategory(0);
			node.setName(element.getName());
			node.setValue(elementSizes.get(element));
			nodes.add(node);
		}
		
		List<Edge> edges = new ArrayList<Edge>();
		Edge edge;
		for (Relation relation : relations) {
			edge = new Edge();
			edge.setSource(getPosition(elements, relation.getCurrent()));
			edge.setTarget(getPosition(elements, relation.getDepend()));
			edge.setWeight(relationSizes.get(relation));
			edges.add(edge);
		}
		return new RelationGraphData(nodes, edges);
	}

	private static int getPosition(Collection<Element> elements, Element obj) {
		int i = 0;
		for (Element element : elements) {
			if (obj.equals(element)) {
				return i;
			}
			i++;
		}
		throw new RuntimeException("数据错误！");

	}

	public static class RelationGraphData {

		private List<Node> nodes;

		private List<Edge> edges;

		public RelationGraphData(List<Node> nodes, List<Edge> edges) {
			super();
			this.nodes = nodes;
			this.edges = edges;
		}

		public String getNodeInfo() {
			return JSONArray.fromObject(nodes).toString();
		}
		
		public String getEdgeInfo() {
			return JSONArray.fromObject(edges).toString();
		}
	}

	public static class Node {

		private int category;
		private String name;
		private int value;

		public int getCategory() {
			return category;
		}

		public void setCategory(int category) {
			this.category = category;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	public static class Edge {

		private int source;
		private int target;
		private int weight;

		public int getSource() {
			return source;
		}

		public void setSource(int source) {
			this.source = source;
		}

		public int getTarget() {
			return target;
		}

		public void setTarget(int target) {
			this.target = target;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
	}
}
