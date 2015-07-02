package jdepend.client.ui.profile.settingpanel;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.profile.model.AnalysisResultProfile;

public class AnalysisResultProfileSettingPanel extends ModelProfileSettingPanel {

	private JTextField distanceField;
	private JTextField balanceField;
	private JTextField encapsulationeField;
	private JTextField relationRationalityField;

	public AnalysisResultProfileSettingPanel(AnalysisResultProfile analysisResultProfile) {

		this.setLayout(new GridLayout(4, 3));

		add(new JLabel("抽象程度合理性比例："));

		distanceField = new JTextField();
		distanceField.setText(String.valueOf(analysisResultProfile.getDistance()));

		add(distanceField);
		add(new JLabel("取值范围：0~100"));

		add(new JLabel("内聚性比例："));

		balanceField = new JTextField();
		balanceField.setText(String.valueOf(analysisResultProfile.getBalance()));

		add(balanceField);
		add(new JLabel("取值范围：0~100"));

		add(new JLabel("封装性比例："));

		encapsulationeField = new JTextField();
		encapsulationeField.setText(String.valueOf(analysisResultProfile.getEncapsulation()));

		add(encapsulationeField);
		add(new JLabel("取值范围：0~100"));

		add(new JLabel("关系合理性比例："));

		relationRationalityField = new JTextField();
		relationRationalityField.setText(String.valueOf(analysisResultProfile.getRelationRationality()));

		add(relationRationalityField);
		add(new JLabel("取值范围：0~100"));
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
		
		float encapsulatione = Float.valueOf(encapsulationeField.getText());
		if (encapsulatione < 0 || encapsulatione > 100) {
			throw new ProfileValidateException("封装性比例超出了范围！", 0);
		}
		
		float relationRationality = Float.valueOf(relationRationalityField.getText());
		if (relationRationality < 0 || relationRationality > 100) {
			throw new ProfileValidateException("关系合理性比例超出了范围！", 0);
		}
		
		float score = distance + balance + encapsulatione + relationRationality;
		if(score != 100){
			throw new ProfileValidateException("比例之和须等于100！", 0);
		}

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}
}
