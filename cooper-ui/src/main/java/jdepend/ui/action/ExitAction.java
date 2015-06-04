package jdepend.ui.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.SocketException;

import javax.swing.AbstractAction;

import jdepend.core.local.analyzer.AnalyzerMgr;
import jdepend.report.way.mapui.layout.specifiedposition.SpecifiedPositionMgr;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.UIPropertyConfigurator;

public class ExitAction extends AbstractAction {

	private JDependCooper frame;

	public ExitAction(JDependCooper frame) {
		super("Exit");
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.exit();
		frame.dispose();
		System.exit(0);
	}

	protected void exit() {
		// 关闭CircleService
		try {
			frame.getCirclePanel().close();
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
		// 保存分析器配置
		AnalyzerMgr.getInstance().save();
		// 保存UI设置
		try {
			UIPropertyConfigurator.getInstance().save();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// 保存组件关系图形中组件节点数据
		try {
			SpecifiedPositionMgr.getInstance().save();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
