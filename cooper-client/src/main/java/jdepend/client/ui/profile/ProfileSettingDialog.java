package jdepend.client.ui.profile;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.profile.settingpanel.AnalysisResultProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.AreaComponentProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.ComponentProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.JavaClassRelationItemProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.JavaClassUnitProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.ModelProfileSettingPanel;
import jdepend.client.ui.profile.settingpanel.RelationProfileSettingPanel;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.util.BundleUtil;
import jdepend.model.profile.MaintainProfileFacade;
import jdepend.model.profile.ProfileFacade;
import jdepend.model.result.ProfileFacadeImpl;
import jdepend.service.profile.scope.ProfileScopeFacade;

/**
 * @author user
 * 
 */
public abstract class ProfileSettingDialog extends CooperDialog {

	private ProfileFacade profileFacade;

	private JTabbedPane jTabbedPane;

	private List<ModelProfileSettingPanel> settingPanels;

	public ProfileSettingDialog(final JDependCooper frame, ProfileFacade profileFacade) {
		this.profileFacade = profileFacade;
		this.init();
	}

	protected void init() {

		jTabbedPane = new JTabbedPane();

		AnalysisResultProfileSettingPanel analysisResultProfileSettingPanel = new AnalysisResultProfileSettingPanel(
				profileFacade.getAnalysisResultProfile());
		AreaComponentProfileSettingPanel areaComponentProfileSettingPanel = new AreaComponentProfileSettingPanel(
				profileFacade.getAreaComponentProfile());
		ComponentProfileSettingPanel componentProfileSettingPanel = new ComponentProfileSettingPanel(
				profileFacade.getComponentProfile());
		RelationProfileSettingPanel relationProfileSettingPanel = new RelationProfileSettingPanel(
				profileFacade.getRelationProfile());
		JavaClassUnitProfileSettingPanel javaClassUnitProfileSettingPanel = new JavaClassUnitProfileSettingPanel(
				profileFacade.getJavaClassUnitProfile());
		JavaClassRelationItemProfileSettingPanel javaClassRelationItemProfileSettingPanel = new JavaClassRelationItemProfileSettingPanel(
				profileFacade.getJavaClassRelationItemProfile());

		settingPanels = new ArrayList<ModelProfileSettingPanel>();
		settingPanels.add(analysisResultProfileSettingPanel);
		settingPanels.add(areaComponentProfileSettingPanel);
		settingPanels.add(componentProfileSettingPanel);
		settingPanels.add(relationProfileSettingPanel);
		settingPanels.add(javaClassUnitProfileSettingPanel);
		settingPanels.add(javaClassRelationItemProfileSettingPanel);

		jTabbedPane.addTab("分析结果", analysisResultProfileSettingPanel);
		jTabbedPane.addTab("组件区域", areaComponentProfileSettingPanel);
		jTabbedPane.addTab("组件", componentProfileSettingPanel);
		jTabbedPane.addTab("组件关系", relationProfileSettingPanel);
		jTabbedPane.addTab("类", javaClassUnitProfileSettingPanel);
		jTabbedPane.addTab("类关系", javaClassRelationItemProfileSettingPanel);

		JScrollPane pane = new JScrollPane(jTabbedPane);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(BorderLayout.CENTER, pane);

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_OK));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					validateData();
					save();
					dispose();
				} catch (ProfileValidateException ex) {
					jTabbedPane.setSelectedIndex(ex.getTabIndex());
					JOptionPane.showMessageDialog(ProfileSettingDialog.this, ex.getMessage());
				} catch (IOException e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(ProfileSettingDialog.this, e2.getMessage());
				}
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button);
		buttonPanel.add(this.createCancelButton());

		this.add(BorderLayout.SOUTH, buttonPanel);
	}

	protected void validateData() throws ProfileValidateException {
		for (ModelProfileSettingPanel settingPanel : settingPanels) {
			settingPanel.validateData();
		}
	}

	private void save() throws IOException {

		MaintainProfileFacade maintainProfileFacade = new ProfileFacadeImpl();

		for (ModelProfileSettingPanel settingPanel : settingPanels) {
			settingPanel.save(maintainProfileFacade);
		}

		this.updateScope(maintainProfileFacade);

		ProfileScopeFacade.getInstance().save();
	}

	protected abstract void updateScope(ProfileFacade profileFacade);
}
