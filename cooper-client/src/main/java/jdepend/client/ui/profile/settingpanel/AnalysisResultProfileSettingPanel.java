package jdepend.client.ui.profile.settingpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.service.profile.scope.ProfileScopeFacade;

public class AnalysisResultProfileSettingPanel extends ModelProfileSettingPanel {

	private AnalysisResultProfile analysisResultProfile;

	private JTextField distanceField;
	private JTextField balanceField;
	private JTextField encapsulationField;
	private JTextField relationRationalityField;

	private JCheckBox componentWeightCheckBox;

	public AnalysisResultProfileSettingPanel(AnalysisResultProfile analysisResultProfile) {
		this.analysisResultProfile = analysisResultProfile;

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

		JPanel scoreWeightPanel = new JPanel(new GridLayout(4, 3));
		scoreWeightPanel.setBorder(new TitledBorder("分项所占权值"));
		scoreWeightPanel.setPreferredSize(new Dimension(this.getWidth(), 120));

		scoreWeightPanel.add(new JLabel("抽象程度合理性比例："));

		distanceField = new JTextField();
		scoreWeightPanel.add(distanceField);
		scoreWeightPanel.add(new JLabel("取值范围：0~100"));

		scoreWeightPanel.add(new JLabel("内聚性比例："));

		balanceField = new JTextField();
		scoreWeightPanel.add(balanceField);
		scoreWeightPanel.add(new JLabel("取值范围：0~100"));

		scoreWeightPanel.add(new JLabel("封装性比例："));

		encapsulationField = new JTextField();
		scoreWeightPanel.add(encapsulationField);
		scoreWeightPanel.add(new JLabel("取值范围：0~100"));

		scoreWeightPanel.add(new JLabel("关系合理性比例："));

		relationRationalityField = new JTextField();
		scoreWeightPanel.add(relationRationalityField);
		scoreWeightPanel.add(new JLabel("取值范围：0~100"));

		left.add(BorderLayout.NORTH, scoreWeightPanel);

		JPanel componentWeightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		componentWeightPanel.setBorder(new TitledBorder(""));
		componentWeightPanel.setPreferredSize(new Dimension(this.getWidth(), 32));

		componentWeightCheckBox = new JCheckBox("分数计算是否考虑组件大小");
		componentWeightPanel.add(componentWeightCheckBox);

		left.add(BorderLayout.CENTER, componentWeightPanel);

		content.add(BorderLayout.NORTH, left);
		content.add(BorderLayout.CENTER, this.getOtherPanel());

		return content;
	}

	@Override
	protected void restore() {
		this.analysisResultProfile = ProfileScopeFacade.getInstance().getDefaultProfileFacade()
				.getAnalysisResultProfile();
		this.refresh();
	}

	@Override
	public void refresh() {

		distanceField.setText(String.valueOf(this.analysisResultProfile.getDistance()));
		balanceField.setText(String.valueOf(this.analysisResultProfile.getBalance()));
		encapsulationField.setText(String.valueOf(this.analysisResultProfile.getEncapsulation()));
		relationRationalityField.setText(String.valueOf(this.analysisResultProfile.getRelationRationality()));

		if (this.analysisResultProfile.isComponentWeight()) {
			componentWeightCheckBox.setSelected(true);
		} else {
			componentWeightCheckBox.setSelected(false);
		}
	}

	@Override
	public void validateData() throws ProfileValidateException {
		float distance = Float.valueOf(distanceField.getText());
		if (distance < 0 || distance > 100) {
			throw new ProfileValidateException("抽象程度合理性比例超出了范围！", 0);
		}

		float balance = Float.valueOf(balanceField.getText());
		if (balance < 0 || balance > 100) {
			throw new ProfileValidateException("内聚性比例超出了范围！", 0);
		}

		float encapsulation = Float.valueOf(encapsulationField.getText());
		if (encapsulation < 0 || encapsulation > 100) {
			throw new ProfileValidateException("封装性比例超出了范围！", 0);
		}

		float relationRationality = Float.valueOf(relationRationalityField.getText());
		if (relationRationality < 0 || relationRationality > 100) {
			throw new ProfileValidateException("关系合理性比例超出了范围！", 0);
		}

		float score = distance + balance + encapsulation + relationRationality;
		if (score != 100) {
			throw new ProfileValidateException("比例之和须等于100！", 0);
		}

	}

	@Override
	public void save(MaintainProfileFacade maintainProfileFacade) {

		float distance = Float.valueOf(distanceField.getText());
		float balance = Float.valueOf(balanceField.getText());
		float encapsulation = Float.valueOf(encapsulationField.getText());
		float relationRationality = Float.valueOf(relationRationalityField.getText());

		AnalysisResultProfile newAnalysisResultProfile = new AnalysisResultProfile();

		newAnalysisResultProfile.setDistance(distance);
		newAnalysisResultProfile.setBalance(balance);
		newAnalysisResultProfile.setEncapsulation(encapsulation);
		newAnalysisResultProfile.setRelationRationality(relationRationality);

		newAnalysisResultProfile.setComponentWeight(componentWeightCheckBox.isSelected());

		maintainProfileFacade.setAnalysisResultProfile(newAnalysisResultProfile);
	}

	@Override
	protected String getExplain() {
		return analysisResultProfile.getExplain();
	}
}
