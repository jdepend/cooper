package jdepend.framework.file.gather;

import java.io.File;

class SimpleFileGatherUtil extends AbstractFileGatherUtil {

	TargetFiles files;// 所有的文件统计结果

	SimpleFileGatherUtil() {
	}

	protected TargetFiles doGather() {

		files = new TargetFiles();
		// 分路径进行扫描
		for (File directory : this.getDirectories()) {
			files.addPlace(directory.getPath());
			collectFiles(directory.getPath(), directory);
		}
		return files;
	}

	void collectFiles(final String rootDir, File directory) {

		if (directory.isDirectory()) {
			String[] directoryFiles = directory.list();
			// 搜索文件夹下的文件或文件夹
			for (int i = 0; i < directoryFiles.length; i++) {
				final File file = new File(directory, directoryFiles[i]);
				if (acceptFile(file)) {
					files.addFile(rootDir, file);
				} else if (file.isDirectory()) {
					collectFiles(rootDir, file);
				}
			}
		} else if (acceptFile(directory)) {
			files.addFile(rootDir, directory);
		}
	}
}
