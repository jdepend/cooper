package jdepend.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;

public class FileUtil {

	public static byte[] getFileData(String fileName) throws JDependException {

		assert fileName != null && fileName.length() != 0;

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			return StreamUtil.getData(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + fileName + "]没有发现。");
			throw new JDependException("文件[" + fileName + "]读取失败。", e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException ignore) {
			}
		}
	}

	public static StringBuilder readFileContent(String filePath) throws JDependException {
		return FileUtil.readFileContent(filePath, "UTF-8");
	}

	public static void readFileContentKeyValue(String filePath, Properties p) throws JDependException {
		readFileContent(filePath, p, "KeyValue");
	}

	public static void readFileContentXML(String filePath, Properties p) throws JDependException {
		readFileContent(filePath, p, "XML");
	}

	private static void readFileContent(String filePath, Properties p, String format) throws JDependException {
		assert filePath != null && filePath.length() != 0;

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			if (fis != null) {
				if (format.equals("XML")) {
					p.loadFromXML(fis);
				} else {
					p.load(fis);
				}
			} else {
				throw new JDependException("读取文件[" + filePath + "]出错。");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + filePath + "]没有发现。");
			throw new JDependException("文件[" + filePath + "]读取失败。", e);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + filePath + "]读取失败。");
			throw new JDependException("文件[" + filePath + "]读取失败。", e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException ignore) {
			}
		}
	}

	public static StringBuilder getJarFileContent(JarFile jarFile, String key) throws IOException {

		byte[] fileData = null;
		Enumeration entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry e = (ZipEntry) entries.nextElement();
			if (key.equals(e.getName())) {

				InputStream is = null;
				try {
					is = jarFile.getInputStream(e);
					fileData = new byte[(int) e.getSize()];
					is.read(fileData);
				} finally {
					is.close();
				}
			}
		}
		if (fileData == null) {
			return new StringBuilder();
		} else {
			return new StringBuilder(new String(fileData));
		}
	}

	public static StringBuilder readFileContent(String filePath, String unicode) throws JDependException {

		assert filePath != null && filePath.length() != 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			byte[] buf = new byte[1024];
			StringBuilder sb = new StringBuilder();
			int length = fis.read(buf);
			while (length != -1) {
				sb.append(new String(buf, 0, length, unicode));
				buf = new byte[1024];
				length = fis.read(buf);
			}
			return sb;
		} catch (FileNotFoundException e) {
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + filePath + "]读取失败。");
			throw new JDependException("文件[" + filePath + "]读取失败。", e);
		} catch (IOException e) {
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + filePath + "]读取失败。");
			throw new JDependException("文件[" + filePath + "]读取失败。", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void saveFileContent(String filePath, StringBuilder content) throws JDependException {
		FileUtil.saveFileContent(filePath, content, "UTF-8");
	}

	public static void saveFileContent(String filePath, StringBuilder content, String unicode) throws JDependException {

		assert filePath != null && filePath.length() != 0;
		FileOutputStream fis = null;
		try {
			fis = new FileOutputStream(filePath);
			fis.write(content.toString().getBytes(unicode));
		} catch (FileNotFoundException e) {
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + filePath + "]保存失败。");
			throw new JDependException("文件[" + filePath + "]保存失败。", e);
		} catch (IOException e) {
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + filePath + "]保存失败。");
			throw new JDependException("文件[" + filePath + "]保存失败。", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void appendFileContent(String filePath, StringBuilder content, String unicode)
			throws JDependException {

		assert filePath != null && filePath.length() != 0;
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(filePath, true);
			writer.write(content.toString());
		} catch (IOException e) {
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + filePath + "]保存失败。");
			throw new JDependException("文件[" + filePath + "]保存失败。", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void saveFileContent(String filePath, Properties content) throws JDependException {

		assert filePath != null && filePath.length() != 0;
		FileOutputStream fis = null;
		try {
			fis = new FileOutputStream(filePath);
			content.storeToXML(fis, null);
		} catch (FileNotFoundException e) {
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + filePath + "]保存失败。");
			throw new JDependException("文件[" + filePath + "]保存失败。", e);
		} catch (IOException e) {
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + filePath + "]保存失败。");
			throw new JDependException("文件[" + filePath + "]保存失败。", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void deleteFile(String filePath) throws JDependException {

		assert filePath != null && filePath.length() != 0;

		File file = new File(filePath);
		deleteFile(file);
	}

	private static void deleteFile(File file) throws JDependException {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
				file.delete();
			}
		}
	}

	public static String getSrcPath(String commandMainClassName) {

		assert commandMainClassName != null && commandMainClassName.length() != 0;

		StringBuilder path = new StringBuilder(50);

		path.append(JDependContext.getRunningPath());

		if (commandMainClassName.startsWith("jdepend")) {
			path.append("//src//report//");
		} else {
			path.append("//src//app//");
		}
		path.append(commandMainClassName.replace(".", "//"));
		path.append(".java");

		return path.toString();

	}

	public static void createFile(String filePath, Properties properties) throws JDependException {

		assert filePath != null && filePath.length() != 0;

		createFile(filePath);
		saveFileContent(filePath, properties);

	}

	public static boolean exists(String filePath) {

		assert filePath != null && filePath.length() != 0;

		return (new File(filePath)).exists();
	}

	public static void createFile(String filePath) throws JDependException {

		assert filePath != null && filePath.length() != 0;

		File file = new File(filePath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			LogUtil.getInstance(FileUtil.class).systemError("文件[" + filePath + "]创建失败。");
			throw new JDependException("文件[" + filePath + "]创建失败。", e);
		}

	}

	public static byte[] readFile(String file) throws JDependException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return StreamUtil.getData(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new JDependException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void saveFile(String filePath, byte[] data) throws JDependException {
		createFile(filePath);
		FileOutputStream fis = null;
		try {
			fis = new FileOutputStream(filePath);
			fis.write(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new JDependException("文件[" + filePath + "]保存失败。", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new JDependException("文件[" + filePath + "]保存失败。", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void unZipFile(String targetPath, InputStream is) throws JDependException {
		try {
			ZipInputStream zis = new ZipInputStream(is);
			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {
				String zipPath = entry.getName();
				if (entry.isDirectory()) {
					File zipFolder = new File(targetPath + File.separator + zipPath);
					if (!zipFolder.exists()) {
						zipFolder.mkdirs();

					}
				} else {
					File file = new File(targetPath + File.separator + zipPath);
					if (!file.exists()) {
						File pathDir = file.getParentFile();
						pathDir.mkdirs();
						file.createNewFile();
						// copy data
						FileOutputStream fos = new FileOutputStream(file);
						int bread;
						while ((bread = zis.read()) != -1) {
							fos.write(bread);
						}
						fos.close();
					}

				}
			}
			zis.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("解压失败", e);
		}
	}

	public static void copyFile(String fileFromPath, String fileToPath) throws JDependException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(fileFromPath);
			out = new FileOutputStream(fileToPath);
			int length = in.available();
			int len = (length % 1024 == 0) ? (length / 1024) : (length / 1024 + 1);
			byte[] temp = new byte[1024];
			for (int i = 0; i < len; i++) {
				in.read(temp);
				out.write(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("copyFile[" + fileFromPath + "]to[" + fileToPath + "]失败", e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static String getFileName(String path) {
		return path.substring(path.lastIndexOf("/") + 1);
	}

	public static boolean acceptCompressFile(File file) {
		return FileUtil.isJar(file) || FileUtil.isZip(file) || FileUtil.isWar(file) || FileUtil.isDll(file);
	}

	private static boolean isWar(File file) {
		return existsWithExtension(file, ".war");
	}

	private static boolean isZip(File file) {
		return existsWithExtension(file, ".zip");
	}

	private static boolean isJar(File file) {
		return existsWithExtension(file, ".jar");
	}

	private static boolean isDll(File file) {
		return false;
		// return existsWithExtension(file, ".dll");
	}

	private static boolean existsWithExtension(File file, String extension) {
		return file.isFile() && file.getName().toLowerCase().endsWith(extension);
	}

}
