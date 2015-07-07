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

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.model.RelationProfile;

public class RelationProfileSettingPanel extends ModelProfileSettingPanel {

	private RelationProfile relationProfile;

	private JTextField cycleDependField;
	private JTextField SDPField;
	private JTextField componentLayerField;
	private JTextField mutualDependField;

	public RelationProfileSettingPanel(RelationProfile relationProfile) {
		this.relationProfile = relationProfile;

		this.add(this.leftPanel());
		this.add(this.rightPanel());

	}

	protected Component leftPanel() {

		Map<String, Integer> problemRelations = this.relationProfile.getProblemRelations();

		JPanel content = new JPanel(new BorderLayout());

		JPanel left = new JPanel(new GridLayout(4, 3));
		left.setPreferredSize(new Dimension(this.getWidth(), 100));

		left.add(new JLabel(RelationProfile.CycleDependAttentionType));

		cycleDependField = new JTextField();
		cycleDependField.setText(String.valueOf(problemRelations.get(RelationProfile.CycleDependAttentionType)));

		left.add(cycleDependField);
		left.add(new JLabel("取值范围：1~4"));

		left.add(new JLabel(RelationProfile.SDPAttentionType));

		SDPField = new JTextField();
		SDPField.setText(String.valueOf(problemRelations.get(RelationProfile.SDPAttentionType)));

		left.add(SDPField);
		left.add(new JLabel("取值范围：1~4"));

		left.add(new JLabel(RelationProfile.ComponentLayerAttentionType));

		componentLayerField = new JTextField();
		componentLayerField.setText(String.valueOf(problemRelations.get(RelationProfile.ComponentLayerAttentionType)));

		left.add(componentLayerField);
		left.add(new JLabel("取值范围：1~4"));

		left.add(new JLabel(RelationProfile.MutualDependAttentionType));

		mutualDependField = new JTextField();
		mutualDependField.setText(String.valueOf(problemRelations.get(RelationProfile.MutualDependAttentionType)));

		left.add(mutualDependField);
		left.add(new JLabel("取值范围：1~4"));

		content.add(BorderLayout.NORTH, left);

		JPanel otherPanel = new JPanel();
		content.add(BorderLayout.CENTER, otherPanel);

		return content;
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

	}

	@Override
	public void save(MaintainProfileFacade maintainProfileFacade) {

		int cycleDepend = Integer.valueOf(cycleDependField.getText());
		int SDP = Integer.valueOf(SDPField.getText());
		int componentLayer = Integer.valueOf(componentLayerField.getText());
		int mutualDepend = Integer.valueOf(mutualDependField.getText());

		Map<String, Integer> problemRelations = new HashMap<String, Integer>();

		problemRelations.put(RelationProfile.CycleDependAttentionType, cycleDepend);
		problemRelations.put(RelationProfile.SDPAttentionType, SDP);
		problemRelations.put(RelationProfile.ComponentLayerAttentionType, componentLayer);
		problemRelations.put(RelationProfile.MutualDependAttentionType, mutualDepend);

		RelationProfile newRelationProfile = new RelationProfile();
		newRelationProfile.setProblemRelations(problemRelations);

		maintainProfileFacade.setRelationProfile(newRelationProfile);
	}

	@Override
	protected String getExplain() {
		return relationProfile.getExplain();
	}

}
