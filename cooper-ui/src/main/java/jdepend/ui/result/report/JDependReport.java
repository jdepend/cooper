package jdepend.ui.result.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.TextViewer;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.FileUtil;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;
import jdepend.report.ui.XMLJDependUtil;
import jdepend.report.util.ReportConstant;
import jdepend.report.way.mapui.GraphPrinter;
import jdepend.report.way.textui.JDependPrinter;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.UIPropertyConfigurator;
import jdepend.ui.result.framework.SubResultTab;
import jdepend.ui.result.framework.SubResultTabPanel;
import jdepend.ui.result.panel.ArchitectPatternPanel;
import jdepend.ui.result.panel.CapacityPanel;
import jdepend.ui.result.panel.ClassListSubTabPanel;
import jdepend.ui.result.panel.ComponentListPanel;
import jdepend.ui.result.panel.DesignPatternPanel;
import jdepend.ui.result.panel.MethodListSubTabPanel;
import jdepend.ui.result.panel.RelationPanel;
import jdepend.ui.result.panel.ReportHistorySaveDialog;
import jdepend.ui.result.panel.ScorePanel;
import jdepend.ui.result.panel.TablePanel;
import jdepend.ui.result.panel.TwoDimensionCell;

/**
 * 报告生成器
 * 
 * @author <b>Abner</b>
 * 
 */
public class JDependReport extends ReportCreator {

	private JDependCooper frame;

	public final static boolean printSummaryText = false;
	public final static boolean printTDC = false;
	public final static boolean printSummaryXML = false;
	public final static boolean printTable = true;

	public final static boolean print3DRelation = false;
	public final static boolean printRelationText = false;

	public final static boolean printCoupling = false;
	public final static boolean printCohesion = false;

	public final static String SystemTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_System);
	public final static String ComponentTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Component);
	public final static String ClassTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Class);
	public final static String MethodTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Method);
	public final static String NoticeTabName = BundleUtil.getString(BundleUtil.ClientWin_Result_Notice);

	// 用于临时保存报告文本信息
	private transient Map<String, StringBuilder> reportTexts;

	public JDependReport(String group, String command) {
		super(group, command);
	}

	public void setFrame(JDependCooper frame) {
		this.frame = frame;
	}

	public StringBuilder getReportText(String title) {
		return reportTexts.get(title);
	}

	public void setReportTexts(Map<String, StringBuilder> reportTexts) {
		this.reportTexts = reportTexts;
	}

	public Map<String, ? extends JComponent> createReport(AnalysisResult result) {

		LogUtil.getInstance(JDependReport.class).systemLog("create main graph report start!");

		this.calReportTexts(result);

		Map<String, SubResultTab> rtn = new LinkedHashMap<String, SubResultTab>();

		Map<String, SubResultTabPanel> groupComponents;

		// 系统
		LogUtil.getInstance(JDependReport.class).systemLog("create system graph report start!");
		groupComponents = new LinkedHashMap<String, SubResultTabPanel>();
		groupComponents.put("Score", new ScorePanel(result, frame));
		groupComponents.put("Architect", new ArchitectPatternPanel());
		groupComponents.put("Capacity", new CapacityPanel(frame));
		rtn.put(SystemTabName, this.compositeComponent(groupComponents));
		LogUtil.getInstance(JDependReport.class).systemLog("create system graph report end!");

		// 组件
		LogUtil.getInstance(JDependReport.class).systemLog("create component graph report start!");
		groupComponents = new LinkedHashMap<String, SubResultTabPanel>();
		groupComponents.put("List", new ComponentListPanel(frame, this));
		if (result.getRelations() != null) {
			groupComponents.put("RGraph2D", this.createGraph(result.getRelations()));
			groupComponents.put("RTable", new RelationPanel(frame));
			if (printRelationText) {
				groupComponents.put("RText", this.createTextReport(ReportConstant.RelationText));
			}
		}
		if (printTDC) {
			groupComponents.put("TDC", new TwoDimensionCell(frame));
		}
		rtn.put(ComponentTabName, this.compositeComponent(groupComponents));
		LogUtil.getInstance(JDependReport.class).systemLog("create component graph report end!");

		// 类
		LogUtil.getInstance(JDependReport.class).systemLog("create class graph report start!");
		groupComponents = new LinkedHashMap<String, SubResultTabPanel>();
		groupComponents.put("List", new ClassListSubTabPanel(frame));
		groupComponents.put("Table", new TablePanel());
		groupComponents.put("Pattern", new DesignPatternPanel());
		rtn.put(ClassTabName, this.compositeComponent(groupComponents));
		LogUtil.getInstance(JDependReport.class).systemLog("create class graph report end!");

		// 方法
		LogUtil.getInstance(JDependReport.class).systemLog("create method graph report start!");
		groupComponents = new LinkedHashMap<String, SubResultTabPanel>();
		groupComponents.put("List", new MethodListSubTabPanel(frame));
		rtn.put(MethodTabName, this.compositeComponent(groupComponents));

		StringBuilder NoticesText = this.reportTexts.get(ReportConstant.NoticesText);
		if (NoticesText != null) {
			groupComponents = new LinkedHashMap<String, SubResultTabPanel>();
			groupComponents.put("Text", this.createTextReport(ReportConstant.NoticesText));
			rtn.put(NoticeTabName, this.compositeComponent(groupComponents));
		}
		LogUtil.getInstance(JDependReport.class).systemLog("create method graph report end!");

		LogUtil.getInstance(JDependReport.class).systemLog("create main graph report finish!");

		return rtn;
	}

	private void calReportTexts(AnalysisResult result) {
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

		JDependPrinter printer = new JDependPrinter();
		printer.setStream(resultStream);

		Set<String> items = new HashSet<String>();
		if (printSummaryText) {
			items.add(ReportConstant.SummaryText);
		}
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
			resultStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clear() {
	}

	private SubResultTabPanel createTextReport(String title) {
		return createTextReport(title, this.reportTexts.get(title).toString());
	}

	private SubResultTabPanel createTextReport(final String title, final String body) {
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

	private SubResultTab compositeComponent(Map<String, SubResultTabPanel> components) {
		SubResultTab tabPane = new SubResultTab();
		for (String title : components.keySet()) {
			tabPane.add(title, components.get(title));
		}
		return tabPane;
	}

	private SubResultTabPanel createGraph(final Collection<Relation> relations) {
		int maxRelations = UIPropertyConfigurator.getInstance().getMaxRelations();
		if (relations.size() == 0) {
			return new SubResultTabPanel() {
				@Override
				protected void init(AnalysisResult result) {
					this.add(new TextViewer());
				}
			};
		} else if (maxRelations == -1 || relations.size() < maxRelations) {
			return new SubResultTabPanel() {
				@Override
				protected void init(AnalysisResult result) {
					this.add(new GraphPrinter(frame, result));
				}
			};
		} else {
			return new SubResultTabPanel() {
				@Override
				protected void init(AnalysisResult result) {
					TextViewer resultViewer = new TextViewer();
					resultViewer.setText("关系数量为[" + relations.size() + "]，不予生成！");
					this.add(resultViewer);
				}
			};
		}
	}

	private TextViewer initResultArea(final String title) {

		final TextViewer resultViewer = new TextViewer();

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem saveItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Save));
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReportHistorySaveDialog d = new ReportHistorySaveDialog(JDependReport.this, title);
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
					JOptionPane.showMessageDialog(null, "报告[" + getCommand() + "]保存失败！", "alert",
							JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "报告[" + getCommand() + "]保存成功。", "alert",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
