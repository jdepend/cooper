package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.util.AnalysisResultExportUtil;

public class ImportResultAction extends AbstractAction {
	private JDependCooper frame;

	public ImportResultAction(JDependCooper frame) {
		super("导入外部分析结果");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		AnalysisResultExportUtil.importResult(frame);
	}
}