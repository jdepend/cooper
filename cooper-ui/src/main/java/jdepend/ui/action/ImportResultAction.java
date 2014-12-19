package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import jdepend.ui.JDependCooper;
import jdepend.ui.util.AnalysisResultExportUtil;

public class ImportResultAction extends AbstractAction {
	private JDependCooper frame;

	public ImportResultAction(JDependCooper frame) {
		super("导入外部分析结果");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			AnalysisResultExportUtil.importResult(frame);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(frame, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
		}
	}
}