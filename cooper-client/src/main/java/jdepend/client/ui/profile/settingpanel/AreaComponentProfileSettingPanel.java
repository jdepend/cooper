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
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.defaultvalue.DefaultAreaComponentProfile;

public class AreaComponentProfileSettingPanel extends ModelProfileSettingPanel {

	private AreaComponentProfile areaComponentProfile;

	private List<JCheckBox> accordingCheckBoxes;

	public AreaComponentProfileSettingPanel(AreaComponentProfile areaComponentProfile) {
		this.areaComponentProfile = areaComponentProfile;

		this.init();
		this.refresh();
	}

	private void init() {
		this.add(this.leftPanel());
		this.add(this.rightPanel());
	}

	protected Component leftPanel() {

		JPanel content = new JPanel(new BorderLayout());

		List<String> allAccordings = AreaComponentProfile.getAllAccordings();

		JPanel left = new JPanel(new BorderLayout());
		left.setPreferredSize(new Dimension(this.getWidth(), 120));

		JPanel accordingPanel = new JPanel(new GridLayout(allAccordings.size(), 1));
		accordingPanel.setBorder(new TitledBorder("判断规则"));

		this.accordingCheckBoxes = new ArrayList<JCheckBox>();
		JCheckBox accordingCheckBox;
		for (String according : allAccordings) {
			accordingCheckBox = new JCheckBox(according);
			accordingCheckBoxes.add(accordingCheckBox);
			accordingPanel.add(accordingCheckBox);
		}

		left.add(BorderLayout.CENTER, accordingPanel);

		content.add(BorderLayout.NORTH, left);
		content.add(BorderLayout.CENTER, this.getOtherPanel());

		return content;
	}

	@Override
	protected void restore() {
		this.areaComponentProfile = new DefaultAreaComponentProfile();
		this.refresh();
	}

	@Override
	public void refresh() {
		for (JCheckBox accordingCheckBox : accordingCheckBoxes) {
			if (areaComponentProfile.getAccordings().contains(accordingCheckBox.getText())) {
				accordingCheckBox.setSelected(true);
			} else {
				accordingCheckBox.setSelected(false);
			}
		}
	}

	@Override
	public void validateData() throws ProfileValidateException {
	}

	@Override
	public void save(MaintainProfileFacade maintainProfileFacade) {

		AreaComponentProfile newAreaComponentProfile = new AreaComponentProfile();

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
