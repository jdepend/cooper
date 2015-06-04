package jdepend.report.way.mapui;

import java.util.Collection;
import java.util.Map;

import jdepend.model.Element;
import jdepend.model.Relation;
import jdepend.report.util.IntensitySizeCalculator;
import jdepend.report.way.mapui.model.MapData;
import prefuse.data.Graph;
import prefuse.data.Table;

public class CovertorUtil {

	static Graph getGraph(MapData mapData) {

		int NNODES = mapData.getElements().size();
		int NNODECOLS = 10;
		int NEDGES = mapData.getRelations().size();
		int NEDGECOLS = 8;

		Map<Relation, Integer> relationSizes = IntensitySizeCalculator.calRelationSize(mapData.getRelations());
		Map<Element, Integer> elementSizes = IntensitySizeCalculator.calElementSize(mapData.getElements());

		Class[] NTYPES = { int.class, String.class, double.class, boolean.class, String.class, boolean.class,
				boolean.class, boolean.class, double.class, double.class };

		String[] NHEADERS = { "id", "label", "size", "isInner", "info", "CaColor", "CeColor", "mutualColor", "xField",
				"yField" };
		// id
		Integer[] NCOLUMN1 = new Integer[NNODES];
		int i = 0;
		for (i = 0; i < NNODES; i++) {
			NCOLUMN1[i] = new Integer(i + 1);
		}
		// 显示名称
		String[] NCOLUMN2 = new String[NNODES];
		i = 0;
		for (Element element : mapData.getElements()) {
			NCOLUMN2[i++] = element.getName();
		}
		// 内聚值
		Float[] NCOLUMN3 = new Float[NNODES];
		i = 0;
		for (Element element : mapData.getElements()) {
			NCOLUMN3[i++] = new Float(elementSizes.get(element));
		}
		// 是否内部类
		Boolean[] NCOLUMN4 = new Boolean[NNODES];
		i = 0;
		for (Element element : mapData.getElements()) {
			NCOLUMN4[i++] = element.getComponent().isInner();
		}
		// 摘要信息
		String[] NCOLUMN5 = new String[NNODES];
		i = 0;
		for (Element element : mapData.getElements()) {
			NCOLUMN5[i++] = element.toString();
		}
		// 关联Node颜色
		Boolean[] NCOLUMN6 = new Boolean[NNODES];
		i = 0;
		for (i = 0; i < NNODES; i++) {
			NCOLUMN6[i] = false;
		}
		Boolean[] NCOLUMN7 = new Boolean[NNODES];
		i = 0;
		for (i = 0; i < NNODES; i++) {
			NCOLUMN7[i] = false;
		}
		Boolean[] NCOLUMN8 = new Boolean[NNODES];
		i = 0;
		for (i = 0; i < NNODES; i++) {
			NCOLUMN8[i] = false;
		}
		// 设置上次人工移动的位置
		Double[] NCOLUMN9 = new Double[NNODES];
		Double[] NCOLUMN10 = new Double[NNODES];
		if (mapData.isHaveSpecifiedPosition()) {
			i = 0;
			for (i = 0; i < NNODES; i++) {
				NCOLUMN9[i] = mapData.getX(NCOLUMN2[i]);
			}
			i = 0;
			for (i = 0; i < NNODES; i++) {
				NCOLUMN10[i] = mapData.getY(NCOLUMN2[i]);
			}
		} else {
			i = 0;
			for (i = 0; i < NNODES; i++) {
				NCOLUMN9[i] = 0D;
			}
			i = 0;
			for (i = 0; i < NNODES; i++) {
				NCOLUMN10[i] = 0D;
			}
		}

		Object[][] NODES = { NCOLUMN1, NCOLUMN2, NCOLUMN3, NCOLUMN4, NCOLUMN5, NCOLUMN6, NCOLUMN7, NCOLUMN8, NCOLUMN9,
				NCOLUMN10 };

		Class[] ETYPES = { int.class, int.class, double.class, String.class, int.class, boolean.class, boolean.class,
				boolean.class };

		String[] EHEADERS = { "id1", "id2", "weight", "info", "attentionLevel", "CaColor", "CeColor", "mutualColor" };
		// 起始点编号
		Integer[] ECOLUMN1 = new Integer[NEDGES];
		i = 0;
		for (Relation relation : mapData.getRelations()) {
			ECOLUMN1[i++] = CovertorUtil.getPosition(mapData.getElements(), relation.getCurrent());
		}
		// 终止点编号
		Integer[] ECOLUMN2 = new Integer[NEDGES];
		i = 0;
		for (Relation relation : mapData.getRelations()) {
			ECOLUMN2[i++] = CovertorUtil.getPosition(mapData.getElements(), relation.getDepend());
		}
		// 耦合值
		Float[] ECOLUMN3 = new Float[NEDGES];
		i = 0;
		for (Relation relation : mapData.getRelations()) {
			ECOLUMN3[i++] = new Float(relationSizes.get(relation));
		}
		// 摘要信息
		String[] ECOLUMN4 = new String[NEDGES];
		i = 0;
		for (Relation relation : mapData.getRelations()) {
			ECOLUMN4[i++] = relation.toString();
		}
		// 关注程度
		Integer[] ECOLUMN5 = new Integer[NEDGES];
		i = 0;
		for (Relation relation : mapData.getRelations()) {
			ECOLUMN5[i++] = relation.getAttentionType();
		}
		// 关联线颜色
		Boolean[] ECOLUMN6 = new Boolean[NEDGES];
		i = 0;
		for (i = 0; i < NEDGES; i++) {
			ECOLUMN6[i] = false;
		}
		Boolean[] ECOLUMN7 = new Boolean[NEDGES];
		i = 0;
		for (i = 0; i < NEDGES; i++) {
			ECOLUMN7[i] = false;
		}
		Boolean[] ECOLUMN8 = new Boolean[NEDGES];
		i = 0;
		for (i = 0; i < NEDGES; i++) {
			ECOLUMN8[i] = false;
		}
		Object[][] EDGES = { ECOLUMN1, ECOLUMN2, ECOLUMN3, ECOLUMN4, ECOLUMN5, ECOLUMN6, ECOLUMN7, ECOLUMN8 };

		Table nodes = new Table(NNODES, NNODECOLS);
		for (int c = 0; c < NNODECOLS; ++c) {
			nodes.addColumn(NHEADERS[c], NTYPES[c]);
			for (int r = 0; r < NNODES; ++r) {
				nodes.set(r, NHEADERS[c], NODES[c][r]);
			}
		}

		Table edges = new Table(NEDGES, NEDGECOLS);
		for (int c = 0; c < NEDGECOLS; ++c) {
			edges.addColumn(EHEADERS[c], ETYPES[c]);
			for (int r = 0; r < NEDGES; ++r) {
				edges.set(r, EHEADERS[c], EDGES[c][r]);
			}
		}

		return new Graph(nodes, edges, true, NHEADERS[0], EHEADERS[0], EHEADERS[1]);

	}

	private static int getPosition(Collection<Element> elements, Element obj) {
		int i = 0;
		for (Element element : elements) {
			if (obj.equals(element)) {
				return i + 1;
			}
			i++;
		}
		throw new RuntimeException("数据错误！");

	}
}