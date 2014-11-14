package jdepend.framework.file;

import java.io.File;

public abstract class FileReader {

	private boolean acceptInnerClasses = true;

	public FileReader() {

	}

	public FileReader(boolean acceptInnerClasses) {
		super();
		this.acceptInnerClasses = acceptInnerClasses;
	}

	protected boolean acceptXMLFileName(String name) {
		if (!name.toLowerCase().endsWith(".xml")) {
			return false;
		}
		return true;
	}

	protected boolean acceptClassFileName(String name) {
		if (!acceptInnerClasses) {
			if (name.toLowerCase().indexOf("$") > 0) {
				return false;
			}
		}
		if (!name.toLowerCase().endsWith(".class")) {
			return false;
		}
		return true;
	}

	public void acceptInnerClasses(boolean b) {
		acceptInnerClasses = b;
	}

	protected boolean isAcceptInnerClasses() {
		return acceptInnerClasses;
	}

	protected boolean acceptClassFile(File file) {
		if (!file.isFile()) {
			return false;
		}
		return acceptClassFileName(file.getName());
	}

	protected boolean acceptXMLFile(File file) {
		if (!file.isFile()) {
			return false;
		}
		return acceptXMLFileName(file.getName());
	}
}
