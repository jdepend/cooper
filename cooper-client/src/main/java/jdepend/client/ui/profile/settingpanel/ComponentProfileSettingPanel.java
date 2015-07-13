package jdepend.client.ui.profile.settingpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.defaultvalue.DefaultComponentProfile;

public class ComponentProfileSettingPanel extends ModelProfileSettingPanel {

	private ComponentProfile componentProfile;

	private JSlider stabilityWithCountScaleJSlider;

	private JRadioButton balanceFromPackageRadio;

	private JRadioButton balanceFromClassRadio;

	public ComponentProfileSettingPanel(ComponentProfile componentProfile) {
		this.componentProfile = componentProfile;

		this.init();
		this.refresh();
	}

	private void init() {
		this.add(this.leftPanel());
		this.add(this.rightPanel());
	}

	protected Component leftPanel() {

		JPanel content = new JPanel(new BorderLayout());

		JPanel left = new JPanel(new GridLayout(2, 1));
		left.setPreferredSize(new Dimension(this.getWidth(), 160));

		JPanel stabilityPanel = new JPanel(new GridLayout(2, 1));
		stabilityPanel.setBorder(new TitledBorder("选择计算组件稳定性的依据"));

		stabilityWithCountScaleJSlider = new JSlider(0, 2);
		stabilityPanel.add(stabilityWithCountScaleJSlider);

		JPanel stabilityJSliderExplain = new JPanel(new BorderLayout());
		stabilityJSliderExplain.add(BorderLayout.WEST, new JLabel("数量"));
		stabilityJSliderExplain.add(BorderLayout.EAST, new JLabel("强度"));

		stabilityPanel.add(stabilityJSliderExplain);

		left.add(stabilityPanel);

		JPanel balancePanel = new JPanel(new GridLayout(2, 1));
		balancePanel.setBorder(new TitledBorder("选择计算组件内聚性的子元素"));

		balanceFromPackageRadio = new JRadioButton(ComponentProfile.balanceFromPackage) {
			@Override
			protected void fireStateChanged() {
				balanceFromClassRadio.setSelected(!balanceFromPackageRadio.isSelected());
			}
		};

		balanceFromClassRadio = new JRadioButton(ComponentProfile.balanceFromClass) {
			@Override
			protected void fireStateChanged() {
				balanceFromPackageRadio.setSelected(!balanceFromClassRadio.isSelected());
			}
		};

		balancePanel.add(balanceFromPackageRadio);
		balancePanel.add(balanceFromClassRadio);

		left.add(BorderLayout.CENTER, balancePanel);

		content.add(BorderLayout.NORTH, left);
		content.add(BorderLayout.CENTER, this.getOtherPanel());

		return content;
	}

	@Override
	protected void restore() {
		this.componentProfile = new DefaultComponentProfile();
		this.refresh();
	}

	@Override
	public void refresh() {
		stabilityWithCountScaleJSlider.getModel().setValue(
				this.getSliderValue(componentProfile.getStabilityWithCountScale()));

		if (componentProfile.getBalance().equals(ComponentProfile.balanceFromPackage)) {
			balanceFromPackageRadio.setSelected(true);
		}

		if (componentProfile.getBalance().equals(ComponentProfile.balanceFromClass)) {
			balanceFromClassRadio.setSelected(true);
		}
	}

	private int getSliderValue(float scale) {
		if (scale == 0F) {
			return 0;
		} else if (scale == 1.0F) {
			return 2;
		} else if (scale == 0.5F) {
			return 1;
		} else {
			return 1;
		}
	}

	private float getScale(int value) {
		if (value == 0) {
			return 0F;
		} else if (value == 2) {
			return 1.0F;
		} else if (value == 1) {
			return 0.5F;
		} else {
			return 0.5F;
		}
	}

	@Override
	public void validateData() throws ProfileValidateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(MaintainProfileFacade maintainProfileFacade) {

		ComponentProfile newComponentProfile = new ComponentProfile();

		newComponentProfile.setStabilityWithCountScale(this.getScale(stabilityWithCountScaleJSlider.getValue()));

		if (balanceFromPackageRadio.isSelected()) {
			newComponentProfile.setBalance(ComponentProfile.balanceFromPackage);
		}
		if (balanceFromClassRadio.isSelected()) {
			newComponentProfile.setBalance(ComponentProfile.balanceFromClass);
		}
		maintainProfileFacade.setComponentProfile(newComponentProfile);
	}

	@Override
	protected String getExplain() {
		return componentProfile.getExplain();
	}

}
