package jdepend.ui.result;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JScrollPane;

import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.framework.util.BundleUtil;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.TableViewInfo;
import jdepend.model.util.TableViewUtil;

public final class TablePanel extends SubResultTabPanel {

	@Override
	protected void init(final AnalysisResult result) {

		List<TableViewInfo> tableInfos = TableViewUtil.view(result);
		if (tableInfos.size() > 0) {
			TableData data = new TableData();
			for (TableViewInfo info : tableInfos) {
				data.setData(BundleUtil.getString(BundleUtil.TableHead_TableName), info.name);
				data.setData(BundleUtil.getString(BundleUtil.TableHead_AppearCount), info.count);
				data.setData(BundleUtil.getString(BundleUtil.TableHead_Operation), info.type);
				data.setData(BundleUtil.getString(BundleUtil.TableHead_ComponentName), info.Component);
				data.setData(BundleUtil.getString(BundleUtil.TableHead_ClassName), info.javaClass);
			}
			this.add(BorderLayout.CENTER, new JScrollPane(new CooperTable(data)));
		} 
	}
}
