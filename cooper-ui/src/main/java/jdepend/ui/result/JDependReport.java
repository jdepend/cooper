package jdepend.ui.result;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.LogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.ui.TextViewer;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.FileUtil;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;
import jdepend.report.ReportCreator;
import jdepend.report.history.ReportHistory;
import jdepend.report.ui.XMLJDependUtil;
import jdepend.report.util.ReportConstant;
import jdepend.report.way.mapui.GraphPrinter;
import jdepend.report.way.textui.JDependPrinter;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.UIPropertyConfigurator;

/**
 * 报告生成器
 * 
 * @author <b>Abner</b>
 * 
 */
public class JDependReport extends ReportCreator {

	private String group;

	private String command;

	private JDependCooper frame;

	public final static boolean printTDC = false;
	public final static boolean printTableTree = false;
	public final static boolean printSummaryXML = false;
	public final static boolean printTable = true;

	public final static boolean print3DRelation = false;
	public final static boolean printRelationText = false;

	public final static boolean printCoupling = false;
	public final static boolean printCohesion = false;
	public final static boolean printCapacity = true;
	public final static boolean printPattern = true;

	public final static String SummaryTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Summary);
	public final static String RelationTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Relation);
	public final static String CouplingTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Coupling);
	public final static String CohesionTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Cohesion);
	public final static String PatternTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Pattern);
	public final static String CapacityTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Capacity);
	public final static String NoticeTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Notice);

	// 用于临时保存报告文本信息
	private transient Map<String, StringBuilder> reportTexts;

	public JDependReport(String group, String command) {
		this.group = group;
		this.command = command;
	}

	public void setFrame(JDependCooper frame) {
		this.frame = frame;
	}

	public Map<String, JComponent> createReport(AnalysisResult result) {

		Map<String, JComponent> rtn = new LinkedHashMap<String, JComponent>();

		rtn.putAll(this.createMainReport(result));
		rtn.putAll(this.createOtherReport(result));

		return rtn;
	}

	public Map<String, JComponent> createMainReport(AnalysisResult result) {

		LogUtil.getInstance(JDependReport.class).systemLog("create main graph report start!");

		this.calReportTexts(result);

		Map<String, JComponent> rtn = new LinkedHashMap<String, JComponent>();

		Map<String, JComponent> groupComponents;

		groupComponents = new LinkedHashMap<String, JComponent>();
		groupComponents.put("Score", new ScorePanel(result, frame));
		groupComponents.put("List", new SummaryPanel(this));
		if (printTableTree) {
			groupComponents.put("TableTree", new SubResultTabPanel() {
				@Override
				protected void init(AnalysisResult result) {
					this.add((new jdepend.report.way.swingui.SwingJDepend()).getResult(result.getComponents()));
				}
			});
		}
		if (printTable) {
			groupComponents.put("Table", new TablePanel());
		}
		groupComponents.put("Text", this.createTextReport(ReportConstant.SummaryText));
		if (printSummaryXML) {
			groupComponents.put("XML", this.createTextReport(ReportConstant.SummaryXML));
		}
		if (printTDC) {
			groupComponents.put("TDC", new TwoDimensionCell());
		}
		rtn.put(SummaryTabName, this.compositeComponent(groupComponents));

		if (result.getRelations() != null) {
			groupComponents = new LinkedHashMap<String, JComponent>();
			groupComponents.put("Graph2D", this.createGraph(result.getRelations()));
			groupComponents.put("Table", new RelationPanel());
			if (printRelationText) {
				groupComponents.put("Text", this.createTextReport(ReportConstant.RelationText));
			}
			rtn.put(RelationTabName, this.compositeComponent(groupComponents));
		}
		if (printCoupling) {
			StringBuilder CouplingText = this.reportTexts.get(ReportConstant.CouplingText);
			if (CouplingText != null) {
				groupComponents = new LinkedHashMap<String, JComponent>();
				groupComponents.put("Table", this.createXML(CouplingText));
				groupComponents.put("Text", this.createTextReport(ReportConstant.CouplingText));
				rtn.put(CouplingTabName, this.compositeComponent(groupComponents));
			}
		}
		if (printCohesion) {
			StringBuilder CohesionText = this.reportTexts.get(ReportConstant.CohesionText);
			if (CohesionText != null) {
				groupComponents = new LinkedHashMap<String, JComponent>();
				groupComponents.put("Table", this.createXML(CohesionText));
				groupComponents.put("Text", this.createTextReport(ReportConstant.CohesionText));
				rtn.put(CohesionTabName, this.compositeComponent(groupComponents));
			}
		}

		LogUtil.getInstance(JDependReport.class).systemLog("create main graph report finish!");

		return rtn;
	}

	public Map<String, JComponent> createOtherReport(AnalysisResult result) {

		LogUtil.getInstance(JDependReport.class).systemLog("create other graph report start!");

		Map<String, JComponent> rtn = new LinkedHashMap<String, JComponent>();

		Map<String, JComponent> groupComponents;

		if (printPattern) {
			groupComponents = new LinkedHashMap<String, JComponent>();
			groupComponents.put("Architect", new ArchitectPatternPanel());
			groupComponents.put("Design", new DesignPatternPanel());
			rtn.put(PatternTabName, this.compositeComponent(groupComponents));
		}

		if (printCapacity) {
			groupComponents = new LinkedHashMap<String, JComponent>();
			groupComponents.put("Panel", new CapacityPanel());
			rtn.put(CapacityTabName, this.compositeComponent(groupComponents));
		}

		StringBuilder NoticesText = this.reportTexts.get(ReportConstant.NoticesText);
		if (NoticesText != null) {
			groupComponents = new LinkedHashMap<String, JComponent>();
			groupComponents.put("Text", this.createTextReport(ReportConstant.NoticesText));
			rtn.put(NoticeTabName, this.compositeComponent(groupComponents));
		}

		LogUtil.getInstance(JDependReport.class).systemLog("create other graph report finish!");

		return rtn;
	}

	private void calReportTexts(AnalysisResult result) {
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

		JDependPrinter printer = new JDependPrinter();
		printer.setStream(resultStream);

		Set<String> items = new HashSet<String>();
		items.add(ReportConstant.SummaryText);
		if (printSummaryXML) {
			items.add(ReportConstant.SummaryXML);
		}
		if (printRelationText) {
			items.add(ReportConstant.RelationText);
		}
		if (printCoupling) {
			items.add(ReportConstant.CouplingText);
		}
		if (printCohesion) {
			items.add(ReportConstant.CohesionText);
		}
		items.add(ReportConstant.NoticesText);

		printer.print(result, items);

		String[] texts = resultStream.toString().split(JDependPrinter.Split);

		reportTexts = new HashMap<String, StringBuilder>();
		String title = ReportConstant.SummaryText;
		for (String text : texts) {
			if (text == null || text.length() == 0)
				continue;
			if (text.startsWith(JDependPrinter.Start)) {
				title = text.substring(1);
				continue;
			}
			this.reportTexts.put(title, new StringBuilder(text));
		}

		try {
			resultStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			resultStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clear() {
	}

	private JComponent createTextReport(String title) {
		return createTextReport(title, this.reportTexts.get(title).toString());
	}

	private JComponent createTextReport(final String title, final String body) {
		return new SubResultTabPanel() {
			@Override
			protected void init(AnalysisResult result) {
				TextViewer resultViewer = initResultArea(title);
				resultViewer.setText(body);
				JScrollPane pane = new JScrollPane(resultViewer);
				resultViewer.setCaretPosition(0);
				resultViewer.setScrollPane(pane);
				this.add(pane);
			}
		};
	}

	private JComponent createXML(final StringBuilder content) {
		return new SubResultTabPanel() {
			@Override
			protected void init(AnalysisResult result) {
				this.add((new XMLJDependUtil()).createResult(content));
			}
		};
	}

	private JComponent compositeComponent(Map<String, JComponent> components) {
		SubResultTab tabPane = new SubResultTab();
		for (String title : components.keySet()) {
			tabPane.add(title, components.get(title));
		}
		return tabPane;
	}

	private JComponent createGraph(Collection<Relation> relations) {
		int maxRelations = UIPropertyConfigurator.getInstance().getMaxRelations();
		if (relations.size() == 0) {
			return new TextViewer();
		} else if (maxRelations == -1 || relations.size() < maxRelations) {
			return new SubResultTabPanel() {
				@Override
				protected void init(AnalysisResult result) {
					this.add(GraphPrinter.getIntance().print(result.getRelations(), result.getJavaPackageTree()));
				}
			};
		} else {
			TextViewer result = new TextViewer();
			result.setText("关系数量为[" + relations.size() + "]，不予生成！");
			return result;
		}
	}

	private TextViewer initResultArea(final String title) {

		final TextViewer resultViewer = new TextViewer();

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem saveItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Save));
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReportHistorySaveDialog d = new ReportHistorySaveDialog(title);
				d.setModal(true);
				d.setLocation(250, 100);
				d.setVisible(true);
			}
		});
		popupMenu.add(saveItem);

		JMenuItem saveasItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveasItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveAs(new StringBuilder(resultViewer.getText()));
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "报告[" + command + "]保存失败！", "alert", JOptionPane.ERROR_MESSAGE);
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

		return resultViewer;
	}

	private void saveAs(StringBuilder content) throws JDependException {
		JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
		int result = jFileChooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File f = jFileChooser.getSelectedFile();
			FileUtil.saveFileContent(f.getAbsolutePath(), content);
			JOptionPane.showMessageDialog(null, "报告[" + command + "]保存成功。", "alert", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	class ReportHistorySaveDialog extends JDialog {

		private JTextArea tip;

		private String title;

		public ReportHistorySaveDialog(String title) {

			setTitle("Tip");

			setResizable(false);

			this.title = title;

			getContentPane().setLayout(new BorderLayout());
			setSize(400, 200);

			addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});

			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());

			tip = new JTextArea();
			tip.setText(title);

			JScrollPane pane = new JScrollPane(tip);

			JPanel buttonBar = new JPanel(new FlowLayout());
			buttonBar.add(createSaveButton());
			buttonBar.add(createCloseButton());

			panel.add(BorderLayout.CENTER, pane);

			panel.add(BorderLayout.SOUTH, buttonBar);

			getContentPane().add(BorderLayout.CENTER, panel);

		}

		private JButton createCloseButton() {

			JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			return button;
		}

		private JButton createSaveButton() {

			JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Save));
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						ReportHistory rh = new ReportHistory(group);
						rh.save(command, reportTexts.get(title), tip.getText());
						JDependReport.this.onReportHistorySave(group, command);
						// 记录日志
						BusiLogUtil.getInstance().businessLog(Operation.saveTextReport);
						JOptionPane.showMessageDialog((java.awt.Component) e.getSource(), "保存成功", "alert",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} catch (Exception ex) {
						ex.printStackTrace();
						java.awt.Component source = (java.awt.Component) e.getSource();
						JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			return button;
		}
	}

}
