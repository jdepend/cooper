package jdepend.ui.result;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.TabWrapper;
import jdepend.framework.ui.TabsPanel;
import jdepend.framework.ui.TextViewer;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.FileUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.UIPropertyConfigurator;
import jdepend.util.refactor.AdjustHistory;
import jdepend.util.refactor.Memento;

public class ResultPanel extends TabsPanel {

	private JDependCooper frame;

	public ResultPanel(JDependCooper frame) {
		super(new ResultTab(frame));
		this.frame = frame;
	}

	/**
	 * 显示结果（弹出分数窗口）
	 * 
	 * @param results
	 */
	public void showResults(Map<String, JComponent> results) {
		this.showResults(results, true);
	}

	/**
	 * 显示结果
	 * 
	 * @param results
	 * @param isPopup
	 *            是否弹出分数窗口（会自动保存分数）
	 */
	private void showResults(Map<String, JComponent> results, boolean isPopup) {
		String label;
		Iterator<String> iterator = results.keySet().iterator();

		this.setVisible(false);

		while (iterator.hasNext()) {
			label = iterator.next();
			this.addTab(label, results.get(label));
		}
		setDefaultTab();

		this.setVisible(true);

		if (isPopup && new UIPropertyConfigurator().getPopupSummary() != 0) {
			this.showPopupSummary();
		}
	}

	private void showPopupSummary() {
		if (JDependUnitMgr.getInstance().getResult() != null) {
			PopupSummaryDialog d = new PopupSummaryDialog(frame);
			d.setVisible(true);
		}
	}

	public void setDefaultTab() {
		UIPropertyConfigurator conf = UIPropertyConfigurator.getInstance();
		int defaultOneIndex = conf.obtainDefaultTabOneIndex();
		int defaultTwoIndex = conf.obtainDefaultTabTwoIndex();
		this.setDefaultTab(defaultOneIndex, defaultTwoIndex);
	}

	private void setDefaultTab(int defaultOneIndex, int defaultTwoIndex) {
		this.setVisible(false);
		if (defaultOneIndex < this.tabPane.getTabCount()) {
			this.tabPane.setSelectedIndex(defaultOneIndex);
		}
		if (this.tabPane.getSelectedComponent() != null
				&& ((TabWrapper) this.tabPane.getSelectedComponent()).getComponent() instanceof SubResultTab) {
			SubResultTab subTab = (SubResultTab) ((TabWrapper) this.tabPane.getSelectedComponent()).getComponent();
			if (defaultOneIndex < subTab.getTabCount()) {
				subTab.setSelectedIndex(defaultTwoIndex);
			}
		}
		this.setVisible(true);
	}

	public void setTab(int one, int two) {
		this.setDefaultTab(one, two);
	}

	private int getOneIndex() {
		if (this.tabPane.getTabCount() > 0) {
			return this.tabPane.getSelectedIndex();
		} else {
			return -1;
		}
	}

	private int getTwoIndex() {
		if (this.tabPane.getTabCount() > 0) {
			if (((TabWrapper) this.tabPane.getSelectedComponent()).getComponent() instanceof SubResultTab) {
				SubResultTab subTab = (SubResultTab) ((TabWrapper) this.tabPane.getSelectedComponent()).getComponent();
				return subTab.getSelectedIndex();
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	@Override
	protected void addTab(String label, JComponent component) {
		super.addTab(label, component);
		if (component instanceof SubResultTab) {
			SubResultTab subTab = (SubResultTab) component;
			subTab.setParentTab((ResultTab) this.tabPane);
		}
	}

	public void addResult(String label, StringBuilder result) {
		this.addResult(label, createTextViewer(result));
	}

	public void addResult(String label, JComponent component) {
		this.show(label, component);
	}

	public void appendResult(String label, JComponent component) {
		addTab(label, component);
	}

	public void appendResult(String label, StringBuilder result) {
		appendResult(label, createTextViewer(result));
	}

	public static JComponent createTextViewer(StringBuilder result) {

		final TextViewer resultViewer = new TextViewer();
		resultViewer.setText(result.toString());
		resultViewer.setCaretPosition(0);

		JScrollPane pane = new JScrollPane(resultViewer);
		resultViewer.setScrollPane(pane);

		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem saveasItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveasItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveAs(new StringBuilder(resultViewer.getText()));
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "保存失败！", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(saveasItem);

		resultViewer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TextViewer obj = (TextViewer) e.getSource();
				if (e.getButton() == 3) {
					popupMenu.show(obj, e.getX(), e.getY());
				}
			}
		});

		return pane;
	}

	private static void saveAs(StringBuilder content) throws JDependException {
		JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
		int result = jFileChooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File f = jFileChooser.getSelectedFile();
			FileUtil.saveFileContent(f.getAbsolutePath(), content);
			JOptionPane.showMessageDialog(null, "保存成功。", "alert", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * 显示内存中JDependUnitMgr中的结果（不弹出分数窗口）
	 */
	public void showResults() {

		this.removeAll();

		Map<String, JComponent> results = null;
		try {
			// 得到内存分析结果
			AnalysisResult result = JDependUnitMgr.getInstance().getResult();
			// 构造报告生成器
			JDependReport jdependReport = new JDependReport(result.getRunningContext().getGroup(), result
					.getRunningContext().getCommand());
			jdependReport.setFrame(frame);
			jdependReport.addReportListener(frame);
			// 创建图形化结果
			results = jdependReport.createMainReport(result);
			// 显示结果
			this.showResults(results, false);
			// 输出其他报告
			Map<String, JComponent> otherReport = jdependReport.createOtherReport(result);
			for (String otherName : otherReport.keySet()) {
				this.appendResult(otherName, otherReport.get(otherName));
			}
			// 添加差异页
			Memento memento = AdjustHistory.getInstance().getCompared();
			if (memento != null) {
				AnalysisResult result1 = memento.getResult();
				StringBuilder diff = result.equals(result1);
				if (diff != null) {
					this.appendResult("差异", diff);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			this.showError(ex);
		}
		frame.getPropertyPanel().getClassPanel().clearClassList();
	}

	/**
	 * 显示内存操作后的结果
	 */
	public void showMemoryResults() {
		int defaultOneIndex = this.getOneIndex();
		int defaultTwoIndex = this.getTwoIndex();

		this.showResults();

		if (defaultOneIndex != -1 && defaultTwoIndex != -1) {
			this.setDefaultTab(defaultOneIndex, defaultTwoIndex);
		}
	}
}
