package jdepend.ui.result;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.JDependUIUtil;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.GraphDataItem;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.knowledge.capacity.Capacity;
import jdepend.knowledge.capacity.CapacityMgr;
import jdepend.model.result.AnalysisResult;

public final class CapacityPanel extends SubResultTabPanel {

	@Override
	protected void init(final AnalysisResult result) {

		this.setBackground(new java.awt.Color(255, 255, 255));
		this.setBorder(new EmptyBorder(2, 2, 2, 2));

		final Capacity capacity = CapacityMgr.getInstance().getCapacity(result);

		JPanel capacityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		capacityPanel.setBorder(new TitledBorder(BundleUtil
				.getString(BundleUtil.Metrics_Capacity)));
		capacityPanel.setBackground(new java.awt.Color(255, 255, 255));

		JLabel capacityLabel = new JLabel();
		capacityLabel.setFont(new java.awt.Font("宋体", 1, 18));
		capacityLabel.setText(BundleUtil
				.getString(BundleUtil.ClientWin_ScorePanel_Score)
				+ ":"
				+ capacity.getLevel());

		capacityPanel.add(capacityLabel);

		this.add(BorderLayout.NORTH, capacityPanel);

		JPanel complexityPanel = new JPanel(new GridLayout(2, 1));
		complexityPanel.setBorder(new TitledBorder(BundleUtil
				.getString(BundleUtil.Metrics_Complexity)));
		complexityPanel.setBackground(new java.awt.Color(255, 255, 255));

		JLabel complexityLabel = new JLabel();
		complexityLabel.setFont(new java.awt.Font("宋体", 1, 18));
		complexityLabel.setText(BundleUtil
				.getString(BundleUtil.ClientWin_ScorePanel_Value)
				+ ":"
				+ capacity.getComplexity().getValue() + "                ");
		complexityPanel.add(complexityLabel);

		GridLayout gLayout = new GridLayout(4, 1);
		JPanel complexityWPanel = new JPanel(gLayout);
		complexityWPanel.setBackground(new java.awt.Color(255, 255, 255));
		JLabel relationsLabel = new JLabel();
		relationsLabel.setText(BundleUtil
				.getString(BundleUtil.Metrics_RelationCount)
				+ ":"
				+ capacity.getComplexity().getRelations());
		complexityWPanel.add(relationsLabel);

		JLabel componentsLabel = new JLabel();
		componentsLabel.setText(BundleUtil
				.getString(BundleUtil.Metrics_ComponentCount)
				+ ":"
				+ capacity.getComplexity().getComponents());
		complexityWPanel.add(componentsLabel);

		JLabel classesLabel = new JLabel();
		classesLabel.setText(BundleUtil.getString(BundleUtil.Metrics_CN) + ":"
				+ capacity.getComplexity().getClasses());
		complexityWPanel.add(classesLabel);

		complexityPanel.add(complexityWPanel);

		this.add(BorderLayout.WEST, complexityPanel);

		JPanel skillPanel = new JPanel(new BorderLayout());
		skillPanel.setBorder(new TitledBorder(BundleUtil
				.getString(BundleUtil.Metrics_Skill)));
		skillPanel.setBackground(new java.awt.Color(255, 255, 255));

		JLabel skillLabel = new JLabel();
		skillLabel.setFont(new java.awt.Font("宋体", 1, 18));
		skillLabel.setText(BundleUtil
				.getString(BundleUtil.ClientWin_ScorePanel_Score)
				+ ":"
				+ capacity.getSkill().getLevel());
		skillPanel.add(BorderLayout.NORTH, skillLabel);

		JPanel skillWPanel = new JPanel(new GridLayout(1, 2));
		skillWPanel.setBackground(new java.awt.Color(255, 255, 255));

		gLayout = new GridLayout(7, 1);
		JPanel skillLPanel = new JPanel(gLayout);
		skillLPanel.setBackground(new java.awt.Color(255, 255, 255));

		skillLPanel.add(new JLabel(""));

		JPanel panel;
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel(BundleUtil
				.getString(BundleUtil.Metrics_PatternCount) + ":"));
		JLabel valuePanel = new JLabel("" + capacity.getSkill().getPatterns());
		JDependUIUtil.addClickTipEffect(valuePanel);
		valuePanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				PatternListDialog d = new PatternListDialog(result);
				d.setModal(true);
				d.setVisible(true);
			}
		});
		panel.add(valuePanel);
		skillLPanel.add(panel);

		JLabel itemLabel;

		itemLabel = new JLabel();
		itemLabel.setText(BundleUtil
				.getString(BundleUtil.Metrics_AverageClassSize)
				+ ":"
				+ capacity.getSkill().getClassSize());
		skillLPanel.add(itemLabel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel("超过500行类的比例："));
		valuePanel = new JLabel(""
				+ MetricsFormat.toFormattedPercent(capacity.getSkill()
						.getBigClassScale()));
		JDependUIUtil.addClickTipEffect(valuePanel);
		valuePanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				BigClassDialog d = new BigClassDialog(capacity.getSkill()
						.getBigClasses());
				d.setModal(true);
				d.setVisible(true);
			}
		});
		panel.add(valuePanel);
		skillLPanel.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel("超过6个参数的方法的比例："));
		valuePanel = new JLabel(""
				+ MetricsFormat.toFormattedPercent(capacity.getSkill()
						.getBigArgumentMethodScale()));
		JDependUIUtil.addClickTipEffect(valuePanel);
		valuePanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				BigArgumentMethodDialog d = new BigArgumentMethodDialog(
						capacity.getSkill().getBigArgumentMethods());
				d.setModal(true);
				d.setVisible(true);
			}
		});
		panel.add(valuePanel);
		skillLPanel.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(new java.awt.Color(255, 255, 255));
		panel.add(new JLabel("超过200行的方法的比例："));
		valuePanel = new JLabel(""
				+ MetricsFormat.toFormattedPercent(capacity.getSkill()
						.getBigLineCountMethodScale()));
		JDependUIUtil.addClickTipEffect(valuePanel);
		valuePanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				BigLineCountMethodDialog d = new BigLineCountMethodDialog(
						capacity.getSkill().getBigLineCountMethods());
				d.setModal(true);
				d.setVisible(true);
			}
		});
		panel.add(valuePanel);
		skillLPanel.add(panel);

		skillWPanel.add(skillLPanel);

		try {
			skillWPanel.add(createClassRelations(capacity.getSkill()
					.getClassRelations()));
		} catch (JDependException e) {
			e.printStackTrace();
		}

		skillPanel.add(BorderLayout.CENTER, skillWPanel);

		this.add(BorderLayout.CENTER, skillPanel);

	}

	private JComponent createClassRelations(Map<Object, Object> data)
			throws JDependException {

		GraphData graphData = new GraphData();
		GraphDataItem item = new GraphDataItem();
		item.setTitle("按关系类型分类比例");
		item.setType(GraphDataItem.PIE);
		item.setDatas(data);
		graphData.addItem(item);
		
		GraphUtil.getInstance().setAddJScrollPane(false);

		return GraphUtil.getInstance().createGraph(graphData);
	}
}
