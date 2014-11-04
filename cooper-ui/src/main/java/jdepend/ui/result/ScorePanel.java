package jdepend.ui.result;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import jdepend.core.command.CommandAdapterMgr;
import jdepend.core.score.ScoreByItemComparator;
import jdepend.core.score.ScoreInfo;
import jdepend.core.score.ScoreRepository;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.JDependUIUtil;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.GraphDataItem;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MathUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.framework.util.VersionUtil;
import jdepend.knowledge.AdviseInfo;
import jdepend.knowledge.ExpertFactory;
import jdepend.knowledge.Structure;
import jdepend.knowledge.StructureCategory;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisResultScored;
import jdepend.report.ui.ClassListDialog;
import jdepend.report.ui.CohesionDialog;
import jdepend.report.ui.CouplingDialog;
import jdepend.report.util.ReportConstant;
import jdepend.ui.JDependCooper;
import jdepend.ui.componentconf.ChangedElementListDialog;
import jdepend.ui.framework.CompareInfoWebWarpper;
import jdepend.ui.motive.MotiveDialog;
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
			scorelist = ScoreRepository.getScoreList();
		} catch (JDependException e) {
			e.printStackTrace();
		}
		initComponents();
	}

	private void initComponents() {

		this.setBackground(new java.awt.Color(255, 255, 255));
		this.setBorder(new EmptyBorder(2, 2, 2, 2));

		this.add(BorderLayout.NORTH, this.createExecuteInfo());

		this.add(BorderLayout.CENTER, this.createWorkspacePanel());
	}

	private JComponent createExecuteInfo() {
		JLabel executeInfo = new JLabel();

		executeInfo.setFont(new java.awt.Font("宋体", 0, 10));
		executeInfo.setForeground(new java.awt.Color(204, 204, 204));
		executeInfo.setText(BundleUtil.getString(BundleUtil.Analysis_Time) + ":"
				+ this.result.getRunningContext().getAnalyseDate() + " V" + VersionUtil.getVersion() + " BuildDate:"
				+ VersionUtil.getBuildDate() + " Group:" + this.result.getRunningContext().getGroup() + " Command:"
				+ this.result.getRunningContext().getCommand());

		return executeInfo;
	}

	private JPanel createWorkspacePanel() {

		JPanel workspacePanel = new JPanel(new GridLayout(1, 2));
		workspacePanel.setBackground(new java.awt.Color(255, 255, 255));

		JPanel leftPanel = new JPanel(new BorderLayout());

		JPanel scorePanel = new JPanel(new BorderLayout());
		scorePanel.setBorder(new TitledBorder(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_TotalScore)));
		scorePanel.setBackground(new java.awt.Color(255, 255, 255));

		scorePanel.add(this.createScorePanel());

		leftPanel.add(BorderLayout.CENTER, scorePanel);

		workspacePanel.add(leftPanel);

		JPanel subitemPanel = new JPanel(new GridLayout(4, 1));
		subitemPanel.setBackground(new java.awt.Color(255, 255, 255));

		JPanel dPanel = new JPanel(new BorderLayout());
		dPanel.setBorder(new TitledBorder(BundleUtil.getString(BundleUtil.Metrics_D)));
		dPanel.setBackground(new java.awt.Color(255, 255, 255));
		dPanel.add(this.createItem(AnalysisResult.Metrics_D, result.getD()));

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

	private JPanel createItem(String itemName, Float scoreValue) {

		JPanel itemPanel = new JPanel(new BorderLayout());
		itemPanel.setBackground(new java.awt.Color(255, 255, 255));
		itemPanel.setBorder(new EmptyBorder(2, 2, 2, 2));

		JPanel scorePanel = new JPanel(new GridLayout(2, 1));
		scorePanel.setBackground(new java.awt.Color(255, 255, 255));

		JPanel scoreItemPanel = new JPanel(new GridLayout(1, 2, 4, 0));
		scoreItemPanel.setBackground(new java.awt.Color(255, 255, 255));
		JLabel score = new JLabel();
		score.setFont(new java.awt.Font("宋体", 1, 18));
		String title = null;
		if (itemName.equals(AnalysisResult.Metrics_OO)) {
			title = itemName + ":";
		} else {
			title = BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_Score) + ":";
		}
		score.setText(title + MetricsFormat.toFormattedMetrics(scoreValue));
		if (itemName.equals(AnalysisResult.Metrics_TotalScore)) {
			score.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					MotiveDialog motive = new MotiveDialog(frame);
					motive.setModal(true);
					motive.setVisible(true);
				}
			});
			JDependUIUtil.addClickTipEffect(score);
		}

		scoreItemPanel.add(score);
		JLabel scoreCompareLabel = this.getComparedLabel(scoreValue, itemName);
		if (scoreCompareLabel != null) {
			scoreItemPanel.add(scoreCompareLabel);
		}
		scorePanel.add(scoreItemPanel);

		JLabel fullScore = new JLabel();
		fullScore.setBackground(new java.awt.Color(153, 153, 153));
		fullScore.setFont(new java.awt.Font("宋体", 0, 10));
		fullScore.setForeground(new java.awt.Color(204, 204, 204));
		if (itemName.equals(AnalysisResult.Metrics_OO)) {
			fullScore.setText(BundleUtil.getString(BundleUtil.Metrics_OO_Desc));
		} else if (itemName.equals(AnalysisResult.Metrics_TotalScore)) {
			fullScore.setText(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_FullScore) + ":"
					+ AnalysisResultScored.FullScore);
		} else if (itemName.equals(AnalysisResult.Metrics_RelationRationality)) {
			fullScore.setText(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_FullScore) + ":"
					+ AnalysisResultScored.RelationRationality);
		} else if (itemName.equals(AnalysisResult.Metrics_D)) {
			fullScore.setText(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_FullScore) + ":"
					+ AnalysisResultScored.D);
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
			Collections.sort(scorelist, new ScoreByItemComparator(itemName));
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
						return result.getD();
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
	private JComponent createScorePanel() {

		JPanel scorePanel = new JPanel(new BorderLayout());
		scorePanel.setBackground(new java.awt.Color(255, 255, 255));
		scorePanel.add(BorderLayout.NORTH, this.createItem(AnalysisResult.Metrics_TotalScore, result.getScore()));

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
				ClassListDialog d = new ClassListDialog(frame);
				d.setModal(true);
				d.setVisible(true);
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

		scorePanel.add(BorderLayout.CENTER, contentpanel);

		return scorePanel;
	}

	private JComponent createGraphScore() {
		GraphData graph = new GraphData();

		GraphDataItem item = null;
		Map<Object, Object> datas = null;
		item = new GraphDataItem();
		item.setTitle(BundleUtil.getString(BundleUtil.Metrics_D));
		item.setType(GraphDataItem.PIE);
		datas = new HashMap<Object, Object>();
		datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_Score), this.result.getD() / AnalysisResult.D);
		datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_ScoreDifference), 1F - this.result.getD()
				/ AnalysisResult.D);
		item.setDatas(datas);
		graph.addItem(item);

		item = new GraphDataItem();
		item.setTitle(BundleUtil.getString(BundleUtil.Metrics_Balance));
		item.setType(GraphDataItem.PIE);
		datas = new HashMap<Object, Object>();
		datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_Score), this.result.getBalance()
				/ AnalysisResult.Balance);
		datas.put(BundleUtil.getString(BundleUtil.ClientWin_ScorePanel_ScoreDifference), 1F - this.result.getBalance()
				/ AnalysisResult.Balance);
		item.setDatas(datas);
		graph.addItem(item);

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

		JLabel adviseLabel = null;
		JLabel descLabel = null;
		Structure structure = null;
		if (itemName.equals(AnalysisResult.Metrics_TotalScore)) {
			adviseLabel = new JLabel();
			structure = new Structure();
			structure.setCategory(StructureCategory.LowScoreItemIdentifier);
			structure.setData(result);
			try {
				AdviseInfo advise = new ExpertFactory().createExpert().advise(structure);
				if (advise != null) {
					adviseLabel.setText(advise.getDesc() + ReportConstant.toMetricsName(advise.getComponentNameInfo()));
					advisePanel.add(adviseLabel);
				}
			} catch (JDependException e) {
				e.printStackTrace();
			}
		} else if (itemName.equals(AnalysisResult.Metrics_D)) {
			structure = new Structure();
			structure.setCategory(StructureCategory.DDomainAnalysis);
			structure.setData(result);
			try {
				AdviseInfo advise = new ExpertFactory().createExpert().advise(structure);
				if (advise != null) {
					descLabel = new JLabel();
					descLabel.setText(advise.getDesc());
					adviseLabel = new JLabel();
					adviseLabel.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
					adviseLabel.setText(advise.getComponentNameInfo());
					JDependUIUtil.addClickTipEffect(adviseLabel);
					adviseLabel.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
							JDependUnitDetailDialog d = new JDependUnitDetailDialog(((JLabel) evt.getSource())
									.getText());
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
			structure = new Structure();
			structure.setCategory(StructureCategory.CohesionDomainAnalysis);
			structure.setData(result);
			try {
				AdviseInfo advise = new ExpertFactory().createExpert().advise(structure);
				if (advise != null) {
					descLabel = new JLabel();
					descLabel.setText(advise.getDesc());
					adviseLabel = new JLabel();
					adviseLabel.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
					adviseLabel.setText(advise.getComponentNameInfo());
					JDependUIUtil.addClickTipEffect(adviseLabel);
					adviseLabel.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
							CohesionDialog d = new CohesionDialog(((JLabel) evt.getSource()).getText());
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
			structure = new Structure();
			structure.setCategory(StructureCategory.EncapsulationDomainAnalysis);
			structure.setData(result);
			try {
				final AdviseInfo advise = new ExpertFactory().createExpert().advise(structure);
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
						frame.getResultPanel().setTab(1, 0);
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
			lScore = lScoreInfo.d;
			hScore = hScoreInfo.d;
		} else if (itemName.equals(AnalysisResult.Metrics_Balance)) {
			lScore = lScoreInfo.balance;
			hScore = hScoreInfo.balance;
		} else if (itemName.equals(AnalysisResult.Metrics_Encapsulation)) {
			lScore = lScoreInfo.encapsulation;
			hScore = hScoreInfo.encapsulation;
		} else if (itemName.equals(AnalysisResult.Metrics_RelationRationality)) {
			lScore = lScoreInfo.relation;
			hScore = hScoreInfo.relation;
		} else if (itemName.equals(AnalysisResult.Metrics_OO)) {
			lScore = lScoreInfo.oo;
			hScore = hScoreInfo.oo;
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
		AnalysisResult result = ScoreRepository.getTheResult(id);
		String group = result.getRunningContext().getGroup();
		String command = result.getRunningContext().getCommand();
		if (!CommandAdapterMgr.getCurrentGroup().equals(group)
				|| !CommandAdapterMgr.getCurrentCommand().equals(command)) {
			JDependUnitMgr.getInstance().setResult(result);
			CommandAdapterMgr.setCurrentGroup(group);
			CommandAdapterMgr.setCurrentCommand(command);
			frame.getResultPanel().showResults();
		}
	}
}
