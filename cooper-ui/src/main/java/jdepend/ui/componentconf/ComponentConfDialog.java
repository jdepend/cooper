package jdepend.ui.componentconf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.model.component.modelconf.ComponentConfException;

/**
 * 组件配置信息窗口抽象类
 * 
 * @author wangdg
 * 
 */
public abstract class ComponentConfDialog extends JDialog {

	protected JTextField componentname;

	protected JCheckBox platformType;

	protected JCheckBox domainType;
	
	protected JCheckBox appType;

	protected JCheckBox interactiveType;

	public ComponentConfDialog() {

		this.setLayout(new BorderLayout());
		setSize(250, 150);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		JPanel content = new JPanel(new BorderLayout());

		JLabel componentLabel = new JLabel("组件名：");
		content.add(BorderLayout.WEST, componentLabel);

		componentname = new JTextField();
		componentname.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doService(e);
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog((Component) e.getSource(), e1.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		content.add(BorderLayout.CENTER, componentname);

		JPanel typePanel = new JPanel(new GridLayout(2, 2));
		this.platformType = new JCheckBox("平台组件");
		this.platformType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ComponentConfDialog.this.domainType.setSelected(false);
					ComponentConfDialog.this.interactiveType.setSelected(false);
					ComponentConfDialog.this.appType.setSelected(false);
				}
			}
		});
		typePanel.add(this.platformType);
		this.domainType = new JCheckBox("领域业务组件");
		this.domainType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ComponentConfDialog.this.platformType.setSelected(false);
					ComponentConfDialog.this.interactiveType.setSelected(false);
					ComponentConfDialog.this.appType.setSelected(false);
				}
			}
		});
		typePanel.add(this.domainType);
		this.appType = new JCheckBox("应用业务组件");
		this.appType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ComponentConfDialog.this.platformType.setSelected(false);
					ComponentConfDialog.this.domainType.setSelected(false);
					ComponentConfDialog.this.interactiveType.setSelected(false);
				}
			}
		});
		typePanel.add(this.appType);
		this.interactiveType = new JCheckBox("交互组件");
		this.interactiveType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ComponentConfDialog.this.platformType.setSelected(false);
					ComponentConfDialog.this.domainType.setSelected(false);
					ComponentConfDialog.this.appType.setSelected(false);
				}
			}
		});
		typePanel.add(this.interactiveType);
		
		content.add(BorderLayout.SOUTH, typePanel);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createOkButton());
		buttonBar.add(createCancelButton());

		this.add(BorderLayout.CENTER, content);

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	protected abstract void doService(ActionEvent e) throws JDependException;

	protected int getComponentLayer() {

		int layer = jdepend.model.Component.UndefinedComponentLevel;

		if (this.platformType.isSelected()) {
			layer = jdepend.model.Component.PlatformComponentLevel;
		}
		if (this.domainType.isSelected()) {
			layer = jdepend.model.Component.DomainComponentLevel;
		}
		if (this.appType.isSelected()) {
			layer = jdepend.model.Component.AppComponentLevel;
		}
		if (this.interactiveType.isSelected()) {
			layer = jdepend.model.Component.InteractiveComponentLevel;
		}
		return layer;
	}

	protected Component createOkButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_OK));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (componentname.getText() == null || componentname.getText().length() == 0) {
					JOptionPane.showMessageDialog((Component) e.getSource(), "请录入组件名称", "alert",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					doService(e);
					ComponentConfDialog.this.dispose();
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog((Component) e.getSource(), e1.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}

	protected Component createCancelButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Cancel));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ComponentConfDialog.this.dispose();
			}
		});

		return button;
	}

}
