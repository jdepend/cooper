package jdepend.client.ui.profile.settingpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.defaultvalue.DefaultAnalysisResultProfile;

public class AnalysisResultProfileSettingPanel extends ModelProfileSettingPanel {

	private AnalysisResultProfile analysisResultProfile;

	private JTextField distanceField;
	private JTextField balanceField;
	private JTextField encapsulationField;
	private JTextField relationRationalityField;

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

		JPanel left = new JPanel(new GridLayout(4, 3));
		left.setPreferredSize(new Dimension(this.getWidth(), 100));

		left.add(new JLabel("抽象程度合理性比例："));

		distanceField = new JTextField();
		left.add(distanceField);
		left.add(new JLabel("取值范围：0~100"));

		left.add(new JLabel("内聚性比例："));

		balanceField = new JTextField();
		left.add(balanceField);
		left.add(new JLabel("取值范围：0~100"));

		left.add(new JLabel("封装性比例："));

		encapsulationField = new JTextField();
		left.add(encapsulationField);
		left.add(new JLabel("取值范围：0~100"));

		left.add(new JLabel("关系合理性比例："));

		relationRationalityField = new JTextField();
		left.add(relationRationalityField);
		left.add(new JLabel("取值范围：0~100"));

		content.add(BorderLayout.NORTH, left);
		content.add(BorderLayout.CENTER, this.getOtherPanel());

		return content;
	}

	@Override
	protected void restore() {
		this.analysisResultProfile = new DefaultAnalysisResultProfile();
		this.refresh();
	}

	@Override
	public void refresh() {
		distanceField.setText(String.valueOf(this.analysisResultProfile.getDistance()));
		balanceField.setText(String.valueOf(this.analysisResultProfile.getBalance()));
		encapsulationField.setText(String.valueOf(this.analysisResultProfile.getEncapsulation()));
		relationRationalityField.setText(String.valueOf(this.analysisResultProfile.getRelationRationality()));
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

		maintainProfileFacade.setAnalysisResultProfile(newAnalysisResultProfile);
	}

	@Override
	protected String getExplain() {
		return analysisResultProfile.getExplain();
	}
}
