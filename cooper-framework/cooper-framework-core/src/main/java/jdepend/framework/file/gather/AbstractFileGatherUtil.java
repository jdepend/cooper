package jdepend.framework.file.gather;

import java.io.File;
import java.util.List;

import jdepend.framework.log.LogUtil;

abstract class AbstractFileGatherUtil implements FileGatherUtil {

	private AcceptFile acceptFile;

	private List<File> directories;// 搜索的文件路径集合

	@Override
	public void setAcceptFile(AcceptFile acceptFile) {
		this.acceptFile = acceptFile;
	}

	protected boolean acceptFile(File file) {
		return acceptFile.acceptFile(file);
	}

	public List<File> getDirectories() {
		return directories;
	}

	@Override
	public void setDirectories(List<File> directories) {
		this.directories = directories;
	}

	@Override
	public TargetFiles gather() {
		long start = System.currentTimeMillis();
		TargetFiles targetFiles = this.doGather();
		LogUtil.getInstance(this.getClass()).systemLog(" gather time : " + (System.currentTimeMillis() - start));
		return targetFiles;
	}

	protected abstract TargetFiles doGather();

}
