package jdepend.client.ui.analyzer;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import jdepend.client.core.analyzer.AnalyzerMgr;
import jdepend.client.report.util.TreeGraphUtil;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.framework.UIPropertyConfigurator;
import jdepend.client.ui.result.framework.ResultPanel;
import jdepend.client.ui.util.ClassPathURLStreamHandler;
import jdepend.framework.domain.PersistentBean;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.ui.dialog.PersistentBeanSettingDialog;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.framework.ui.graph.model.GraphData;
import jdepend.framework.ui.util.ExceptionPrinter;
import jdepend.framework.util.BundleUtil;
import jdepend.metadata.tree.Node;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerExecutor;
import jdepend.util.analyzer.framework.AnalyzerResult;

public class AnalyzerPanel extends JPanel {

	protected JDependCooper frame;

	protected Map<String, List<AnalyzerUIWrapper>> analyzers;

	private Map<String, DefaultTableModel> models;

	protected int currentRow;

	protected String currentGroup;

	private Map<String, String> tips = new HashMap<String, String>();

	public AnalyzerPanel(final JDependCooper frame) {
		super();
		this.frame = frame;
		this.models = new LinkedHashMap<String, DefaultTableModel>();

		this.setLayout(new BorderLayout());

		JTabbedPane tab = this.initAnalyzerTab();

		this.setDefaultTab(tab);

		this.add(tab);
	}

