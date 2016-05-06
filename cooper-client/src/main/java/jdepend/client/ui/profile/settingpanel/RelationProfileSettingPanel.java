package jdepend.client.ui.profile.settingpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.Relation;
import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.model.RelationProfile;
import jdepend.service.profile.scope.ProfileScopeFacade;

public class RelationProfileSettingPanel extends ModelProfileSettingPanel {

	private RelationProfile relationProfile;

	private JTextField cycleDependField;
	private JTextField SDPField;
	private JTextField componentLayerField;
	private JTextField mutualDependField;

	private JTextField SDPDifferenceField;

	public RelationProfileSettingPanel(RelationProfile relationProfile) {
		this.relationProfile = relationProfile;

		this.init();
		this.refresh();
	}

	private void init() {
		this.add(this.leftPanel());
		this.add(this.rightPanel());
	}

	protected Component leftPanel() {

		JPanel content = new JPanel(new BorderLayout());

		JPanel left = new JPanel(new BorderLayout());

		JPanel problemRelationPanel = new JPanel(new GridLayout(4, 3));
		problemRelationPanel.setBorder(new TitledBorder("问题关系类型权值"));
		problemRelationPanel.setPreferredSize(new Dimension(this.getWidth(), 120));

		problemRelationPanel.add(new JLabel("循环依赖"));

		cycleDependField = new JTextField();
		problemRelationPanel.add(cycleDependField);
		problemRelationPanel.add(new JLabel("取值范围：1~4"));

		problemRelationPanel.add(new JLabel("违反稳定依赖原则"));

		SDPField = new JTextField();
		problemRelationPanel.add(SDPField);
		problemRelationPanel.add(new JLabel("取值范围：1~4"));

		problemRelationPanel.add(new JLabel("彼此依赖"));

		mutualDependField = new JTextField();
		problemRelationPanel.add(mutualDependField);
		problemRelationPanel.add(new JLabel("取值范围：1~4"));
		
		problemRelationPanel.add(new JLabel("下层组件依赖了上层组件"));

		componentLayerField = new JTextField();
		problemRelationPanel.add(componentLayerField);
		problemRelationPanel.add(new JLabel("取值范围：1~4"));

		left.add(BorderLayout.NORTH, problemRelationPanel);

		JPanel SDPDifferencePanel = new JPanel(new GridLayout(1, 3));
		SDPDifferencePanel.setBorder(new TitledBorder(""));
		SDPDifferencePanel.setPreferredSize(new Dimension(this.getWidth(), 30));

		SDPDifferencePanel.add(new JLabel("违反稳定依赖原则的阈值"));
		
		SDPDifferenceField = new JTextField();
		SDPDifferencePanel.add(SDPDifferenceField);
		SDPDifferencePanel.add(new JLabel("取值范围：0~1"));

		left.add(BorderLayout.CENTER, SDPDifferencePanel);

		content.add(BorderLayout.NORTH, left);
		content.add(BorderLayout.CENTER, this.getOtherPanel());

		return content;
	}

	@Override
	protected void restore() {
		this.relationProfile = ProfileScopeFacade.getInstance().getDefaultProfileFacade().getRelationProfile();
		this.refresh();
	}

	@Override
	public void refresh() {
		Map<String, Integer> problemRelations = this.relationProfile.getProblemRelations();

		cycleDependField.setText(String.valueOf(problemRelations.get(Relation.CycleDependAttentionType)));
		SDPField.setText(String.valueOf(problemRelations.get(Relation.SDPAttentionType)));
		componentLayerField.setText(String.valueOf(problemRelations.get(Relation.ComponentLayerAttentionType)));
		mutualDependField.setText(String.valueOf(problemRelations.get(Relation.MutualDependAttentionType)));
		
		SDPDifferenceField.setText(String.valueOf(this.relationProfile.getSDPDifference()));
	}

	@Override
	public void validateData() throws ProfileValidateException {
		int cycleDepend = Integer.valueOf(cycleDependField.getText());
		if (cycleDepend < 1 || cycleDepend > 4) {
			throw new ProfileValidateException("循环依赖权值超出了范围！", 3);
		}

		int SDP = Integer.valueOf(SDPField.getText());
		if (SDP < 1 || SDP > 4) {
			throw new ProfileValidateException("违反稳定依赖原则权值超出了范围！", 3);
		}

		int componentLayer = Integer.valueOf(componentLayerField.getText());
		if (componentLayer < 1 || componentLayer > 4) {
			throw new ProfileValidateException("下层组件依赖了上层组件权值超出了范围！", 3);
		}

		int mutualDepend = Integer.valueOf(mutualDependField.getText());
		if (mutualDepend < 1 || mutualDepend > 4) {
			throw new ProfileValidateException("彼此依赖权值超出了范围！", 3);
		}
		
		float SDPDifference = Float.valueOf(SDPDifferenceField.getText());
		if (SDPDifference < 0 || SDPDifference > 1) {
			throw new ProfileValidateException("违反稳定依赖原则的阈值超出了范围！", 3);
		}

	}

	@Override
	public void save(MaintainProfileFacade maintainProfileFacade) {

		int cycleDepend = Integer.valueOf(cycleDependField.getText());
		int SDP = Integer.valueOf(SDPField.getText());
		int componentLayer = Integer.valueOf(componentLayerField.getText());
		int mutualDepend = Integer.valueOf(mutualDependField.getText());
		
		float SDPDifference = Float.valueOf(SDPDifferenceField.getText());

		Map<String, Integer> problemRelations = new HashMap<String, Integer>();

		problemRelations.put(Relation.CycleDependAttentionType, cycleDepend);
		problemRelations.put(Relation.SDPAttentionType, SDP);
		problemRelations.put(Relation.ComponentLayerAttentionType, componentLayer);
		problemRelations.put(Relation.MutualDependAttentionType, mutualDepend);

		RelationProfile newRelationProfile = new RelationProfile();
		
		newRelationProfile.setProblemRelations(problemRelations);
		newRelationProfile.setSDPDifference(SDPDifference);
		
		maintainProfileFacade.setRelationProfile(newRelationProfile);
	}

	@Override
	protected String getExplain() {
		return relationProfile.getExplain();
	}

}
