package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;
import jdepend.util.analyzer.framework.GraphItemData;
import jdpend.model.util.JavaClassRelationInfo;
import jdpend.model.util.JavaClassRelationUtil;

public final class JavaClassRelationView extends AbstractAnalyzer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4257969468997138180L;

	public JavaClassRelationView() {
		super("JavaClassRelation浏览", Analyzer.Attention, "JavaClassRelation浏览");
	}

	@Override
	protected void doExecute(AnalysisResult result) throws AnalyzerException {

		JavaClassRelationUtil javaClassRelationUtil = new JavaClassRelationUtil(result);

		for (JavaClassRelationInfo info : javaClassRelationUtil.getRelationInfos()) {
			this.printTable("当前JavaClass", info.current);
			this.printTable("依赖的JavaClass", info.depend);
			this.printTable("类型", info.type);
			this.printTable("是否组件间", info.isInner ? "否" : "是");
			this.printTable("强度", info.intensity);
		}

		this.print(javaClassRelationUtil.getSummaryInfo());

		this.printGraphData(this.getGraphData(javaClassRelationUtil));

	}

	private List<GraphItemData> getGraphData(JavaClassRelationUtil javaClassRelationUtil) {

		List<GraphItemData> items = new ArrayList<GraphItemData>();

		GraphItemData item = new GraphItemData();

		item.setTitle("按类型比例");
		item.setGroup("Graph");
		item.setType(GraphItemData.PIE);
		item.setDatas(javaClassRelationUtil.getTypes());

		items.add(item);

		item = new GraphItemData();

		item.setTitle("按内外比例");
		item.setGroup("Graph");
		item.setType(GraphItemData.PIE);
		item.setDatas(javaClassRelationUtil.getInners());

		items.add(item);

		item = new GraphItemData();

		item.setTitle("组件内按类型比例");
		item.setGroup("Graph");
		item.setType(GraphItemData.PIE);
		item.setDatas(javaClassRelationUtil.getIn_types());

		items.add(item);

		item = new GraphItemData();

		item.setTitle("组件间按类型比例");
		item.setGroup("Graph");
		item.setType(GraphItemData.PIE);
		item.setDatas(javaClassRelationUtil.getExt_types());

		items.add(item);

		return items;
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("Class间的关系是组件间关系形成的原因。<br>");
		explain.append("<strong>JavaClassRelation浏览</strong>从关系的类型，是组件间还是组件内的关系，以及在组件间关系中各类型的类关系的比例等角度展现了Class的关系比例。<br>");
		return explain.toString();
	}

}