	private JTabbedPane initAnalyzerTab() {

		JTabbedPane tab = new JTabbedPane(JTabbedPane.BOTTOM);

		tab.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
				currentGroup = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			}
		});

		for (final String group : AnalyzerMgr.getInstance().getTypes()) {

			DefaultTableModel model = new DefaultTableModel() {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}

			};

			this.models.put(group, model);

			JTable table = new JTable(model) {
				@Override
				public String getToolTipText(MouseEvent event) {
					Point p = event.getPoint();
					int row = rowAtPoint(p);
					int col = columnAtPoint(p);

					if (row == -1 || col == -1) {
						return null;
					}

					String key = (String) this.getValueAt(row, col);
					return tips.get(key);
				}
			};

			final JPopupMenu popupMenu = this.getPopupMenu();

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					JTable table = (JTable) e.getSource();
					currentRow = table.rowAtPoint(e.getPoint());
					if (currentRow >= 0) {
						table.setRowSelectionInterval(currentRow, currentRow);
					}
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						try {
							run();
						} catch (JDependException e1) {
							e1.printStackTrace();
							JOptionPane.showConfirmDialog(frame, e1.getMessage(), "提示", JOptionPane.CLOSED_OPTION);
						}
					} else if (e.getButton() == 3) {
						JTable table = (JTable) e.getSource();
						popupMenu.show(table, e.getX(), e.getY());
					}
				}
			});

			model.addColumn(BundleUtil.getString(BundleUtil.TableHead_Name));
			this.refresh(group);

			final JPopupMenu popupMenu1 = this.getPopupMenu1();

			final JScrollPane pane = new JScrollPane(table);
			pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			pane.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 3) {
						popupMenu1.show(pane, e.getX(), e.getY());
					}
				}
			});
			tab.add(group, pane);
		}

		if (tab.getSelectedIndex() != -1) {
			currentGroup = tab.getTitleAt(tab.getSelectedIndex());
		}

		return tab;
	}

	protected JPopupMenu getPopupMenu() {

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem runItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Run));
		runItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					run();
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showConfirmDialog(frame, e1.getMessage(), "提示", JOptionPane.CLOSED_OPTION);
				}
			}
		});
		popupMenu.add(runItem);
		popupMenu.addSeparator();

		JMenuItem settingItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Setting));
		settingItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setting();
			}
		});
		popupMenu.add(settingItem);

		JMenuItem explainItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Explain));
		explainItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				explain();
			}
		});
		popupMenu.add(explainItem);

		JMenuItem refreshItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Refresh));
		refreshItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(refreshItem);

		return popupMenu;

	}

	protected JPopupMenu getPopupMenu1() {

		final JPopupMenu popupMenu1 = new JPopupMenu();

		JMenuItem refreshItem1 = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Refresh));
		refreshItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu1.add(refreshItem1);

		return popupMenu1;
	}

	private void setDefaultTab(JTabbedPane tab) {

		String defaultTab = UIPropertyConfigurator.getInstance().getDefaultAnalyzerType();

		for (int i = 0; i < tab.getTabCount(); i++) {
			if (tab.getTitleAt(i).equals(defaultTab)) {
				tab.setSelectedIndex(i);
				currentGroup = defaultTab;
				break;
			}
		}
	}

	private void run() throws JDependException {
		final AnalysisResult result = JDependUnitMgr.getInstance().getResult();
		if (result != null && !result.isEmpty()) {
			final Analyzer analyzer = this.analyzers.get(this.currentGroup).get(currentRow);

			final AnalyzerExecutor executor = new AnalyzerExecutor(analyzer);
			executor.setListener(frame);
			new Thread() {
				@Override
				public void run() {
					try {
						frame.startProgressMonitor(analyzer.getMaxProgress(result));

						AnalyzerResult analyzerResult = executor.execute(result);
						if (analyzerResult.existResult()) {
							frame.getResultPanel().addResult(analyzer.getName(), formatResult(analyzerResult));
						} else {
							StringBuilder info = new StringBuilder();
							info.append(analyzer.getName());
							info.append(":\n");
							info.append("	没有信息");
							frame.getResultPanel().addResult(analyzer.getName(), info);
						}
						// 记录日志
						BusiLogUtil.getInstance().businessLog(Operation.executeAnalyzer);

						frame.stopProgressMonitor();
					} catch (JDependException ex) {
						frame.getResultPanel().showError(ex);
						frame.stopProgressMonitor();
					}
				}
			}.start();
		} else {
			throw new JDependException("您还没有执行命令，请执行完命令后在执行该操作");
		}
	}

	public void refreshAll() {
		for (String group : AnalyzerMgr.getInstance().getTypes()) {
			this.refresh(group);
		}
	}

	public void refresh() {
		this.refresh(this.currentGroup);
	}

	private void refresh(String group) {
		Object[] row;

		this.analyzers = new LinkedHashMap<String, List<AnalyzerUIWrapper>>();
		for (String type : AnalyzerMgr.getInstance().getAnalyzers().keySet()) {
			this.analyzers.put(type, new ArrayList<AnalyzerUIWrapper>());
			for (Analyzer analyzer : AnalyzerMgr.getInstance().getAnalyzers(type)) {
				this.analyzers.get(type).add(new AnalyzerUIWrapper(analyzer));
			}
		}

		this.models.get(group).setRowCount(0);

		for (Analyzer analyzer : this.analyzers.get(group)) {
			row = new Object[1];
			row[0] = analyzer.getName();
			tips.put(analyzer.getName(), analyzer.getTip());

			this.models.get(group).addRow(row);
		}
	}

	private JComponent formatResult(AnalyzerResult result) {
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setTabPlacement(JTabbedPane.BOTTOM);

		if (result.existTableData()) {
			tabPane.addTab("Table", new JScrollPane(new CooperTable(result.getTableData())));
		}

		if (result.getTree() != null) {
			tabPane.addTab("Tree", createTree(result.getTree()));
		}
		try {
			if (result.getGraphData() != null) {
				tabPane.addTab("Graph", createGraph(result.getGraphData()));
			}
		} catch (JDependException e) {
			e.printStackTrace();
			tabPane.addTab("Graph", ExceptionPrinter.createComponent(e));
		}

		if (result.getInfo() != null) {
			tabPane.addTab("Text", ResultPanel.createTextViewer(new StringBuilder(result.getInfo())));
		}

		return tabPane;
	}

	private JComponent createGraph(GraphData graphData) throws JDependException {
		return GraphUtil.createGraph(graphData);
	}

	private JComponent createTree(Node root) {
		return (new TreeGraphUtil()).createTree(root);
	}

	private void setting() {
		AnalyzerUIWrapper analyzer = this.analyzers.get(this.currentGroup).get(this.currentRow);

		PersistentBeanSettingDialog d = new PersistentBeanSettingDialog(frame, (PersistentBean) analyzer.getAnalyzer());

		d.setModal(true);
		d.setVisible(true);
	}

	protected void explain() {
		Analyzer antiPattern = this.analyzers.get(this.currentGroup).get(this.currentRow);

		String explainTxt = antiPattern.getExplain();
		if (explainTxt == null) {
			explainTxt = "没有提供说明";
		}
		JEditorPane explain = new JEditorPane();
		explain.setContentType("text/html;charset=utf-8");
		explain.setEditable(false);

		StyleSheet ss = new StyleSheet();
		HTMLEditorKit kit = new HTMLEditorKit();
		ss.addStyleSheet(kit.getStyleSheet());

		kit.setStyleSheet(ss);
		explain.setEditorKit(kit);

		if (explainTxt.endsWith("htm") || explainTxt.endsWith("html")) {
			try {
				explain.setPage(new URL(null, "classpath:" + explainTxt, new ClassPathURLStreamHandler()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			explain.setText(explainTxt);
		}
		explain.setCaretPosition(0);

		JScrollPane pane = new JScrollPane(explain);

		frame.getResultPanel().addResult(antiPattern.getName() + "说明", pane);
		frame.getResultPanel().setLastedTab();

	}
}
