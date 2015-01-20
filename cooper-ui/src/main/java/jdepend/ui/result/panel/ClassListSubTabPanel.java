package jdepend.ui.result.panel;

import jdepend.framework.ui.JDependFrame;
import jdepend.model.result.AnalysisResult;
import jdepend.report.ui.ClassListOperationPanel;
import jdepend.report.ui.ClassListPanel;
import jdepend.ui.framework.JavaClassCompareTableCellRenderer;
import jdepend.ui.result.framework.SubResultTabPanel;

public class ClassListSubTabPanel extends SubResultTabPanel {

	private JDependFrame frame;

	public ClassListSubTabPanel(JDependFrame frame) {
		this.frame = frame;
	}

	@Override
	protected void init(AnalysisResult result) {

		ClassListPanel classListPanel = new ClassListPanel(frame) {
			@Override
			protected void initClassList() {
				super.initClassList();

				JavaClassCompareTableCellRenderer renderer = new JavaClassCompareTableCellRenderer(this.extendUnits);
				for (int i = 0; i < classListTable.getColumnCount(); i++) {
					classListTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
				}
			}
		};
		classListPanel.initPopupMenu(null);

		ClassListOperationPanel classListOperationPanel = new ClassListOperationPanel(classListPanel);

		this.add(classListOperationPanel);
	}

}
