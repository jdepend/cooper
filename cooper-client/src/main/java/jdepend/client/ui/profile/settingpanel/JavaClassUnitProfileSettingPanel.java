package jdepend.client.ui.profile.settingpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.model.JavaClassUnitProfile;

public class JavaClassUnitProfileSettingPanel extends ModelProfileSettingPanel {

	private JavaClassUnitProfile javaClassUnitProfile;

	private List<JCheckBox> abstractClassRuleCheckBoxes;

	private List<JCheckBox> stableRuleCheckBoxes;

	public JavaClassUnitProfileSettingPanel(JavaClassUnitProfile javaClassUnitProfile) {
		this.javaClassUnitProfile = javaClassUnitProfile;

		this.add(this.leftPanel());
		this.add(this.rightPanel());
	}

	protected Component leftPanel() {

		JPanel content = new JPanel(new BorderLayout());

		JPanel left = new JPanel(new GridLayout(2, 1));
		left.setPreferredSize(new Dimension(this.getWidth(), 300));
		// 类的抽象性
		List<String> allAbstractClassRules = JavaClassUnitProfile.getAllAbstractClassRules();
		JPanel abstractClassRulePanel = new JPanel(new GridLayout(allAbstractClassRules.size(), 1));
		abstractClassRulePanel.setBorder(new TitledBorder("判断类抽象性规则"));

		this.abstractClassRuleCheckBoxes = new ArrayList<JCheckBox>();
		JCheckBox abstractClassRuleCheckBox;
		for (String abstractClassRule : allAbstractClassRules) {
			abstractClassRuleCheckBox = new JCheckBox(abstractClassRule);
			if (javaClassUnitProfile.getAbstractClassRules().contains(abstractClassRule)) {
				abstractClassRuleCheckBox.setSelected(true);
			}
			abstractClassRuleCheckBoxes.add(abstractClassRuleCheckBox);

			abstractClassRulePanel.add(abstractClassRuleCheckBox);
		}
		left.add(abstractClassRulePanel);
		// 类的稳定性
		List<String> allStableRules = JavaClassUnitProfile.getAllStableRules();
		JPanel stableRulePanel = new JPanel(new GridLayout(allStableRules.size(), 1));
		stableRulePanel.setBorder(new TitledBorder("判断类稳定性规则"));

		this.stableRuleCheckBoxes = new ArrayList<JCheckBox>();
		JCheckBox stableRuleCheckBox;
		for (String stableRule : allStableRules) {
			stableRuleCheckBox = new JCheckBox(stableRule);
			if (javaClassUnitProfile.getStableRules().contains(stableRule)) {
				stableRuleCheckBox.setSelected(true);
			}
			stableRuleCheckBoxes.add(stableRuleCheckBox);

			stableRulePanel.add(stableRuleCheckBox);
		}

		left.add(stableRulePanel);

		content.add(BorderLayout.NORTH, left);

		JPanel otherPanel = new JPanel();
		content.add(BorderLayout.CENTER, otherPanel);

		return content;
	}

	@Override
	public void validateData() throws ProfileValidateException {
	}

	@Override
	public void save(MaintainProfileFacade maintainProfileFacade) {

		JavaClassUnitProfile newJavaClassUnitProfile = new JavaClassUnitProfile();
		List<String> abstractClassRules = new ArrayList<String>();
		for (JCheckBox abstractClassRuleCheckBox : abstractClassRuleCheckBoxes) {
			if (abstractClassRuleCheckBox.isSelected()) {
				abstractClassRules.add(abstractClassRuleCheckBox.getText());
			}
		}
		newJavaClassUnitProfile.setAbstractClassRules(abstractClassRules);

		List<String> stableClassRules = new ArrayList<String>();
		for (JCheckBox stableRuleCheckBox : stableRuleCheckBoxes) {
			if (stableRuleCheckBox.isSelected()) {
				stableClassRules.add(stableRuleCheckBox.getText());
			}
		}
		newJavaClassUnitProfile.setStableRules(stableClassRules);

		maintainProfileFacade.setJavaClassUnitProfile(newJavaClassUnitProfile);
	}

	@Override
	protected String getExplain() {
		return javaClassUnitProfile.getExplain();
	}

}
