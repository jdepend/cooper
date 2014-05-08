package jdepend.framework.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import jdepend.framework.exception.JDependException;

/**
 * The <code>TargetFileManager</code> class is responsible for extracting Java
 * class files (<code>.class</code> files) from a collection of registered
 * directories.
 * 
 * @author <b>Abner</b>
 * 
 */

public class TargetFileManager extends FileReader {

	public static final String FilePathSplit = ";";

	private List<File> directories;

	private List<File> targetFiles;

	private int countClasses = -1;

	private Map<String, Collection<String>> targetFileGroupInfo;

	public TargetFileManager() {
		directories = new ArrayList<File>();
	}

	public boolean addDirectory(String name) throws IOException {

		File directory = new File(name);

		if (directory.isDirectory() || FileUtil.acceptCompressFile(directory)) {
			if (!directories.contains(directory)) {
				directories.add(directory);
				return true;
			} else {
				return false;
			}
		} else {
			throw new IOException("Invalid directory or JAR file: " + name);
		}
	}

	public List<File> getDirectories() {
		return directories;
	}

	public List<File> extractClassFiles() {
		if (targetFiles == null) {
			this.targetFiles = extractFiles();
		}
		List<File> files = new ArrayList<File>();
		for (File file : targetFiles) {
			if (this.acceptClassFile(file) || FileUtil.acceptCompressFile(file)) {
				files.add(file);
			}
		}
		return files;
	}

	public List<File> extractXMLFiles() {
		if (targetFiles == null) {
			this.targetFiles = extractFiles();
		}
		List<File> files = new ArrayList<File>();
		for (File file : targetFiles) {
			if (this.acceptXMLFile(file) || FileUtil.acceptCompressFile(file)) {
				files.add(file);
			}
		}
		return files;
	}

	private List<File> extractFiles() {
		if (targetFiles == null) {
			FileGatherUtil fgUtil = new FileGatherUtil(directories);
			this.targetFiles = fgUtil.gather();
			this.targetFileGroupInfo = fgUtil.getTargetFileGroupInfo();
		}
		return this.targetFiles;
	}

	public Map<String, Collection<String>> getTargetFileGroupInfo() {
		return targetFileGroupInfo;
	}

	public void setTargetFileGroupInfo(
			Map<String, Collection<String>> targetFileGroupInfo) {
		this.targetFileGroupInfo = targetFileGroupInfo;
	}

	public Map<FileType, List<byte[]>> getFileData() throws IOException {

		Map<FileType, List<byte[]>> fileDatases = new HashMap<FileType, List<byte[]>>();
		List<byte[]> classFileDatas = new ArrayList<byte[]>();
		List<byte[]> xmlFileDatas = new ArrayList<byte[]>();

		byte[] fileData = null;

		for (File file : this.extractFiles()) {
			if (this.acceptClassFile(file) || this.acceptXMLFile(file)) {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					fileData = StreamUtil.getData(fis);
					if (this.acceptClassFile(file)) {
						classFileDatas.add(fileData);
					} else {
						xmlFileDatas.add(fileData);
					}
				} catch (JDependException e) {
					e.printStackTrace();
					throw new IOException(e);
				} finally {
					if (fis != null) {
						fis.close();
					}
				}
			} else if (FileUtil.acceptCompressFile(file)) {

				InputStream in = new FileInputStream(file);
				JarFileReader reader = new JarFileReader(
						this.isAcceptInnerClasses());
				Map<FileType, List<byte[]>> jarFileDatases = reader
						.readDatas(in);
				in.close();

				String jarName = file.getName().substring(
						file.getName().lastIndexOf('\\') + 1);
				targetFileGroupInfo.put(jarName, reader.getEntryNames());

				classFileDatas.addAll(jarFileDatases.get(FileType.classType));
				xmlFileDatas.addAll(jarFileDatases.get(FileType.xmlType));
			} else {
				throw new IOException("File is not a valid "
						+ ".class, .jar, .war, .dll, or .zip file: "
						+ file.getPath());
			}
		}

		fileDatases.put(FileType.classType, classFileDatas);
		fileDatases.put(FileType.xmlType, xmlFileDatas);

