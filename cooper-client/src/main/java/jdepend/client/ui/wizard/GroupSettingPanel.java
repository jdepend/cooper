package jdepend.client.ui.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;

import jdepend.client.core.local.config.CommandConfMgr;
import jdepend.client.core.local.config.GroupConf;
import jdepend.framework.context.JDependContext;
import jdepend.framework.context.Scope.SCOPE;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.client.ui.JDependCooper;

public class GroupSettingPanel extends JPanel {

	private JDependCooper frame;

	private JTextField groupname;

	private JTextArea pathname;

	private JTextArea srcPathName;

	private JTextArea filteredPackages;

	private JTextField attribute;

	public GroupSettingPanel(JDependCooper frame) {
		this.frame = frame;
		init(null);
	}

	public GroupSettingPanel(JDependCooper frame, String name) {
		this.frame = frame;
		init(name);
	}

	private void init(String name) {

		GroupConf group = null;
		if (name != null) {
			try {
				group = CommandConfMgr.getInstance().getTheGroup(name);
			} catch (JDependException e) {
				e.printStackTrace();
			}
		}

		this.setLayout(new GridBagLayout());
		// align all the labels
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		double itemY = c.weighty;

		JLabel groupLabel = new JLabel(
				BundleUtil.getString(BundleUtil.ClientWin_Group_Name) + ":");
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;
		add(groupLabel, c);

		groupname = new JTextField();
		if (group != null) {
			groupname.setText(group.getName());
			groupname.setEditable(false);
		}

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.9;
		add(groupname, c);

		JLabel pathLabel = new JLabel(
				BundleUtil.getString(BundleUtil.ClientWin_Group_Path) + ":");

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.1;
		c.weighty = 0.3;
		add(pathLabel, c);

		pathname = new JTextArea() {
			@Override
			public String getToolTipText(MouseEvent e) {
				return "选择jar文件或者class所在的文件夹";
			}
		};
		ToolTipManager.sharedInstance().registerComponent(pathname);

		if (group != null && group.getPath() != null) {
			pathname.setText(group.getPath().replaceAll(";", ";\n"));
		}

		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new BorderLayout());

		JPanel pathButtons = new JPanel(new FlowLayout());
		pathButtons.add(this.selectDirButton(pathname, "Path"));
		pathButtons.add(this.clearDirButton(pathname));

		pathPanel.add(new JScrollPane(pathname), BorderLayout.CENTER);
		pathPanel.add(pathButtons, BorderLayout.EAST);

		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.9;
		c.weighty = 0.3;
		add(pathPanel, c);

		JLabel srcPathLabel = new JLabel(
				BundleUtil.getString(BundleUtil.ClientWin_Group_SrcPath) + ":");

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.1;
		c.weighty = 0.3;
		add(srcPathLabel, c);

		srcPathName = new JTextArea() {
			@Override
			public String getToolTipText(MouseEvent e) {
				return "选择源文件所在的文件夹";
			}
		};
		ToolTipManager.sharedInstance().registerComponent(srcPathName);

		if (group != null && group.getSrcPath() != null) {
			srcPathName.setText(group.getSrcPath().replaceAll(";", ";\n"));
		}

		JPanel srcPathPanel = new JPanel();
		srcPathPanel.setLayout(new BorderLayout());

		JPanel srcPathButtons = new JPanel(new FlowLayout());
		srcPathButtons.add(this.selectDirButton(srcPathName, "srcPath"));
		srcPathButtons.add(this.clearDirButton(srcPathName));

		srcPathPanel.add(new JScrollPane(srcPathName), BorderLayout.CENTER);
		srcPathPanel.add(srcPathButtons, BorderLayout.EAST);

		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 0.9;
		c.weighty = 0.3;
		add(srcPathPanel, c);

		JLabel filteredPackagesLabel = new JLabel(
				BundleUtil
						.getString(BundleUtil.ClientWin_Group_FilteredPackages)
						+ ":");

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0.1;
		add(filteredPackagesLabel, c);

