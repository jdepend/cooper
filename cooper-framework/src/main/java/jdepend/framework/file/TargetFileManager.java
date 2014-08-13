package jdepend.framework.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileUtil;
import jdepend.framework.util.StreamUtil;

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

	private TargetFiles targetFiles;

	private AnalyzeData data;

	private int countClasses = -1;

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
		List<File> files = new ArrayList<File>();
		for (File file : extractFiles().getAllFiles()) {
			if (this.acceptClassFile(file) || FileUtil.acceptCompressFile(file)) {
				files.add(file);
			}
		}
		return files;
	}

	public List<File> extractXMLFiles() {
		List<File> files = new ArrayList<File>();
		for (File file : extractFiles().getAllFiles()) {
			if (this.acceptXMLFile(file) || FileUtil.acceptCompressFile(file)) {
				files.add(file);
			}
		}
		return files;
	}

	private TargetFiles extractFiles() {
		if (targetFiles == null) {
			FileGatherUtil fgUtil = new FileGatherUtil(directories);
			this.targetFiles = fgUtil.gather();
		}
		return this.targetFiles;
	}

	public AnalyzeData getAnalyzeData() throws IOException {

		data = new AnalyzeData();
		TargetFileInfo targetFileInfo;
		byte[] fileData = null;

		for (String place : this.extractFiles().getFiles().keySet()) {
			for (File file : this.extractFiles().getFiles().get(place)) {
				if (this.acceptClassFile(file) || this.acceptXMLFile(file)) {
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(file);
						fileData = StreamUtil.getData(fis);
						targetFileInfo = new TargetFileInfo();
						targetFileInfo.setName(file.getPath());
						targetFileInfo.setContent(fileData);
						if (this.acceptClassFile(file)) {
							targetFileInfo.setType(TargetFileInfo.TYPE_CLASS);
						} else {
							targetFileInfo.setType(TargetFileInfo.TYPE_XML);
						}
						data.addFileInfo(place, targetFileInfo);
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
					JarFileReader reader = new JarFileReader(this.isAcceptInnerClasses());
					List<TargetFileInfo> targetFileInfos2 = reader.readDatas(in);
					in.close();

					String jarName = file.getName().substring(file.getName().lastIndexOf('\\') + 1);
					for (TargetFileInfo targetFileInfo2 : targetFileInfos2) {
						data.addFileInfo(jarName, targetFileInfo2);
					}
				} else {
					throw new IOException("File is not a valid " + ".class, .jar, .war, .dll, or .zip file: "
							+ file.getPath());
				}
			}
		}

		return data;
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

		InputStream in = new FileInputStream(file);
		JarFileReader reader = new JarFileReader(this.isAcceptInnerClasses());
		int count = reader.countClasses(in);
		in.close();

		return count;
	}

	private boolean acceptFile(File file) {
		return acceptXMLFile(file) || acceptClassFile(file) || FileUtil.acceptCompressFile(file);
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

		ThreadSecurityTargetFiles files;// 所有的文件统计结果

		List<File> directories;// 搜索的文件路径集合

		Count count;// 跟踪并发线程数实体

		final int threadCount = 10;// 并发线程数上限

		FileGatherUtil(List<File> directories) {
			this.directories = directories;
		}

		synchronized TargetFiles gather() {

			files = new ThreadSecurityTargetFiles();
			count = new Count();
			// 分路径进行扫描
			for (File directory : directories) {
				files.addPlace(directory.getPath());
				collectFiles(directory.getPath(), directory);
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
			return files.toTargetFiles();
		}

		void collectFiles(final String rootDir, File directory) {

			if (directory.isDirectory()) {
				String[] directoryFiles = directory.list();
				// 搜索文件夹下的文件或文件夹
				for (int i = 0; i < directoryFiles.length; i++) {
					final File file = new File(directory, directoryFiles[i]);
					if (acceptFile(file)) {
						files.addFile(rootDir, file);// 将文件计入结果
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
									collectFiles(rootDir, file);
									// 当前文件夹搜索结束后，线程任务结束，改变跟踪并发线程数实体的值
									synchronized (count) {
										count.count--;
										count.notify();
									}
								}
							}.start();
						} else {
							collectFiles(rootDir, file);// 在当前线程内进行统计
						}
					}
				}
			} else if (acceptFile(directory)) {
				files.addFile(rootDir, directory);// 将文件计入结果
			}
		}
	}

	class Count {
		Integer count = 0;
	}
}
