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
		return areaComponentProfile.getExplain();
	}

}
