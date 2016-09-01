package jdepend.client.ui.profile.settingpanel;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.framework.util.BundleUtil;
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

	protected JPanel getOtherPanel() {
		JPanel otherPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Restore));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restore();
			}
		});
		otherPanel.add(button);

		return otherPanel;
	}

	protected abstract void restore();

	public abstract void refresh();

	protected abstract String getExplain();

	public abstract void validateData() throws ProfileValidateException;

	public abstract void save(MaintainProfileFacade maintainProfileFacade);
}
