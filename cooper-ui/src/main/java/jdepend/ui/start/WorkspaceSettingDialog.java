package jdepend.ui.start;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;

import jdepend.framework.context.JDependContext;
import jdepend.framework.ui.WelcomeDialog;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.StringUtil;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.UIPropertyConfigurator;

public class WorkspaceSettingDialog extends JDialog {

	private JTextField workspacePath;

	private WorkspaceSetting setting;

	private WelcomeDialog welcomeDialog;

	private String[] args;

	private JDependCooper frame;

	public WorkspaceSettingDialog(WorkspaceSetting setting, JDependCooper frame) {
		this(null, setting, null);
		this.frame = frame;
	}

	public WorkspaceSettingDialog(final WelcomeDialog welcomeDialog1, WorkspaceSetting setting, String[] args) {
		setTitle("设置工作区路径");

		this.setting = setting;
		this.welcomeDialog = welcomeDialog1;
		this.args = args;

		getContentPane().setLayout(new BorderLayout());
		setSize(580, 115);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				if (welcomeDialog != null) {
					welcomeDialog.dispose();
					System.exit(0);
				}
			}
		});

		this.add(BorderLayout.WEST, new JLabel("工作区路径："));
		workspacePath = new JTextField() {
			@Override
			public String getToolTipText(MouseEvent e) {
				return "在选择的路径下会创建\\cooper\\workspace文件夹";
			}
		};
		ToolTipManager.sharedInstance().registerComponent(workspacePath);
		if (setting.getWorkspacePath() != null) {
			workspacePath.setText(setting.getWorkspacePath());
		}

		JPanel pathPanel = new JPanel(new BorderLayout());
		pathPanel.add(BorderLayout.CENTER, workspacePath);
		pathPanel.add(BorderLayout.EAST, this.selectDirButton());
		this.add(BorderLayout.CENTER, pathPanel);

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createOkButton());
		buttonBar.add(createCancelButton());

		this.add(BorderLayout.SOUTH, buttonBar);

	}

	private JButton selectDirButton() {
		JButton b = new JButton("选择") {
			@Override
			public String getToolTipText(MouseEvent e) {
				return "在选择的路径下会创建\\cooper\\workspace文件夹";
			}
		};
		ToolTipManager.sharedInstance().registerComponent(b);

		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String defaultDir = null;
				if (workspacePath.getText() == null || workspacePath.getText().length() == 0) {
					defaultDir = System.getProperty("user.home");
				} else {
					int pos = StringUtil.lastIndexOf(workspacePath.getText(), "\\", 3);
					if (pos != -1) {
						defaultDir = workspacePath.getText().substring(0, pos);
					} else {
						defaultDir = System.getProperty("user.home");
					}
				}
				JFileChooser jFileChooser = new JFileChooser(defaultDir);
				jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = jFileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File f = jFileChooser.getSelectedFile();
					if (f.getAbsolutePath().endsWith("\\cooper\\workspace")) {
						workspacePath.setText(f.getAbsolutePath());
					} else {
						workspacePath.setText(f.getAbsolutePath() + "\\cooper\\workspace");
					}

				}
			}
		});
		return b;
	}

	private Component createOkButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_OK));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (workspacePath.getText() == null || workspacePath.getText().length() == 0) {
					JOptionPane.showMessageDialog((Component) e.getSource(), "请录入工作区路径", "alert",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					if (setting.getWorkspacePath() == null
							|| !setting.getWorkspacePath().equals(workspacePath.getText())) {
						setting.initWorkspace(workspacePath.getText());
						setting.save();
					}
					if (welcomeDialog != null) {// 客户端启动时
						frame.start(args, setting);
						welcomeDialog.dispose();
					} else {// 切换工作区时
						// 保存原UI信息
						UIPropertyConfigurator.getInstance().save();
						// 设置workspacePath
						JDependContext.setWorkspacePath(setting.getWorkspacePath());
						// 刷新GroupPanl
						frame.getGroupPanel().refreshGroup();
						// 刷新分析器
						frame.getCulturePanel().refreshAnalyzer();
						// 刷新新的UI信息
						UIPropertyConfigurator.getInstance().refresh();
						// 刷新Layout
						frame.refreshLayout();
					}
					WorkspaceSettingDialog.this.dispose();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "设置工作区失败", "alert", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		return button;
	}

	private Component createCancelButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Cancel));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (welcomeDialog != null) {
					welcomeDialog.dispose();
					WorkspaceSettingDialog.this.dispose();
					System.exit(0);
				} else {
					WorkspaceSettingDialog.this.dispose();
				}
			}
		});

		return button;
	}
}
