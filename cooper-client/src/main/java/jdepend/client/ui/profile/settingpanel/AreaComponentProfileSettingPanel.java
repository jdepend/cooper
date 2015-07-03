package jdepend.client.ui.profile.settingpanel;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import jdepend.client.ui.profile.ProfileValidateException;
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

		List<String> allAccordings = AreaComponentProfile.getAllAccordings();

		JPanel left = new JPanel(new GridLayout(1 + allAccordings.size(), 1));

		this.createCheckBox = new JCheckBox("是否计算组件区域");

		if (areaComponentProfile.isCreate()) {
			this.createCheckBox.setSelected(true);
		}

		left.add(this.createCheckBox);

		this.accordingCheckBoxes = new ArrayList<JCheckBox>();
		JCheckBox accordingCheckBox;
		for (String according : allAccordings) {
			accordingCheckBox = new JCheckBox(according);
			if (areaComponentProfile.getAccordings().contains(according)) {
				accordingCheckBox.setSelected(true);
			}
			accordingCheckBoxes.add(accordingCheckBox);

			left.add(accordingCheckBox);
		}

		return left;
	}

	@Override
	public void validateData() throws ProfileValidateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(MaintainProfileFacade maintainProfileFacade) {
		// TODO Auto-generated method stub
		maintainProfileFacade.setAreaComponentProfile(areaComponentProfile);
	}

	@Override
	protected String getExplain() {
		// TODO Auto-generated method stub
		return null;
	}

}
