package jdepend.client.ui.result.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import jdepend.client.report.ui.BalanceComponentDialog;
import jdepend.client.report.ui.ClassListDialog;
import jdepend.client.report.ui.CohesionDialog;
import jdepend.client.report.ui.CouplingDialog;
import jdepend.client.report.util.ReportConstant;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.componentconf.ChangedElementListDialog;
import jdepend.client.ui.framework.CompareInfoWebWarpper;
import jdepend.client.ui.result.framework.SubResultTabPanel;
import jdepend.client.ui.shoppingcart.model.ShoppingCart;
import jdepend.client.ui.util.AnalysisResultExportUtil;
import jdepend.core.local.domain.WisdomAnalysisResult;
import jdepend.core.local.score.ScoreFacade;
import jdepend.core.local.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.GraphDataItem;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.framework.ui.util.JDependUIUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MathUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.framework.util.VersionUtil;
import jdepend.knowledge.domainanalysis.AdviseInfo;
import jdepend.knowledge.domainanalysis.StructureCategory;
import jdepend.model.JDependUnitMgr;
import jdepend.model.Scored;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisResultScored;
import jdepend.util.refactor.AdjustHistory;
import jdepend.util.refactor.CompareInfo;
import jdepend.util.refactor.CompareObject;

public final class ScorePanel extends SubResultTabPanel {

	private AnalysisResult result;
	private JDependCooper frame;

	private List<ScoreInfo> scorelist = new ArrayList<ScoreInfo>();

	public ScorePanel(AnalysisResult result, JDependCooper frame) {
		this.result = result;
		this.frame = frame;
	}

	@Override
	protected void init(AnalysisResult result) {
		try {
			scorelist = ScoreFacade.getScoreList();
		} catch (JDependException e) {
			e.printStackTrace();
		}
		initComponents();
	}

	private void initComponents() {

		this.setBackground(new java.awt.Color(255, 255, 255));
		this.setBorder(new EmptyBorder(2, 2, 2, 2));

		this.add(BorderLayout.NORTH, this.createTitleInfo());

		this.add(BorderLayout.CENTER, this.createWorkspacePanel());
	}

