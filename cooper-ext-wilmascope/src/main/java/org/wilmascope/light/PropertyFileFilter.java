/**
 * @author Christine
 */
package org.wilmascope.light;

import javax.swing.filechooser.FileFilter;

/**
 * File filter for the light configuration files which with extension
 * of".properties".
 */
public class PropertyFileFilter extends FileFilter {
	public boolean accept(java.io.File file) {
		if (file.isDirectory()) {
			return true;
		} else
			return file.getName().endsWith(".properties");
	}

	public String getDescription() {
		return "light configuration files (.properties)";
	}
}
