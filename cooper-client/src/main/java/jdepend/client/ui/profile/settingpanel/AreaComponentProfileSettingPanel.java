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
import jdepend.framework.util.BundleUtil;
import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.model.AreaComponentProfile;

public class AreaComponentProfileSettingPanel extends ModelProfileSettingPanel {

	private AreaComponentProfile areaComponentProfile;

	private JCheckBox createCheckBox;

	private List<JCheckBox> accordingCheckBoxes;

	public AreaComponentProfileSettingPanel(AreaComponentProfile areaComponentProfile) {
		this.areaComponentProfile = areaComponentProfile;

		this.add(this.leftPanel());
		this.add(this.rightPanel());
	}

	protected Component leftPanel() {

		JPanel content = new JPanel(new BorderLayout());

		List<String> allAccordings = AreaComponentProfile.getAllAccordings();

		JPanel left = new JPanel(new BorderLayout());
		left.setPreferredSize(new Dimension(this.getWidth(), 120));

		this.createCheckBox = new JCheckBox("是否计算组件区域");

		if (areaComponentProfile.isCreate()) {
			this.createCheckBox.setSelected(true);
		}

		left.add(BorderLayout.NORTH, this.createCheckBox);

		JPanel accordingPanel = new JPanel(new GridLayout(allAccordings.size(), 1));
		accordingPanel.setBorder(new TitledBorder("判断规则"));

		this.accordingCheckBoxes = new ArrayList<JCheckBox>();
		JCheckBox accordingCheckBox;
		for (String according : allAccordings) {
			accordingCheckBox = new JCheckBox(according);
			if (areaComponentProfile.getAccordings().contains(according)) {
				accordingCheckBox.setSelected(true);
			}
			accordingCheckBoxes.add(accordingCheckBox);

			accordingPanel.add(accordingCheckBox);
		}

		left.add(BorderLayout.CENTER, accordingPanel);

		content.add(BorderLayout.NORTH, left);

		JPanel otherPanel = new JPanel();
		content.add(BorderLayout.CENTER, otherPanel);

		return content;
	}

	@Override
	public void validateData() throws ProfileValidateException {
		if (createCheckBox.isSelected()) {
			boolean according = false;
			for (JCheckBox accordingCheckBox : accordingCheckBoxes) {
				if (accordingCheckBox.isSelected()) {
					according = true;
					break;
				}
			}
			if (!according) {
				throw new ProfileValidateException("请选择至少一个创建组件区域的算法", 1);
			}
		}
	}

	@Override
	public void save(MaintainProfileFacade maintainProfileFacade) {

		AreaComponentProfile newAreaComponentProfile = new AreaComponentProfile();
		newAreaComponentProfile.setCreate(createCheckBox.isSelected());

		List<String> accordings = new ArrayList<String>();
		for (JCheckBox accordingCheckBox : accordingCheckBoxes) {
			if (accordingCheckBox.isSelected()) {
				accordings.add(accordingCheckBox.getText());
			}
		}
		newAreaComponentProfile.setAccordings(accordings);

		maintainProfileFacade.setAreaComponentProfile(newAreaComponentProfile);
	}

	@Override
	protected String getExplain() {
		StringBuilder info = new StringBuilder();

		info.append("系统是由多个组件组成的，当系统比较大的时候，组件的数量会比较大（大于20），这时候通过将组件划分到不同的区域（或者叫子系统）内，可以更好的管理这些组件。\n\n");
		info.append("系统可以通过组件的一些指标来识别组件所属的区域，并计算区域的一些指标，如稳定性。\n\n");
		info.append("携带了区域信息的组件关系应该有一定的要求，应该避免稳定区域中的组件依赖不稳定区域中的组件。\n\n");
		info.append("识别组件区域是可选的。\n\n");
		info.append("用户可以通过选择识别组件区域的算法来识别待分析系统的组件区域。\n\n");

		return info.toString();
	}

}
