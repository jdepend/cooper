package jdepend.framework.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.util.zip.ZipEntry;
import jdepend.framework.util.zip.ZipInputStream;

public class JarFileReader extends FileReader {

	public JarFileReader(boolean acceptInnerClasses) {
		super(acceptInnerClasses);
	}

	public List<TargetFileInfo> readDatas(InputStream in) throws IOException {

		List<TargetFileInfo> targetFileInfos = new ArrayList<TargetFileInfo>();
		TargetFileInfo targetFileInfo;

		ZipInputStream zipInput = new ZipInputStream(in);

		ZipEntry entry = zipInput.getNextEntry();
		while (entry != null) {
			if (this.acceptClassFileName(entry.getName()) || this.acceptXMLFileName(entry.getName())) {
				targetFileInfo = new TargetFileInfo();
				byte[] data = getData(zipInput);
				targetFileInfo.setContent(data);
				if (this.acceptClassFileName(entry.getName())) {
					targetFileInfo.setName(parseClassName(entry.getName()));
					targetFileInfo.setType(TargetFileInfo.TYPE_CLASS);
				} else {
					targetFileInfo.setName(entry.getName());
					targetFileInfo.setType(TargetFileInfo.TYPE_XML);
				}
				targetFileInfos.add(targetFileInfo);
			}
			entry = zipInput.getNextEntry();
		}

		zipInput.close();

		return targetFileInfos;
	}

	public int countClasses(InputStream in) throws IOException {
		int count = 0;
		ZipInputStream zipInput = new ZipInputStream(in);
		List<String> entryNames = new ArrayList<String>();

		ZipEntry entry = zipInput.getNextEntry();
		while (entry != null) {
			if (acceptClassFileName(entry.getName())) {
				count++;
				entryNames.add(parseClassName(entry.getName()));
			}
			entry = zipInput.getNextEntry();

		}
		zipInput.close();

		return count;
	}

	private static byte[] getData(InputStream in) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int chunk = 0;
		byte[] data = new byte[256];
		while (-1 != (chunk = in.read(data))) {
			outputStream.write(data, 0, chunk);
		}

		return outputStream.toByteArray();

	}

	/**
	 * 从压缩包中识别类名
	 * 
	 * @param jarEntry
	 * @return
	 */
	private static String parseClassName(String jarEntry) {
		return jarEntry.replace('/', '.').substring(0, jarEntry.length() - 6);
	}

	public static void main(String[] args) {

		System.setProperty("sun.zip.encoding", "default");

		File jarFile = new File("c:\\交通_沈阳地铁.jar");
		try {
			InputStream in = new FileInputStream(jarFile);
			List<TargetFileInfo> targetFileInfos = new JarFileReader(false).readDatas(in);
			in.close();

			System.out.println(targetFileInfos.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
