package jdepend.client.ui.profile.settingpanel;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.model.profile.MaintainProfileFacade;

public abstract class ModelProfileSettingPanel extends JPanel {

	public ModelProfileSettingPanel() {
		this.setLayout(new GridLayout(1, 2));
	}

	protected Component rightPanel() {
		JEditorPane explainPane = new JEditorPane();
		explainPane.setEditable(false);

		String explain = this.getExplain();
		if (explain != null) {
			explainPane.setText(explain);
			explainPane.setCaretPosition(0);
			explainPane.setFont(new Font("DialogInput", Font.PLAIN, 12));
		}

		JScrollPane pane = new JScrollPane(explainPane);

		return pane;

	};

	protected abstract String getExplain();

	public abstract void validateData() throws ProfileValidateException;

	public abstract void save(MaintainProfileFacade maintainProfileFacade);
}
