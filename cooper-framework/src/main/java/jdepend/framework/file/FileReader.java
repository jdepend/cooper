package jdepend.framework.file;

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

	/**
	 * 从文件路径中识别类名
	 * 
	 * @param fileName
	 * @return
	 */
	protected static String parseClassName(String fileName) {
		return fileName.replace('\\', '.').substring(0, fileName.length() - 6);
	}

	/**
	 * 从压缩包中识别类名
	 * 
	 * @param jarEntry
	 * @return
	 */
	protected static String parseClassName2(String jarEntry) {
		return jarEntry.replace('/', '.').substring(0, jarEntry.length() - 6);
	}

}
