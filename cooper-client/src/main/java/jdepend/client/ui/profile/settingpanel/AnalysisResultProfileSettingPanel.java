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

public class AnalysisResultProfileSettingPanel extends ModelProfileSettingPanel {

	private AnalysisResultProfile analysisResultProfile;

	private JTextField distanceField;
	private JTextField balanceField;
	private JTextField encapsulationField;
	private JTextField relationRationalityField;

	public AnalysisResultProfileSettingPanel(AnalysisResultProfile analysisResultProfile) {
		this.analysisResultProfile = analysisResultProfile;

		this.add(this.leftPanel());
		this.add(this.rightPanel());
	}

	protected Component leftPanel() {

		JPanel content = new JPanel(new BorderLayout());

		JPanel left = new JPanel(new GridLayout(4, 3));
		left.setPreferredSize(new Dimension(this.getWidth(), 100));

		left.add(new JLabel("抽象程度合理性比例："));

		distanceField = new JTextField();
		distanceField.setText(String.valueOf(this.analysisResultProfile.getDistance()));

		left.add(distanceField);
		left.add(new JLabel("取值范围：0~100"));

		left.add(new JLabel("内聚性比例："));

		balanceField = new JTextField();
		balanceField.setText(String.valueOf(this.analysisResultProfile.getBalance()));

		left.add(balanceField);
		left.add(new JLabel("取值范围：0~100"));

		left.add(new JLabel("封装性比例："));

		encapsulationField = new JTextField();
		encapsulationField.setText(String.valueOf(this.analysisResultProfile.getEncapsulation()));

		left.add(encapsulationField);
		left.add(new JLabel("取值范围：0~100"));

		left.add(new JLabel("关系合理性比例："));

		relationRationalityField = new JTextField();
		relationRationalityField.setText(String.valueOf(this.analysisResultProfile.getRelationRationality()));

		left.add(relationRationalityField);
		left.add(new JLabel("取值范围：0~100"));

		content.add(BorderLayout.NORTH, left);

		JPanel otherPanel = new JPanel();
		content.add(BorderLayout.CENTER, otherPanel);

		return content;
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
		StringBuilder info = new StringBuilder();

		info.append("量化评价分析结果的分数由多个部分构成，用户可以用过设置每个部分所占的比例来表达自己关心的特性。\n\n");
		info.append("评价总分为100，每一个部分的取值范围为0~100。\n\n");

		info.append("抽象程度合理性的含义是：\n");
		info.append("一个软件系统被划分成多个组件后，由于之间的配合必定出现上层组件和底层组件，一般来说，底层组件都要应对比较多的且有差异的调用者的请求，为此多设计些接口是好手段。而上层组件就没有必要设计过多的接口了。\n");
		info.append("抽象程度合理性的公式是（1-分析单元平均D值）*权值。\n");

		info.append("内聚性指数的含义是：\n");
		info.append("组件内的子元素（JavaPackage或JavaClass）之间的关系应该比与其他组件的关系紧密（除了复用的考虑外），在复用目的的考虑下，组件内的子元素与其他组件的关系应能做到“相互抵消”。\n");
		info.append("内聚性指数的公式是（1 – 分析单元平均Balance值）*权值。\n");

		info.append("封装性的含义是：\n");
		info.append("组件暴漏给其他组件可使用的接口应保证尽量的少，而实现这些接口的元素要做要隐藏，利于修改和扩展。\n");
		info.append("封装性的公式是（1 – 分析单元平均Encapsulation值）*权值。\n");

		info.append("关系合理性的含义是：\n");
		info.append("组件间的关系应该是单向、合理，不存在彼此、循环、下层组件调用上层组件、稳定性强的组件依赖稳定性弱的组件的情况。\n");
		info.append("关系合理性的公式是（存在问题的关系 * 问题权值/总关系）*权值。\n");

		return info.toString();
	}
}
