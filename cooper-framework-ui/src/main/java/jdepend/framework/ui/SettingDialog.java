package jdepend.framework.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ToolTipManager;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.FileUtil;

/**
 * The <code>SettingDialog</code> displays the setting information.
 * 
 * @author <b>Abner</b>
 * 
 */

public abstract class SettingDialog extends JDialog {

	private Map<String, String> files;

	private Map<String, String> tips;

	private JTextArea setting;

	private String selectedItem;

	protected JDependFrame frame;

	private static final int SettingPopDialogWidth = 500;
	private static final int SettingPopDialogHeight = 350;

	/**
	 * Constructs an <code>SettingDialog</code> with the specified parent frame.
	 * 
	 * @param parent
	 *            Parent frame.
	 */
	public SettingDialog(JDependFrame parent) {
		super(parent);

		frame = parent;

		files = this.initFiles();

		tips = this.fileTips();

		setTitle("设置");

		setResizable(false);

		getContentPane().setLayout(new BorderLayout());
		setSize(SettingPopDialogWidth, SettingPopDialogHeight);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JLabel fileLabel = new JLabel("选择配置文件：");

		String[] listData = new String[files.size()];
		Iterator it = files.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			listData[i++] = (String) it.next();
		}

		ComboBoxModel model = new DefaultComboBoxModel(listData);
		JComboBox list = new JComboBox(model);

		list.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				selectedItem = (String) cb.getSelectedItem();
				onChangeItem();
			}
		});

		JPanel fileBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		fileBar.add(fileLabel);
		fileBar.add(list);

		setting = new JTextArea() {
			@Override
			public String getToolTipText(MouseEvent e) {
				return tips.get(selectedItem);
			}
		};
		ToolTipManager.sharedInstance().registerComponent(setting);
		setting.setFont(UIProperty.TEXTFONT);
		JScrollPane pane = new JScrollPane(setting);

		// 如果只有一个项目直接显示内容
		if (listData.length == 1) {
			selectedItem = listData[0];
			onChangeItem();
		}

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createSaveButton());
		buttonBar.add(createCloseButton());

		panel.add(BorderLayout.NORTH, fileBar);

		panel.add(BorderLayout.CENTER, pane);

		panel.add(BorderLayout.SOUTH, buttonBar);

		getContentPane().add(BorderLayout.CENTER, panel);

	}

	private void onChangeItem() {
		String filePath = files.get(selectedItem);
		if (filePath != null && !"".equals(filePath))
			try {
				setting.setText(readFileContent(filePath).toString());
				setting.setCaretPosition(0);
			} catch (JDependException e1) {
				e1.printStackTrace();
				LogUtil.getInstance(SettingDialog.class).systemError("读取配置文件[" + filePath + "]失败！");
				setting.setText("");
			}
		else
			setting.setText("");
	}

	private StringBuilder readFileContent(String filePath) throws JDependException {
		return FileUtil.readFileContent(JDependContext.getWorkspacePath() + filePath);

	}

	private void saveFileContent(String filePath, StringBuilder content) throws JDependException {
		FileUtil.saveFileContent(JDependContext.getWorkspacePath() + filePath, content);
	}

	/**
	 * 初始化操作的设置文件
	 * 
	 * @return
	 */
	protected abstract Map<String, String> initFiles();

	/**
	 * 返回配置文件的提示
	 * 
	 * @return
	 */
	protected Map<String, String> fileTips() {
		Map<String, String> tips = new LinkedHashMap<String, String>();
		tips.put("忽略的包列表", "通过对值增加not前缀可以排除在之前的规则下不被忽略的包（多个的时候用空格分开）");
		return tips;
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
				String filePath = files.get(selectedItem);
				if (filePath != null && !"".equals(filePath)) {
					try {
						saveFileContent(filePath, new StringBuilder(setting.getText()));
						saveAfter(filePath);
						dispose();
					} catch (JDependException e2) {
						e2.printStackTrace();
						LogUtil.getInstance(SettingDialog.class).systemError("保存配置文件[" + filePath + "]失败！");
						JOptionPane.showMessageDialog((Component) e.getSource(), "保存配置文件[" + filePath + "]失败！",
								"alert", JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		});

		return button;
	}

	private void saveAfter(String filePath) throws JDependException {
		doSaveAfter(filePath);
		this.frame.refresh();
	}

	protected void doSaveAfter(String filePath) throws JDependException {

	}
}