		return fileDatases;
	}

	public int countClasses() {
		if (this.countClasses == -1) {
			this.countClasses = 0;
			List<File> files = this.extractClassFiles();
			for (File file : files) {
				if (FileUtil.acceptCompressFile(file)) {
					try {
						countClasses += this.countClasses(file);
					} catch (IOException ioe) {
						System.err.println("\n" + ioe.getMessage());
					}
				} else {
					countClasses++;
				}
			}
		}

		return this.countClasses;
	}

	private int countClasses(File file) throws IOException {
		int count = 0;
		JarInputStream jarInput = new JarInputStream(new FileInputStream(file));
		List<String> entryNames = new ArrayList<String>();

		ZipEntry entry = jarInput.getNextJarEntry();
		while (entry != null) {
			if (acceptClassFileName(entry.getName())) {
				count++;
				entryNames.add(parseClassName2(entry.getName()));
			}
			entry = jarInput.getNextJarEntry();
		}
		jarInput.close();

		String jarName = file.getName().substring(
				file.getName().lastIndexOf('\\') + 1);
		targetFileGroupInfo.put(jarName, entryNames);

		return count;
	}

	private boolean acceptFile(File file) {
		return acceptXMLFile(file) || acceptClassFile(file)
				|| FileUtil.acceptCompressFile(file);
	}

	private boolean acceptClassFile(File file) {
		if (!file.isFile()) {
			return false;
		}
		return acceptClassFileName(file.getName());
	}

	private boolean acceptXMLFile(File file) {
		if (!file.isFile()) {
			return false;
		}
		return acceptXMLFileName(file.getName());
	}

	class FileGatherUtil {

		List<File> files;// 所有的文件统计结果

		List<File> directories;// 搜索的文件路径集合

		Map<String, Collection<String>> targetFileGroupInfo;// 按dir对分析的文件进行分组

		Count count;// 跟踪并发线程数实体

		final int threadCount = 10;// 并发线程数上限

		FileGatherUtil(List<File> directories) {
			this.directories = directories;
		}

		public Map<String, Collection<String>> getTargetFileGroupInfo() {
			return targetFileGroupInfo;
		}

		synchronized List<File> gather() {

			files = new Vector<File>();// 采用Vector类型（线程安全）
			count = new Count();
			targetFileGroupInfo = new Hashtable();
			// 分路径进行扫描
			for (File directory : directories) {
				collectFiles(directory);
			}
			// 等到多个线程是否都结束
			synchronized (count) {
				while (count.count != 0) {
					try {
						count.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			// 返回结果
			return files;
		}

		void collectFiles(File directory) {

			if (acceptFile(directory)) {
				dFiles(directory);
				files.add(directory);// 将文件计入结果
			} else {
				String[] directoryFiles = directory.list();
				// 搜索文件夹下的文件或文件夹
				for (int i = 0; i < directoryFiles.length; i++) {

					final File file = new File(directory, directoryFiles[i]);
					if (acceptFile(file)) {
						dFiles(file);
						files.add(file);// 将文件计入结果
					} else if (file.isDirectory()) {
						boolean newThread = false;// 是否开启新的线程计算文件夹下的文件数量
						synchronized (count) {
							if (count.count < threadCount) {
								count.count++;
								newThread = true;
								count.notify();
							}
						}
						if (newThread) {
							new Thread() {// 开启新线程进行统计
								public void run() {
									collectFiles(file);
									// 当前文件夹搜索结束后，线程任务结束，改变跟踪并发线程数实体的值
									synchronized (count) {
										count.count--;
										count.notify();
									}
								}
							}.start();
						} else {
							collectFiles(file);// 在当前线程内进行统计
						}
					}
				}
			}
		}

		private void dFiles(File file) {
			if (acceptClassFile(file)) {
				for (File dir : this.directories) {
					if (file.getPath().startsWith(dir.getPath())) {
						if (!targetFileGroupInfo.containsKey(dir.getPath())) {
							targetFileGroupInfo.put(dir.getPath(),
									new Vector<String>());
						}
						targetFileGroupInfo.get(dir.getPath()).add(
								parseClassName(file.getPath().substring(
										dir.getPath().length() + 1)));
						break;
					}
				}
			}
		}
	}

	class Count {
		Integer count = 0;
	}
}
