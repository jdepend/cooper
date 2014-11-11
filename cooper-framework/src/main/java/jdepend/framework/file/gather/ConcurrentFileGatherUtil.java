package jdepend.framework.file.gather;

import java.io.File;

class ConcurrentFileGatherUtil extends AbstractFileGatherUtil {

	ThreadSecurityTargetFiles files;// 所有的文件统计结果

	Count count;// 跟踪并发线程数实体

	final int threadCount = 10;// 并发线程数上限

	ConcurrentFileGatherUtil() {
	}

	protected TargetFiles doGather() {

		files = new ThreadSecurityTargetFiles();
		count = new Count();
		// 分路径进行扫描
		for (File directory : this.getDirectories()) {
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
