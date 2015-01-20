package jdepend.ui.result.panel;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JScrollPane;

import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.framework.util.BundleUtil;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.TableViewInfo;
import jdepend.model.util.TableViewUtil;
import jdepend.ui.result.framework.SubResultTabPanel;

public final class TablePanel extends SubResultTabPanel {

	@Override
	protected void init(final AnalysisResult result) {

		List<TableViewInfo> tableInfos = TableViewUtil.view(result);
		if (tableInfos.size() > 0) {
			TableData data = new TableData();
			for (TableViewInfo info : tableInfos) {
				data.setData(BundleUtil.getString(BundleUtil.TableHead_TableName), info.getName());
				data.setData(BundleUtil.getString(BundleUtil.TableHead_AppearCount), info.getCount());
				data.setData(BundleUtil.getString(BundleUtil.TableHead_Operation), info.getType());
				data.setData(BundleUtil.getString(BundleUtil.TableHead_ComponentName), info.getComponent());
				data.setData(BundleUtil.getString(BundleUtil.TableHead_ClassName), info.getJavaClass());
			}
			this.add(BorderLayout.CENTER, new JScrollPane(new CooperTable(data)));
		}
	}
}
