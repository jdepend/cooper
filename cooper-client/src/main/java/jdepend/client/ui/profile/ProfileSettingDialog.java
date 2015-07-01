package jdepend.client.ui.profile;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.profile.settingpanel.AnalysisResultProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.AreaComponentProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.ComponentProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.JavaClassRelationItemProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.JavaClassUnitProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.RelationProfileSettingPanel;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.util.BundleUtil;
import jdepend.model.profile.ProfileFacade;
import jdepend.service.profile.scope.ProfileScope;

public class ProfileSettingDialog extends CooperDialog {

	private ProfileFacade profileFacade;

	public ProfileSettingDialog(final JDependCooper frame, ProfileFacade profileFacade) {
		this.profileFacade = profileFacade;
		this.init();
	}

	protected void init() {

		JTabbedPane jTabbedPane = new JTabbedPane();

		jTabbedPane.addTab("分析结果", new AnalysisResultProfileSettingPanel());
		jTabbedPane.addTab("组件区域", new AreaComponentProfileSettingPanel());
		jTabbedPane.addTab("组件", new ComponentProfileSettingPanel());
		jTabbedPane.addTab("组件关系", new RelationProfileSettingPanel());
		jTabbedPane.addTab("类", new JavaClassUnitProfileSettingPanel());
		jTabbedPane.addTab("类关系", new JavaClassRelationItemProfileSettingPanel());

		JScrollPane pane = new JScrollPane(jTabbedPane);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(BorderLayout.CENTER, pane);

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_OK));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button);
		buttonPanel.add(this.createCloseButton());

		this.add(BorderLayout.SOUTH, buttonPanel);
	}
}
