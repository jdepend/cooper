package jdepend.ui.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;

import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.knowledge.capacity.Capacity;
import jdepend.knowledge.capacity.CapacityMgr;
import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.result.AnalysisResult;

public final class DesignPatternPanel extends SubResultTabPanel {

	@Override
	protected void init(AnalysisResult result) {

		Capacity capacity = CapacityMgr.getInstance().getCapacity(result);
		Map<String, Collection<PatternInfo>> patternList = capacity.getSkill()
				.getPatternList();

		TableData tableData = new TableData();
		
		List<String> patternNames = new ArrayList<String>(patternList.keySet());
		Collections.sort(patternNames);

		for (String patternName : patternNames) {
			if (patternList.get(patternName) != null
					&& patternList.get(patternName).size() != 0) {
				for (PatternInfo className : patternList.get(patternName)) {
					tableData.setData("模型类型", patternName);
					tableData.setData("类名", className.getJavaClass().getName());
					tableData.setData("信息", className.toString());
				}
			} else {
				tableData.setData("模型类型", patternName);
				tableData.setData("类名", "无");
				tableData.setData("信息", "无");
			}
		}

		this.add(new JScrollPane(new CooperTable(tableData)));
	}

}
