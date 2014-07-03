package jdepend.ui.motive;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;

public final class CreateAreaDialog extends ComponentListDialog {

	public CreateAreaDialog(MotiveOperationPanel motiveOperationPanel) {
		super(motiveOperationPanel);
		this.setTitle("创建区域");
	}

	@Override
	protected void add() throws JDependException {
		if (this.currentComponentNames == null || currentComponentNames.size() == 0) {
			JOptionPane.showMessageDialog(this, "请选择组件", "alert", JOptionPane.INFORMATION_MESSAGE);
		} else {
			InputAreaNameDialog d = new InputAreaNameDialog();
			d.setModal(true);
			d.setVisible(true);
		}

	}

	private void addAreaInfo(int areaLayer, String areaName) throws JDependException {
		Collection<jdepend.model.Component> components = new ArrayList<jdepend.model.Component>();
		Map<String, jdepend.model.Component> unitForNames = JDependUnitMgr.getInstance().getResult()
				.getComponentForNames();
		for (String componentName : this.currentComponentNames) {
			if (unitForNames.containsKey(componentName)) {
				components.add(unitForNames.get(componentName));
			}
		}
		this.motiveContainer.addAreaComponent(areaLayer, areaName, components);
		motiveOperationPanel.refreshArea();
	}

	class InputAreaNameDialog extends JDialog {

		private JTextField areaNameField;

		private JTextField areaLayerField;

		public InputAreaNameDialog() {
			this.setTitle("区域名称");
			getContentPane().setLayout(new BorderLayout());
			setSize(350, 100);
			this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

			JPanel workpacePanel = new JPanel();
			areaNameField = new JTextField();
			if (CreateAreaDialog.this.currentComponentNames.size() == 1) {
				areaNameField.setText(CreateAreaDialog.this.currentComponentNames.get(0));
			}

			areaLayerField = new JTextField();

			this.add(BorderLayout.CENTER, areaNameField);

			JPanel buttonBar = new JPanel(new FlowLayout());
			buttonBar.add(createOKButton());
			buttonBar.add(createCloseButton());

			this.add(BorderLayout.SOUTH, buttonBar);
		}

		private Component createOKButton() {
			JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_OK));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (areaNameField.getText() == null || areaNameField.getText().length() == 0) {
						JOptionPane.showMessageDialog(InputAreaNameDialog.this, "请录入区域名", "alert",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					try {
						CreateAreaDialog.this.addAreaInfo(0, areaNameField.getText());
						dispose();
						CreateAreaDialog.this.dispose();
					} catch (JDependException e1) {
						JOptionPane.showMessageDialog(InputAreaNameDialog.this, e1.getMessage(), "error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			return button;
		}

		private Component createCloseButton() {
			JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			return button;
		}
	}
}
