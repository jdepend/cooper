package jdepend.webclient;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileUtil;
import jdepend.framework.util.TargetFileManager;

public final class AppletClient extends Applet {

	private JTextField path;
	private JTextField componentInfo;

	private String prevSelectedPath;

	@Override
	public void start() {
		super.start();
		this.setLayout(new BorderLayout());

		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

		JPanel left = new JPanel(new GridLayout(2, 1));
		JPanel right = new JPanel(new GridLayout(2, 1));

		JPanel right1 = new JPanel(new BorderLayout());
		JPanel right2 = new JPanel(new BorderLayout());

		right.add(right1);
		right.add(right2);

		JPanel left1 = new JPanel(new BorderLayout());
		JPanel left2 = new JPanel(new BorderLayout());

		left.add(left1);
		left.add(left2);

		left1.add(new JLabel("分析的jar："));
		left2.add(new JLabel("组件信息："));

		path = new JTextField();
		right1.add(BorderLayout.CENTER, path);

		JPanel pathBar = new JPanel();
		pathBar.add(selectDirButton(path));
		pathBar.add(new JLabel("（只能上传一个jar文件）"));
		right1.add(BorderLayout.EAST, pathBar);

		componentInfo = new JTextField();
		right2.add(BorderLayout.CENTER, componentInfo);
		right2.add(BorderLayout.EAST, new JLabel("(组件名以“;”分开，组件名为所属包的公共前缀)"));

		content.add(BorderLayout.WEST, left);
		content.add(BorderLayout.CENTER, right);

		JPanel buttonBar = new JPanel();
		buttonBar.add(createAnalyseButton());
		buttonBar.add(createOrgComponentButton());

		content.add(BorderLayout.SOUTH, buttonBar);

		this.add(BorderLayout.NORTH, content);
	}

	private JButton createOrgComponentButton() {
		JButton b = new JButton("组织组件");

		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					validateData();
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog((Component) e.getSource(), e1.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		return b;
	}

	private JButton createAnalyseButton() {
		JButton b = new JButton("直接分析");

		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					validateData();
					byte[] data = FileUtil.readFile(path.getText());

					URL url1 = new URL("http://localhost:8080/cooper/appletServlet");
					HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
					conn.setRequestMethod("POST");
					conn.setAllowUserInteraction(true);
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setUseCaches(true);
					conn.setRequestProperty("Content-Type", "multipart/form-data");

					OutputStream os = conn.getOutputStream();
					BufferedOutputStream bos = new BufferedOutputStream(os);
					bos.write(data, 0, data.length);
					bos.flush();
					bos.close();
					System.out.println(conn.getContentType() + ": " + conn.getResponseCode());

				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(AppletClient.this, e1.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return b;
	}

	private void validateData() throws JDependException {

		if (path.getText() == null || path.getText().length() == 0)
			throw new JDependException("分析的文件没有填写！");

		String[] paths = path.getText().replaceAll("\n", "").split(TargetFileManager.FilePathSplit);
		for (String path : paths) {
			File directory = new File(path);
			if (!directory.isDirectory() && !FileUtil.acceptCompressFile(directory)) {
				throw new IllegalArgumentException("分析的文件路径不合格。");
			}
		}
	}

	private JButton selectDirButton(final JTextField pathname) {
		JButton b = new JButton("...");

		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = null;
				jFileChooser = new JFileChooser(prevSelectedPath);
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = jFileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File f = jFileChooser.getSelectedFile();
					pathname.setText(f.getAbsolutePath());
					// 暂存上次选择的路径
					prevSelectedPath = f.getAbsolutePath();
				}
			}
		});
		return b;
	}
}
