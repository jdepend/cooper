package jdepend.framework.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.StreamUtil;

public class SimpileFileReader extends FileReader {

	public SimpileFileReader(boolean acceptInnerClasses) {
		super(acceptInnerClasses);
	}

	public TargetFileInfo readDatas(String place, File file) throws IOException {

		TargetFileInfo targetFileInfo;
		byte[] fileData = null;

		if (this.acceptClassFile(file) || this.acceptXMLFile(file)) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				fileData = StreamUtil.getData(fis);
				targetFileInfo = new TargetFileInfo();
				targetFileInfo.setName(parseClassName(place, file.getPath()));
				targetFileInfo.setContent(fileData);
				if (this.acceptClassFile(file)) {
					targetFileInfo.setType(TargetFileInfo.TYPE_CLASS);
				} else {
					targetFileInfo.setType(TargetFileInfo.TYPE_XML);
				}
				return targetFileInfo;
			} catch (JDependException e) {
				e.printStackTrace();
				throw new IOException(e);
			} finally {
				if (fis != null) {
					fis.close();
				}
			}
		} else {
			return null;
		}
	}

	/**
	 * 从文件路径中识别类名
	 * 
	 * @param fileName
	 * @return
	 */
	private static String parseClassName(String place, String fileName) {
		fileName = fileName.substring(place.length() + 1);
		return fileName.replace('\\', '.').substring(0, fileName.length() - 6);
	}
}
