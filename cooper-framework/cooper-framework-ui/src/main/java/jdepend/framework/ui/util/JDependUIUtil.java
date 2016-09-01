package jdepend.framework.ui.util;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class JDependUIUtil {

	public static Image getImage(String name) {
		try {
			return ImageIO.read(JDependUIUtil.class.getResourceAsStream("/image/" + name));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static File getSelectedFile(final String type, Component parent) {

		JFileChooser pathChooser = new JFileChooser();
		pathChooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					if (f.getName().toLowerCase().endsWith(type)) {
						return true;
					} else {
						return false;
					}
				}
			}

			@Override
			public String getDescription() {
				return "文件格式（" + type + "）";
			}
		});
		pathChooser.setSelectedFile(new File(type));
		int showSaveDialog = pathChooser.showSaveDialog(parent);
		if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
			return pathChooser.getSelectedFile();
		} else {
			return null;
		}
	}

	public static void addClickTipEffect(final JComponent component) {
		component.setForeground(new java.awt.Color(51, 51, 255));
		component.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		component.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				component.setOpaque(true);
				component.setBackground(new java.awt.Color(51, 51, 255));
				component.setForeground(new java.awt.Color(255, 255, 255));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				component.setOpaque(false);
				component.setBackground(new java.awt.Color(255, 255, 255));
				component.setForeground(new java.awt.Color(51, 51, 255));
			}
		});
	}
}
