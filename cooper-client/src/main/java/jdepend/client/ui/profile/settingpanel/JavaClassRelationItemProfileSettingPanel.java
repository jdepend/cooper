package jdepend.client.ui.profile.settingpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import jdepend.client.ui.profile.ProfileValidateException;
import jdepend.metadata.profile.JavaClassRelationItemProfile;
import jdepend.model.profile.MaintainProfileFacade;

public class JavaClassRelationItemProfileSettingPanel extends ModelProfileSettingPanel {

	private JavaClassRelationItemProfile javaClassRelationItemProfile;

	private List<JTextField> typeJTextFields;

	public JavaClassRelationItemProfileSettingPanel(JavaClassRelationItemProfile javaClassRelationItemProfile) {
		this.javaClassRelationItemProfile = javaClassRelationItemProfile;

		this.add(this.leftPanel());
		this.add(this.rightPanel());
	}

	protected Component leftPanel() {
		JPanel content = new JPanel(new BorderLayout());

		List<String> allTypes = JavaClassRelationItemProfile.getAllTypes();

		JPanel left = new JPanel(new BorderLayout());
		left.setPreferredSize(new Dimension(this.getWidth(), 180));

		JPanel typePanel = new JPanel(new GridLayout(allTypes.size(), 3));
		typePanel.setBorder(new TitledBorder("设置类关系类型的强度"));

		this.typeJTextFields = new ArrayList<JTextField>();
		JTextField typeJTextField;
		for (String type : allTypes) {
			typePanel.add(new JLabel(type + ":"));
			typeJTextField = new JTextField();
			typeJTextField.setText(String.valueOf(javaClassRelationItemProfile.getTypes().get(type)));
			typeJTextFields.add(typeJTextField);
			typePanel.add(typeJTextField);
			typePanel.add(new JLabel("取值范围：0~1"));
		}

		left.add(BorderLayout.CENTER, typePanel);

		content.add(BorderLayout.NORTH, left);

		JPanel otherPanel = new JPanel();
		content.add(BorderLayout.CENTER, otherPanel);

		return content;
	}

	@Override
	public void validateData() throws ProfileValidateException {

		for (JTextField typeJTextField : typeJTextFields) {
			float type = Float.valueOf(typeJTextField.getText());
			if (type < 0 || type > 1) {
				throw new ProfileValidateException("类关系类型的强度超出了范围！", 5);
			}
		}
	}

	@Override
	public void save(MaintainProfileFacade maintainProfileFacade) {

		JavaClassRelationItemProfile newJavaClassRelationItemProfile = new JavaClassRelationItemProfile();

		Map<String, Float> types = new HashMap<String, Float>();

		List<String> allTypes = JavaClassRelationItemProfile.getAllTypes();
		int index = 0;
		for (JTextField typeJTextField : typeJTextFields) {
			float type = Float.valueOf(typeJTextField.getText());
			types.put(allTypes.get(index++), type);
		}
		newJavaClassRelationItemProfile.setTypes(types);

		maintainProfileFacade.setJavaClassRelationItemProfile(newJavaClassRelationItemProfile);
	}

	@Override
	protected String getExplain() {
		return javaClassRelationItemProfile.getExplain();
	}
}