		filteredPackages = new JTextArea() {
			@Override
			public String getToolTipText(MouseEvent e) {
				return "本命令组忽略的包列表，一行一个包，右键查看全局忽略的包列表";
			}
		};
		ToolTipManager.sharedInstance().registerComponent(filteredPackages);
		filteredPackages.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					JPopupMenu menu = new JPopupMenu();
					JMenuItem gobalIgnoreItem = new JMenuItem("全局忽略的包列表");
					gobalIgnoreItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								gobalIgnore();
							} catch (Exception ex) {
								ex.printStackTrace();
								JOptionPane.showMessageDialog(
										GroupSettingPanel.this,
										ex.getMessage(), "alert",
										JOptionPane.ERROR_MESSAGE);
							}
						}

						private void gobalIgnore() {
							IgnoreSettingDialog d = new IgnoreSettingDialog(
									frame);
							d.setModal(true);
							d.setVisible(true);
						}
					});
					menu.add(gobalIgnoreItem);
					menu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}
		});
		if (group != null) {
			StringBuilder filteredPackagesBuffer = new StringBuilder();
			for (String filteredPackage : group.getFilteredPackages()) {
				filteredPackagesBuffer.append(filteredPackage);
				filteredPackagesBuffer.append("\n");
			}
			filteredPackages.setText(filteredPackagesBuffer.toString());
			filteredPackages.setCaretPosition(0);
		}

		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 0.9;
		add(new JScrollPane(filteredPackages), c);

		JLabel attributeLabel = new JLabel(
				BundleUtil.getString(BundleUtil.ClientWin_Group_Attribute)
						+ ":");
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 0.1;
		c.weighty = itemY;
		add(attributeLabel, c);

		attribute = new JTextField();
		if (group != null) {
			attribute.setText(group.getAttribute());
		}
		c.gridx = 1;
		c.gridy = 4;
		c.weightx = 0.9;
		add(attribute, c);

	}

	private JButton selectDirButton(final JTextArea pathname, final String mode) {
		JButton b = new JButton("...") {
			@Override
			public String getToolTipText(MouseEvent e) {
				if (mode.equals("Path")) {
					return "选择jar文件或者class所在的文件夹";
				} else {
					return "选择源文件所在的文件夹";
				}
			}
		};
		ToolTipManager.sharedInstance().registerComponent(b);

		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = null;
				if (mode.equals("Path")) {
					jFileChooser = new JFileChooser((String) JDependContext
							.getInfo(SCOPE.APP_SCOPSE,
									JDependContext.prevSelectedPath));
				} else {
					jFileChooser = new JFileChooser((String) JDependContext
							.getInfo(SCOPE.APP_SCOPSE,
									JDependContext.prevSelectedSrcPath));
				}
				jFileChooser
						.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jFileChooser.setMultiSelectionEnabled(true);
				int result = jFileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					for (File f : jFileChooser.getSelectedFiles()) {
						if (pathname.getText() != null
								&& pathname.getText().length() != 0) {
							pathname.setText(pathname.getText() + ";\n"
									+ f.getAbsolutePath());
						} else {
							pathname.setText(f.getAbsolutePath());
						}
						// 暂存上次选择的路径
						if (mode.equals("Path")) {
							JDependContext.setInfo(SCOPE.APP_SCOPSE,
									JDependContext.prevSelectedPath,
									f.getAbsolutePath());
						} else {
							JDependContext.setInfo(SCOPE.APP_SCOPSE,
									JDependContext.prevSelectedSrcPath,
									f.getAbsolutePath());
						}
					}
				}
			}
		});
		return b;
	}

	private JButton clearDirButton(final JTextArea target) {
		JButton b = new JButton(BundleUtil.getString(BundleUtil.Command_Clear));

		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				target.setText(null);
			}
		});
		return b;
	}

	public JTextField getGroupname() {
		return groupname;
	}

	public JTextArea getPathname() {
		return pathname;
	}

	public JTextArea getSrcPathName() {
		return srcPathName;
	}

	public List<String> getFilteredPackages() {

		List<String> filteredPackageList = new ArrayList<String>();

		for (String filteredPackage : filteredPackages.getText().split("\n")) {
			filteredPackageList.add(filteredPackage);
		}
		return filteredPackageList;
	}

	public JTextField getAttribute() {
		return attribute;
	}
}