	private JComponent createTitleInfo() {

		JPanel content = new JPanel(new BorderLayout());
		content.setBackground(new java.awt.Color(255, 255, 255));

		JLabel executeInfo = new JLabel();

		executeInfo.setFont(new java.awt.Font("宋体", 0, 10));
		executeInfo.setForeground(new java.awt.Color(204, 204, 204));
		executeInfo.setText(BundleUtil.getString(BundleUtil.Analysis_Time) + ":"
				+ this.result.getRunningContext().getAnalyseDate() + " V" + VersionUtil.getVersion() + " BuildDate:"
				+ VersionUtil.getBuildDate() + " Group:" + this.result.getRunningContext().getGroup() + " Command:"
				+ this.result.getRunningContext().getCommand());

		content.add(BorderLayout.WEST, executeInfo);

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
		buttons.setBackground(new java.awt.Color(255, 255, 255));

		JLabel addResultButton = new JLabel();
		addResultButton.setIcon(new ImageIcon(JDependUIUtil.getImage("cart/add.png")));
		addResultButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		addResultButton.setToolTipText("将当前结果加入到购物车");
		addResultButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				try {
					ShoppingCart.getInstance().addProduct(JDependUnitMgr.getInstance().getResult());
					frame.getStatusField().refresh();
				} catch (JDependException e) {
					JOptionPane.showMessageDialog(ScorePanel.this, e.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		buttons.add(addResultButton);

		JLabel exportResultButton = new JLabel();
		exportResultButton.setIcon(new ImageIcon(JDependUIUtil.getImage("export.png")));
		exportResultButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		exportResultButton.setToolTipText("将当前结果导出");
		exportResultButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				try {
					AnalysisResult result = JDependUnitMgr.getInstance().getResult();
					AnalysisResultExportUtil.exportResult(frame, result);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "导出失败！", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		buttons.add(exportResultButton);

		content.add(BorderLayout.EAST, buttons);

		return content;
	}

	private JPanel createWorkspacePanel() {

		JPanel workspacePanel = new JPanel(new GridLayout(1, 2));
		workspacePanel.setBackground(new java.awt.Color(255, 255, 255));

		JPanel leftPanel = new JPanel(new BorderLayout());

		JPanel scorePanel = new JPanel(new BorderLayout());
		scorePanel.setBorder(new TitledBorder(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_TotalScore)));
		scorePanel.setBackground(new java.awt.Color(255, 255, 255));

		scorePanel.add(this.createLeftPanel());

		leftPanel.add(BorderLayout.CENTER, scorePanel);

		workspacePanel.add(leftPanel);

		JPanel subitemPanel = new JPanel(new GridLayout(4, 1));
		subitemPanel.setBackground(new java.awt.Color(255, 255, 255));

		JPanel dPanel = new JPanel(new BorderLayout());
		dPanel.setBorder(new TitledBorder(BundleUtil.getString(BundleUtil.Metrics_D)));
		dPanel.setBackground(new java.awt.Color(255, 255, 255));
		dPanel.add(this.createItem(AnalysisResult.Metrics_D, result.getDistance()));

		subitemPanel.add(dPanel);

		JPanel balancePanel = new JPanel(new BorderLayout());
		balancePanel.setBorder(new TitledBorder(BundleUtil.getString(BundleUtil.Metrics_Balance)));
		balancePanel.setBackground(new java.awt.Color(255, 255, 255));
		balancePanel.add(this.createItem(AnalysisResult.Metrics_Balance, result.getBalance()));

		subitemPanel.add(balancePanel);

		JPanel encapsulationPanel = new JPanel(new BorderLayout());
		encapsulationPanel.setBorder(new TitledBorder(BundleUtil.getString(BundleUtil.Metrics_Encapsulation)));
		encapsulationPanel.setBackground(new java.awt.Color(255, 255, 255));
		encapsulationPanel.add(this.createItem(AnalysisResult.Metrics_Encapsulation, result.getEncapsulation()));

		subitemPanel.add(encapsulationPanel);

		JPanel relationRationalityPanel = new JPanel(new BorderLayout());
		relationRationalityPanel.setBorder(new TitledBorder(BundleUtil
				.getString(BundleUtil.Metrics_RelationRationality)));
		relationRationalityPanel.setBackground(new java.awt.Color(255, 255, 255));
		relationRationalityPanel.add(this.createItem(AnalysisResult.Metrics_RelationRationality,
				result.getRelationRationality()));

		subitemPanel.add(relationRationalityPanel);

		workspacePanel.add(subitemPanel);

		return workspacePanel;

	}

	private JPanel createItem(String itemName, final Float scoreValue) {

		JPanel itemPanel = new JPanel(new BorderLayout());
		itemPanel.setBackground(new java.awt.Color(255, 255, 255));
		itemPanel.setBorder(new EmptyBorder(2, 2, 2, 2));

		JPanel scorePanel = new JPanel(new GridLayout(2, 1));
		scorePanel.setBackground(new java.awt.Color(255, 255, 255));

		JPanel scoreItemPanel = new JPanel(new GridLayout(1, 4, 4, 0));
		scoreItemPanel.setBackground(new java.awt.Color(255, 255, 255));
		JLabel scoreTitle = new JLabel();
		scoreTitle.setFont(new java.awt.Font("宋体", 1, 18));
		String title = BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_Score) + ":";
		scoreTitle.setText(title);
		scoreItemPanel.add(scoreTitle);

		JLabel score = new JLabel();
		score.setFont(new java.awt.Font("宋体", 1, 18));

		if (scoreValue != null) {
			score.setText(MetricsFormat.toFormattedMetrics(scoreValue).toString());
		} else {
			score.setText(Scored.NoValue);
		}

		scoreItemPanel.add(score);
		JLabel scoreCompareLabel = this.getComparedLabel(scoreValue, itemName);
		if (scoreCompareLabel != null) {
			scoreItemPanel.add(scoreCompareLabel);
		}
		if (itemName.equals(AnalysisResult.Metrics_TotalScore)) {
			JLabel mm = new JLabel("MM");
			mm.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					MMDialog motive = new MMDialog(scoreValue);
					motive.setModal(true);
					motive.setVisible(true);
				}
			});
			JDependUIUtil.addClickTipEffect(mm);
			scoreItemPanel.add(mm);
		}
		scorePanel.add(scoreItemPanel);

		JLabel fullScore = new JLabel();
		fullScore.setBackground(new java.awt.Color(153, 153, 153));
		fullScore.setFont(new java.awt.Font("宋体", 0, 10));
		fullScore.setForeground(new java.awt.Color(204, 204, 204));
		if (itemName.equals(AnalysisResult.Metrics_TotalScore)) {
			fullScore.setText(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_FullScore) + ":"
					+ AnalysisResultScored.FullScore);
		} else if (itemName.equals(AnalysisResult.Metrics_RelationRationality)) {
			fullScore.setText(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_FullScore) + ":"
					+ AnalysisResultScored.RelationRationality);
		} else if (itemName.equals(AnalysisResult.Metrics_D)) {
			fullScore.setText(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_FullScore) + ":"
					+ AnalysisResultScored.Distance);
		} else if (itemName.equals(AnalysisResult.Metrics_Balance)) {
			fullScore.setText(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_FullScore) + ":"
					+ AnalysisResultScored.Balance);
		} else if (itemName.equals(AnalysisResult.Metrics_Encapsulation)) {
			fullScore.setText(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_FullScore) + ":"
					+ AnalysisResultScored.Encapsulation);
		}

		scorePanel.add(fullScore);

		itemPanel.add(BorderLayout.WEST, scorePanel);

		JPanel scoreScope = new JPanel();
		if (scorelist.size() > 0) {
			ScoreFacade.sort(scorelist, itemName);
			final ScoreInfo lScore = scorelist.get(0);
			final ScoreInfo hScore = scorelist.get(scorelist.size() - 1);

			scoreScope = this.createScope(lScore, hScore, itemName);
		}

		itemPanel.add(BorderLayout.EAST, scoreScope);

		itemPanel.add(BorderLayout.SOUTH, this.createAdvisePanel(itemName));

		return itemPanel;
	}

	private JLabel getComparedLabel(Object value, String metrics) {
		try {
			final CompareInfo info = AdjustHistory.getInstance().compare(new CompareObject(value, null, metrics) {
				@Override
				public Object getOriginalityValue(AnalysisResult result) {
					if (this.getMetrics().equals(AnalysisResult.Metrics_TotalScore)) {
						return result.getScore();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_D)) {
						return result.getDistance();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_Balance)) {
						return result.getBalance();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_RelationRationality)) {
						return result.getRelationRationality();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_Encapsulation)) {
						return result.getEncapsulation();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_LC)) {
						return result.getSummary().getLineCount();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_CN)) {
						return result.getSummary().getClassCount();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_ComponentCount)) {
						return result.getComponents().size();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_RelationCount)) {
						return result.getRelations().size();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_RelationComponentScale)) {
						return result.calRelationComponentScale();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_Coupling)) {
						return result.getSummary().getCoupling();
					} else if (this.getMetrics().equals(AnalysisResult.Metrics_Cohesion)) {
						return result.getSummary().getCohesion();
					}
					return null;
				}

				@Override
				public Boolean evaluate(int result, String metrics) {
					if (metrics.equals(AnalysisResult.Metrics_TotalScore)) {
						if (result < 0) {
							return false;
						} else {
							return true;
						}
					} else if (metrics.equals(AnalysisResult.Metrics_D)) {
						if (result < 0) {
							return false;
						} else {
							return true;
						}
					} else if (metrics.equals(AnalysisResult.Metrics_Balance)) {
						if (result < 0) {
							return false;
						} else {
							return true;
						}
					} else if (metrics.equals(AnalysisResult.Metrics_RelationRationality)) {
						if (result < 0) {
							return false;
						} else {
							return true;
						}
					} else if (metrics.equals(AnalysisResult.Metrics_Encapsulation)) {
						if (result < 0) {
							return false;
						} else {
							return true;
						}
					} else if (metrics.equals(AnalysisResult.Metrics_RelationComponentScale)) {
						if (result < 0) {
							return true;
						} else {
							return false;
						}
					} else if (metrics.equals(AnalysisResult.Metrics_Coupling)) {
						if (result < 0) {
							return true;
						} else {
							return false;
						}
					} else if (metrics.equals(AnalysisResult.Metrics_Cohesion)) {
						if (result < 0) {
							return false;
						} else {
							return true;
						}
					} else {
						return null;
					}
				}

			});

			if (info != null && info.isDiff()) {
				JLabel labelDirection = new JLabel() {
					@Override
					public String getToolTipText(MouseEvent e) {
						return "Originality:" + info.getOriginality();
					}
				};
				ToolTipManager.sharedInstance().registerComponent(labelDirection);
				CompareInfoWebWarpper warpper = new CompareInfoWebWarpper(info);
				labelDirection.setText(warpper.getCompare());
				labelDirection.setForeground(warpper.getDirectionColor());
				return labelDirection;
			} else {
				return null;
			}
		} catch (JDependException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	/**
	 * 左侧分数面板
	 * 
	 * @return
	 */
	private JComponent createLeftPanel() {

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setBackground(new java.awt.Color(255, 255, 255));
		leftPanel.add(BorderLayout.NORTH, this.createItem(AnalysisResult.Metrics_TotalScore, result.getScore()));

		JPanel otherPanel = new JPanel(new BorderLayout());
		otherPanel.setBackground(new java.awt.Color(255, 255, 255));
		JPanel descPanel = new JPanel(new GridLayout(7, 1));
		descPanel.setBackground(new java.awt.Color(255, 255, 255));
		JPanel panel = null;
		JLabel valuePanel = null;
		JLabel itemCompareLabel = null;

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel(BundleUtil.getString(BundleUtil.Metrics_LC) + ":"));
		panel.add(new JLabel("" + result.getSummary().getLineCount()));
		itemCompareLabel = this.getComparedLabel(result.getSummary().getLineCount(), AnalysisResult.Metrics_LC);
		if (itemCompareLabel != null) {
			panel.add(itemCompareLabel);
		}
		descPanel.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel(BundleUtil.getString(BundleUtil.Metrics_CN) + ":"));
		valuePanel = new JLabel("" + result.getSummary().getClassCount());
		JDependUIUtil.addClickTipEffect(valuePanel);
		valuePanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				frame.getResultPanel().setTab(2, 0);
			}
		});
		panel.add(valuePanel);
		itemCompareLabel = this.getComparedLabel(result.getSummary().getClassCount(), AnalysisResult.Metrics_CN);
		if (itemCompareLabel != null) {
			panel.add(itemCompareLabel);
		}
		descPanel.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel(BundleUtil.getString(BundleUtil.Metrics_ComponentCount) + ":"));
		valuePanel = new JLabel("" + result.getComponents().size());
		JDependUIUtil.addClickTipEffect(valuePanel);
		valuePanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				frame.getResultPanel().setTab(1, 0);
			}
		});
		panel.add(valuePanel);
		itemCompareLabel = this.getComparedLabel(result.getComponents().size(), AnalysisResult.Metrics_ComponentCount);
		if (itemCompareLabel != null) {
			panel.add(itemCompareLabel);
		}
		descPanel.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel(BundleUtil.getString(BundleUtil.Metrics_RelationCount) + ":"));
		valuePanel = new JLabel("" + result.getRelations().size());
		JDependUIUtil.addClickTipEffect(valuePanel);
		valuePanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				frame.getResultPanel().setTab(1, 1);
			}
		});
		panel.add(valuePanel);
		itemCompareLabel = this.getComparedLabel(result.getRelations().size(), AnalysisResult.Metrics_RelationCount);
		if (itemCompareLabel != null) {
			panel.add(itemCompareLabel);
		}
		descPanel.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel(BundleUtil.getString(BundleUtil.Metrics_RelationComponentScale) + ":"));
		panel.add(new JLabel("" + result.calRelationComponentScale()));
		itemCompareLabel = this.getComparedLabel(result.calRelationComponentScale(),
				AnalysisResult.Metrics_RelationComponentScale);
		if (itemCompareLabel != null) {
			panel.add(itemCompareLabel);
		}
		descPanel.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel(BundleUtil.getString(BundleUtil.Metrics_Coupling) + ":"));
		valuePanel = new JLabel("" + result.getSummary().getCoupling());
		JDependUIUtil.addClickTipEffect(valuePanel);
		valuePanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				CouplingDialog d = new CouplingDialog();
				d.setModal(true);
				d.setVisible(true);
			}
		});
		panel.add(valuePanel);
		itemCompareLabel = this.getComparedLabel(result.getSummary().getCoupling(), AnalysisResult.Metrics_Coupling);
		if (itemCompareLabel != null) {
			panel.add(itemCompareLabel);
		}
		descPanel.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel(BundleUtil.getString(BundleUtil.Metrics_Cohesion) + ":"));
		valuePanel = new JLabel("" + result.getSummary().getCohesion());
		JDependUIUtil.addClickTipEffect(valuePanel);
		valuePanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				CohesionDialog d = new CohesionDialog();
				d.setModal(true);
				d.setVisible(true);
			}
		});
		panel.add(valuePanel);
		itemCompareLabel = this.getComparedLabel(result.getSummary().getCohesion(), AnalysisResult.Metrics_Cohesion);
		if (itemCompareLabel != null) {
			panel.add(itemCompareLabel);
		}
		descPanel.add(panel);

		otherPanel.add(BorderLayout.SOUTH, descPanel);

		Map<String, String> diffElements = result.getDiffElements();
		if (diffElements != null && diffElements.size() > 0) {
			JPanel tipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			tipPanel.setBackground(new java.awt.Color(255, 255, 255));
			tipPanel.add(new JLabel(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_ElementChangeTip)));
			JLabel tipClickLabel = new JLabel(
					BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_ElementChangeTip_This));
			JDependUIUtil.addClickTipEffect(tipClickLabel);
			tipClickLabel.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					ChangedElementListDialog d = new ChangedElementListDialog(frame);
					d.setModal(true);
					d.setVisible(true);
				}
			});
			tipPanel.add(tipClickLabel);
			otherPanel.add(BorderLayout.NORTH, tipPanel);
		}

		JPanel contentpanel = new JPanel(new GridLayout(1, 2));
		contentpanel.add(otherPanel);
		contentpanel.add(this.createGraphScore());

		leftPanel.add(BorderLayout.CENTER, contentpanel);

		return leftPanel;
	}

	private JComponent createGraphScore() {
		GraphData graph = new GraphData();
		GraphDataItem item = null;
		Map<Object, Object> datas = null;

		if (this.result.getDistance() != null) {
			item = new GraphDataItem();
			item.setTitle(BundleUtil.getString(BundleUtil.Metrics_D));
			item.setType(GraphDataItem.PIE);
			datas = new HashMap<Object, Object>();
			datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_Score), this.result.getDistance()
					/ AnalysisResult.Distance);
			datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_ScoreDifference),
					1F - this.result.getDistance() / AnalysisResult.Distance);
			item.setDatas(datas);
			graph.addItem(item);
		}

		if (this.result.getBalance() != null) {
			item = new GraphDataItem();
			item.setTitle(BundleUtil.getString(BundleUtil.Metrics_Balance));
			item.setType(GraphDataItem.PIE);
			datas = new HashMap<Object, Object>();
			datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_Score), this.result.getBalance()
					/ AnalysisResult.Balance);
			datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_ScoreDifference),
					1F - this.result.getBalance() / AnalysisResult.Balance);
			item.setDatas(datas);
			graph.addItem(item);
		}

		if (this.result.getEncapsulation() != null) {
			item = new GraphDataItem();
			item.setTitle(BundleUtil.getString(BundleUtil.Metrics_Encapsulation));
			item.setType(GraphDataItem.PIE);
			datas = new HashMap<Object, Object>();
			datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_Score), this.result.getEncapsulation()
					/ AnalysisResult.Encapsulation);
			datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_ScoreDifference),
					1F - this.result.getEncapsulation() / AnalysisResult.Encapsulation);
			item.setDatas(datas);
			graph.addItem(item);
		}

		if (this.result.getRelationRationality() != null) {
			item = new GraphDataItem();
			item.setTitle(BundleUtil.getString(BundleUtil.Metrics_RelationRationality));
			item.setType(GraphDataItem.PIE);
			datas = new HashMap<Object, Object>();
			datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_Score), this.result.getRelationRationality()
					/ AnalysisResult.RelationRationality);
			datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_ScoreDifference),
					1F - this.result.getRelationRationality() / AnalysisResult.RelationRationality);
			item.setDatas(datas);
			graph.addItem(item);
		}

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(new java.awt.Color(255, 255, 255));
		try {
			GraphUtil.getInstance().setAddJScrollPane(false);
			contentPanel.add(GraphUtil.getInstance().createGraph(graph));
		} catch (JDependException e) {
			e.printStackTrace();
		}

		return contentPanel;
	}

	private JPanel createAdvisePanel(String itemName) {

		JPanel advisePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		advisePanel.setBackground(new java.awt.Color(255, 255, 255));

		WisdomAnalysisResult wisdomAnalysisResult = new WisdomAnalysisResult(result);

		JLabel adviseLabel = null;
		JLabel descLabel = null;
		if (itemName.equals(AnalysisResult.Metrics_TotalScore)) {
			adviseLabel = new JLabel();
			try {
				AdviseInfo advise = wisdomAnalysisResult.getAdvise(StructureCategory.LowScoreItemIdentifier);
				if (advise != null) {
					adviseLabel.setText(advise.getDesc() + ReportConstant.toMetricsName(advise.getComponentNameInfo()));
					advisePanel.add(adviseLabel);
				}
			} catch (JDependException e) {
				e.printStackTrace();
			}
		} else if (itemName.equals(AnalysisResult.Metrics_D)) {
			try {
				AdviseInfo advise = wisdomAnalysisResult.getAdvise(StructureCategory.DDomainAnalysis);
				if (advise != null) {
					descLabel = new JLabel();
					descLabel.setText(advise.getDesc());
					adviseLabel = new JLabel();
					adviseLabel.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
					adviseLabel.setText(advise.getComponentNameInfo());
					JDependUIUtil.addClickTipEffect(adviseLabel);
					adviseLabel.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
							jdepend.model.Component component = JDependUnitMgr.getInstance().getResult()
									.getTheComponent(((JLabel) evt.getSource()).getText());
							ComponentDetailDialog d = new ComponentDetailDialog(component);
							d.setModal(true);
							d.setVisible(true);
						}
					});
					advisePanel.add(adviseLabel);
					advisePanel.add(descLabel);
				}
			} catch (JDependException e) {
				e.printStackTrace();
			}
		} else if (itemName.equals(AnalysisResult.Metrics_Balance)) {
			try {
				AdviseInfo advise = wisdomAnalysisResult.getAdvise(StructureCategory.CohesionDomainAnalysis);
				if (advise != null) {
					descLabel = new JLabel();
					descLabel.setText(advise.getDesc());
					adviseLabel = new JLabel();
					adviseLabel.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
					adviseLabel.setText(advise.getComponentNameInfo());
					JDependUIUtil.addClickTipEffect(adviseLabel);
					adviseLabel.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
							jdepend.model.Component component = JDependUnitMgr.getInstance().getResult()
									.getTheComponent(((JLabel) evt.getSource()).getText());
							BalanceComponentDialog d = new BalanceComponentDialog(frame, component);
							d.setModal(true);
							d.setVisible(true);
						}
					});
					advisePanel.add(descLabel);
					advisePanel.add(adviseLabel);
				}
			} catch (JDependException e) {
				e.printStackTrace();
			}
		} else if (itemName.equals(AnalysisResult.Metrics_Encapsulation)) {
			try {
				final AdviseInfo advise = wisdomAnalysisResult.getAdvise(StructureCategory.EncapsulationDomainAnalysis);
				if (advise != null) {
					descLabel = new JLabel();
					descLabel.setText(advise.getDesc());
					adviseLabel = new JLabel();
					adviseLabel.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
					adviseLabel.setText(advise.getComponentNameInfo());
					JDependUIUtil.addClickTipEffect(adviseLabel);
					adviseLabel.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
							jdepend.model.Component component = JDependUnitMgr.getInstance().getResult()
									.getTheComponent(advise.getComponentNameInfo());
							ClassListDialog d = new ClassListDialog(frame, component);
							d.setModal(true);
							d.setVisible(true);
						}
					});
					advisePanel.add(descLabel);
					advisePanel.add(adviseLabel);
				}
			} catch (JDependException e) {
				e.printStackTrace();
			}
		} else if (itemName.equals(AnalysisResult.Metrics_RelationRationality)) {
			Float rs = result.getAttentionRelationScale();
			adviseLabel = new JLabel();
			if (MathUtil.isZero(rs)) {
				adviseLabel.setText(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_RelationNormal));
			} else {
				adviseLabel.setText(BundleUtil.getString(BundleUtil.Metrics_AttentionRelationScale) + ":"
						+ MetricsFormat.toFormattedPercent(rs));
				JDependUIUtil.addClickTipEffect(adviseLabel);
				adviseLabel.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						frame.getResultPanel().setTab(1, 1);
					}
				});
			}
			advisePanel.add(adviseLabel);
		}

		return advisePanel;

	}

	private JPanel createScope(ScoreInfo lScoreInfo, ScoreInfo hScoreInfo, String itemName) {
		Float lScore = null;
		String lScoreId;
		Float hScore = null;
		String hScoreId;

		lScoreId = lScoreInfo.id;
		hScoreId = hScoreInfo.id;
		if (itemName.equals(AnalysisResult.Metrics_TotalScore)) {
			lScore = lScoreInfo.score;
			hScore = hScoreInfo.score;
		} else if (itemName.equals(AnalysisResult.Metrics_D)) {
			lScore = lScoreInfo.distance;
			hScore = hScoreInfo.distance;
		} else if (itemName.equals(AnalysisResult.Metrics_Balance)) {
			lScore = lScoreInfo.balance;
			hScore = hScoreInfo.balance;
		} else if (itemName.equals(AnalysisResult.Metrics_Encapsulation)) {
			lScore = lScoreInfo.encapsulation;
			hScore = hScoreInfo.encapsulation;
		} else if (itemName.equals(AnalysisResult.Metrics_RelationRationality)) {
			lScore = lScoreInfo.relation;
			hScore = hScoreInfo.relation;
		}

		return this.createScope(lScore, lScoreId, hScore, hScoreId);
	}

	private JPanel createScope(final Float lScore, final String lScoreId, final Float hScore, final String hScoreId) {
		JPanel scoreScope = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		scoreScope.setLayout(flowLayout);
		scoreScope.setBackground(new java.awt.Color(255, 255, 255));
		JLabel scopeTitle = new JLabel(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_ExistingScoreScope) + ":");
		scopeTitle.setForeground(new java.awt.Color(204, 204, 204));
		scoreScope.add(scopeTitle);
		JLabel lScoreJLablel = new JLabel(String.valueOf(lScore));
		lScoreJLablel.setForeground(new java.awt.Color(204, 204, 204));
		lScoreJLablel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		scoreScope.add(lScoreJLablel);
		lScoreJLablel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				try {
					scoreScopeMouseClicked(lScoreId);
				} catch (JDependException e) {
					frame.getResultPanel().showError(e);
				}
			}
		});
		JLabel interval = new JLabel("~");
		interval.setForeground(new java.awt.Color(204, 204, 204));
		scoreScope.add(interval);
		JLabel hScoreJLablel = new JLabel(String.valueOf(hScore));
		hScoreJLablel.setForeground(new java.awt.Color(204, 204, 204));
		hScoreJLablel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		hScoreJLablel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				try {
					scoreScopeMouseClicked(hScoreId);
				} catch (JDependException e) {
					frame.getResultPanel().showError(e);
				}
			}
		});
		scoreScope.add(hScoreJLablel);

		return scoreScope;
	}

	private void scoreScopeMouseClicked(String id) throws JDependException {
		AnalysisResult result = ScoreFacade.getTheResult(id);
		JDependUnitMgr.getInstance().setResult(result);
		frame.getResultPanelWrapper().showResults(false);
	}
}
