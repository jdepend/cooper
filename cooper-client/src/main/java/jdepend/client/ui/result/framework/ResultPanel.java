package jdepend.client.ui.result.framework;

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

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.framework.UIPropertyConfigurator;
import jdepend.client.ui.result.panel.ScoreSummaryDialog;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.component.TextViewer;
import jdepend.framework.ui.panel.TabWrapper;
import jdepend.framework.ui.panel.TabsPanel;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.FileUtil;

public class ResultPanel extends TabsPanel {

	private JDependCooper frame;

	public ResultPanel(JDependCooper frame) {
		super(new ResultTab(frame));
		this.frame = frame;
	}

	public JDependCooper getFrame() {
		return frame;
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
	public void showResults(Map<String, ? extends JComponent> results, boolean isPopup) {
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
		ScoreSummaryDialog d = new ScoreSummaryDialog(frame);
		d.setVisible(true);
	}

	public void setDefaultTab() {
		UIPropertyConfigurator conf = UIPropertyConfigurator.getInstance();
		int defaultOneIndex = conf.obtainDefaultTabOneIndex();
		int defaultTwoIndex = conf.obtainDefaultTabTwoIndex();
		this.setDefaultTab(defaultOneIndex, defaultTwoIndex);
	}

	public void setDefaultTab(int defaultOneIndex, int defaultTwoIndex) {
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

	public int getOneIndex() {
		if (this.tabPane.getTabCount() > 0) {
			return this.tabPane.getSelectedIndex();
		} else {
			return -1;
		}
	}

	public int getTwoIndex() {
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

	public void appendResult(Map<String, JComponent> components) {

		this.setVisible(false);

		for (String label : components.keySet()) {
			this.addTab(label, components.get(label));
		}

		this.setVisible(true);
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

}
