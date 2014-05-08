package jdepend.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class JarFileReader extends FileReader {

	private List<String> entryNames = new ArrayList<String>();

	public JarFileReader(boolean acceptInnerClasses) {
		super(acceptInnerClasses);
	}

	public Map<FileType, List<byte[]>> readDatas(InputStream in)
			throws IOException {

		JarInputStream jarInput = new JarInputStream(in);
		Map<FileType, List<byte[]>> fileDatases = new HashMap<FileType, List<byte[]>>();
		List<byte[]> classFileDatas = new ArrayList<byte[]>();
		List<byte[]> xmlFileDatas = new ArrayList<byte[]>();

		ZipEntry entry = jarInput.getNextJarEntry();
		while (entry != null) {
			if (this.acceptClassFileName(entry.getName())
					|| this.acceptXMLFileName(entry.getName())) {
				byte[] data = getData(jarInput);
				if (this.acceptClassFileName(entry.getName())) {
					classFileDatas.add(data);
					entryNames.add(parseClassName2(entry.getName()));
				} else {
					xmlFileDatas.add(data);
				}
			}
			entry = jarInput.getNextJarEntry();
		}

		jarInput.close();

		fileDatases.put(FileType.classType, classFileDatas);
		fileDatases.put(FileType.xmlType, xmlFileDatas);

		return fileDatases;
	}

	public List<String> getEntryNames() {
		return entryNames;
	}

	public byte[] getData(InputStream in) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int read = 0;
		int chunk = 0;
		byte[] data = new byte[256];
		while (-1 != (chunk = in.read(data))) {
			read += data.length;
			outputStream.write(data, 0, chunk);
		}

		return outputStream.toByteArray();

	}

	public static void main(String[] args) {
		File jarFile = new File("C:\\dom4j-1.6.1.jar");
		try {
			InputStream in = new FileInputStream(jarFile);
			Map<FileType, List<byte[]>> fileDatases = new JarFileReader(false)
					.readDatas(in);
			in.close();

			System.out.println(fileDatases.get(FileType.classType).size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
