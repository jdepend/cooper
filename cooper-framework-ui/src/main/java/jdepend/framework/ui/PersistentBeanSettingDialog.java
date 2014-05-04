package jdepend.framework.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import jdepend.framework.domain.PersistentBean;
import jdepend.framework.domain.notPersistent;
import jdepend.framework.util.BundleUtil;

public class PersistentBeanSettingDialog extends JDialog {

	private PersistentBean bean;

	private Map<Method, JTextComponent> attributes = new LinkedHashMap<Method, JTextComponent>();

	private static final int TextItemHeight = 60;
	private static final int AreaItemHeight = 120;

	private static final int ButtonHeight = 50;

	public PersistentBeanSettingDialog(JDependFrame frame, PersistentBean bean) {

		super(frame);

		this.bean = bean;

		this.setTitle(this.bean.getClass().getName() + " 设置");

		setResizable(false);

		getContentPane().setLayout(new BorderLayout());

		Method[] methods = this.bean.getClass().getDeclaredMethods();

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		// 创建页面控件
		JPanel content = new JPanel(new GridLayout(methods.length, 2));
		JPanel item;
		for (Method method : methods) {
			if (method.getName().startsWith("set") && method.getAnnotation(notPersistent.class) == null) {
				Type[] types = method.getGenericParameterTypes();
				if (types.length > 0) {
					item = new JPanel(new BorderLayout());
					item.add(BorderLayout.NORTH, new JLabel(method.getName().substring(3)));
					JTextComponent attribute;
					if (types[0].toString().indexOf('L') != -1) {
						attribute = new JTextArea(4, 20);
					} else if (types[0].toString().indexOf("java.util.Collection") != -1) {
						attribute = new JTextArea(4, 20);
					} else {
						attribute = new JTextField();
					}
					item.add(BorderLayout.CENTER, attribute);
					content.add(item);
					attributes.put(method, attribute);
				}
			}
		}
		// 填充控件数据
		try {
			for (Method method : methods) {
				if (method.getName().startsWith("get")) {
					for (Method method1 : attributes.keySet()) {
						if (method1.getName().substring(3).equals(method.getName().substring(3))) {
							Object rtn = method.invoke(bean);
							if (rtn != null) {
								if (rtn instanceof String) {
									((JTextComponent) attributes.get(method1)).setText((String) rtn);
								} else if (rtn instanceof Integer) {
									((JTextComponent) attributes.get(method1)).setText(((Integer) rtn).toString());
								} else if (rtn instanceof Boolean) {
									((JTextComponent) attributes.get(method1)).setText(((Boolean) rtn).toString());
								} else if (rtn instanceof String[]) {
									StringBuilder info = new StringBuilder();
									String[] infos = (String[]) rtn;
									for (int i = 0; i < infos.length; i++) {
										info.append(infos[i]);
										info.append("\n");
									}
									((JTextComponent) attributes.get(method1)).setText(info.toString());
								} else if (rtn instanceof Collection) {
									StringBuilder info = new StringBuilder();
									Collection<String> infos = (Collection<String>) rtn;
									for (String ifo : infos) {
										info.append(ifo);
										info.append("\n");
									}
									((JTextComponent) attributes.get(method1)).setText(info.toString());
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
		}

		if (attributes.size() == 0) {
			setSize(500, TextItemHeight + ButtonHeight);
		} else {
			int height = 0;
			for (JTextComponent component : attributes.values()) {
				if (component instanceof JTextArea) {
					height += AreaItemHeight;
				} else {
					height += TextItemHeight;
				}
			}
			setSize(500, height + ButtonHeight);
		}
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createSaveButton());
		buttonBar.add(createCloseButton());

		panel.add(BorderLayout.CENTER, new JScrollPane(content));

		panel.add(BorderLayout.SOUTH, buttonBar);

		getContentPane().add(BorderLayout.CENTER, panel);
	}

	/**
	 * Creates and returns a button with the specified label.
	 * 
	 * @param label
	 *            Button label.
	 * @return Button.
	 */
	private JButton createCloseButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		return button;
	}

	private JButton createSaveButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Save));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					for (Method method : attributes.keySet()) {
						if (method.getParameterTypes()[0].getName().equals("java.lang.String")) {
							method.invoke(bean, ((JTextComponent) attributes.get(method)).getText());
						} else if (method.getParameterTypes()[0].getName().equals("int")
								|| method.getParameterTypes()[0].getName().equals("java.lang.Integer")) {
							method.invoke(bean, Integer.parseInt(((JTextComponent) attributes.get(method)).getText()));
						} else if (method.getParameterTypes()[0].getName().equalsIgnoreCase("boolean")
								|| method.getParameterTypes()[0].getName().equals("java.lang.Boolean")) {
							method.invoke(bean,
									Boolean.parseBoolean(((JTextComponent) attributes.get(method)).getText()));
						} else if (method.getParameterTypes()[0].getName().equalsIgnoreCase("[Ljava.lang.String;")) {
							String info = ((JTextComponent) attributes.get(method)).getText();
							if (info != null && info.length() > 0) {
								ArrayList<String> values = new ArrayList<String>();
								for (String item : info.split("\n")) {
									values.add(item);
								}
								String[] param = new String[values.size()];
								int i = 0;
								for (String value : values) {
									param[i++] = value;
								}
								method.invoke(bean, (Object) param);
							}
						} else if (method.getParameterTypes()[0].getName().equalsIgnoreCase("java.util.Collection")) {
							String info = ((JTextComponent) attributes.get(method)).getText();
							if (info != null && info.length() > 0) {
								Collection<String> param = new ArrayList<String>();
								for (String item : info.split("\n")) {
									param.add(item);
								}
								method.invoke(bean, (Object) param);
							}
						}
					}
					bean.save();
					dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
					Component source = (Component) e.getSource();
					JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}
}
