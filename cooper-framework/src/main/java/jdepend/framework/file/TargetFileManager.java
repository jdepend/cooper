package jdepend.framework.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.file.gather.AcceptFile;
import jdepend.framework.file.gather.FileGatherUtil;
import jdepend.framework.file.gather.FileGatherUtilFactory;
import jdepend.framework.file.gather.TargetFiles;
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

public class TargetFileManager extends FileReader implements AcceptFile {

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
			FileGatherUtil fgUtil = new FileGatherUtilFactory().createFileGatherUtil();
			fgUtil.setAcceptFile(this);
			fgUtil.setDirectories(directories);
			this.targetFiles = fgUtil.gather();
		}
		return this.targetFiles;
	}

	public AnalyzeData getAnalyzeData() throws IOException {

		data = new AnalyzeData();
		SimpileFileReader simpileFileReader = new SimpileFileReader(this.isAcceptInnerClasses());

		for (String place : this.extractFiles().getFiles().keySet()) {
			for (File file : this.extractFiles().getFiles().get(place)) {
				if (this.acceptClassFile(file) || this.acceptXMLFile(file)) {
					TargetFileInfo targetFileInfo = simpileFileReader.readDatas(place, file);
					if (targetFileInfo != null) {
						data.addFileInfo(place, targetFileInfo);
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

	@Override
	public boolean acceptFile(File file) {
		return acceptXMLFile(file) || acceptClassFile(file) || FileUtil.acceptCompressFile(file);
	}

}
